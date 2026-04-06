#!/usr/bin/env python3
"""Seed movie_ratings.ratings with API-validated movie IDs.

Behavior:
- Checks whether each candidate movie_id exists via movie-info-service API.
- If a movie_id does not exist, it automatically moves to the next ID.
- Reuses previously validated movie IDs with configurable probability to mimic normal user behavior.
- Inserts only when a valid movie_id is found.
- Handles APIs that return HTTP 500 for unknown IDs by treating those IDs as not found (default).

Examples:
- Dry run (no inserts):
  python scripts/seed_ratings_with_movie_validation.py --count 200

- Execute inserts:
  python scripts/seed_ratings_with_movie_validation.py --count 200 --execute

- Execute inserts with repeated valid IDs (more realistic):
    python scripts/seed_ratings_with_movie_validation.py --count 200 --execute --repeat-valid-prob 0.8
"""

from __future__ import annotations

import argparse
import random
import subprocess
import sys
import urllib.error
import urllib.request
from dataclasses import dataclass
from typing import Dict, List, Optional


@dataclass
class DbConfig:
    mysql_path: str
    host: str
    port: int
    user: str
    password: str
    database: str
    table: str


def run_mysql_query(cfg: DbConfig, sql: str) -> List[str]:
    cmd = [
        cfg.mysql_path,
        "-h",
        cfg.host,
        "-P",
        str(cfg.port),
        "-u",
        cfg.user,
        f"-p{cfg.password}",
        "-D",
        cfg.database,
        "--batch",
        "--raw",
        "--skip-column-names",
        "-e",
        sql,
    ]
    result = subprocess.run(cmd, capture_output=True, text=True)
    if result.returncode != 0:
        stderr = (result.stderr or "").strip()
        raise RuntimeError(f"MySQL command failed: {stderr}")
    return [line.strip() for line in result.stdout.splitlines() if line.strip()]


def movie_exists(
    api_base_url: str,
    movie_id: int,
    cache: Dict[int, bool],
    timeout: float,
    strict_http_errors: bool,
) -> bool:
    if movie_id in cache:
        return cache[movie_id]

    url = f"{api_base_url.rstrip('/')}/{movie_id}"
    try:
        with urllib.request.urlopen(url, timeout=timeout) as response:
            exists = 200 <= response.getcode() < 300
            cache[movie_id] = exists
            return exists
    except urllib.error.HTTPError as exc:
        if exc.code == 404:
            cache[movie_id] = False
            return False
        # Some movie-info implementations return 500 for missing IDs.
        if exc.code == 500 and not strict_http_errors:
            cache[movie_id] = False
            return False
        raise RuntimeError(f"Movie API returned HTTP {exc.code} for {url}") from exc
    except urllib.error.URLError as exc:
        raise RuntimeError(
            f"Cannot reach movie API at {api_base_url}. "
            f"Start movie-info-service (expected on localhost:8082)."
        ) from exc


def find_next_valid_movie_id(
    start_id: int,
    api_base_url: str,
    cache: Dict[int, bool],
    timeout: float,
    scan_limit: int,
    strict_http_errors: bool,
) -> int:
    candidate = start_id
    for _ in range(scan_limit):
        if movie_exists(api_base_url, candidate, cache, timeout, strict_http_errors):
            return candidate
        candidate += 1
    raise RuntimeError(
        f"No valid movie_id found starting at {start_id} "
        f"within scan limit {scan_limit}."
    )


def get_count(cfg: DbConfig) -> int:
    out = run_mysql_query(cfg, f"SELECT COUNT(*) FROM {cfg.table};")
    return int(out[0])


def get_max_user_id(cfg: DbConfig) -> int:
    out = run_mysql_query(cfg, f"SELECT IFNULL(MAX(user_id), 0) FROM {cfg.table};")
    return int(out[0])


def insert_one_rating(cfg: DbConfig, user_id: int, movie_id: int, rating: int) -> int:
    sql = (
        f"INSERT IGNORE INTO {cfg.table} (user_id, movie_id, rating) "
        f"VALUES ({user_id}, {movie_id}, {rating});"
        "SELECT ROW_COUNT();"
    )
    out = run_mysql_query(cfg, sql)
    if not out:
        return 0
    return int(out[-1])


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Seed ratings with movie_id API validation")
    parser.add_argument("--count", type=int, default=200, help="Number of records to add")
    parser.add_argument("--execute", action="store_true", help="Actually insert records")
    parser.add_argument("--api-base-url", default="http://localhost:8082/movies", help="Movie API base URL")
    parser.add_argument("--api-timeout", type=float, default=3.0, help="Movie API timeout in seconds")
    parser.add_argument(
        "--strict-http-errors",
        action="store_true",
        help="Fail on HTTP 500 from movie API instead of treating it as a missing movie ID",
    )
    parser.add_argument("--start-movie-id", type=int, default=1, help="Starting movie_id candidate")
    parser.add_argument("--start-user-id", type=int, default=None, help="First user_id to use")
    parser.add_argument("--scan-limit", type=int, default=5000, help="Max IDs to scan per lookup")
    parser.add_argument(
        "--repeat-valid-prob",
        type=float,
        default=0.7,
        help="Probability of reusing a previously validated movie_id instead of discovering a new one",
    )
    parser.add_argument(
        "--repeat-pool-size",
        type=int,
        default=100,
        help="Max number of validated movie IDs to keep for reuse",
    )
    parser.add_argument(
        "--random-seed",
        type=int,
        default=42,
        help="Random seed for repeat behavior (set to another value for a different distribution)",
    )
    parser.add_argument("--mysql-path", default=r"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe")
    parser.add_argument("--db-host", default="localhost")
    parser.add_argument("--db-port", type=int, default=3306)
    parser.add_argument("--db-user", default="root")
    parser.add_argument("--db-password", default="1234")
    parser.add_argument("--db-name", default="movie_ratings")
    parser.add_argument("--db-table", default="ratings")
    parser.add_argument("--log-every", type=int, default=25, help="Progress log interval")
    return parser.parse_args()


def main() -> int:
    args = parse_args()
    if args.count <= 0:
        print("count must be greater than 0")
        return 1
    if not 0.0 <= args.repeat_valid_prob <= 1.0:
        print("repeat-valid-prob must be between 0.0 and 1.0")
        return 1
    if args.repeat_pool_size <= 0:
        print("repeat-pool-size must be greater than 0")
        return 1

    cfg = DbConfig(
        mysql_path=args.mysql_path,
        host=args.db_host,
        port=args.db_port,
        user=args.db_user,
        password=args.db_password,
        database=args.db_name,
        table=args.db_table,
    )

    before_count = get_count(cfg)
    current_user_id = args.start_user_id if args.start_user_id is not None else get_max_user_id(cfg) + 1
    rng = random.Random(args.random_seed)

    print("Mode:", "EXECUTE" if args.execute else "DRY RUN")
    print(f"Target: {cfg.database}.{cfg.table}")
    print(f"Rows before: {before_count}")
    print(f"Start user_id: {current_user_id}")
    print(f"Start movie_id candidate: {args.start_movie_id}")
    print(f"Repeat valid probability: {args.repeat_valid_prob}")
    print(f"Repeat pool size: {args.repeat_pool_size}")
    print(f"Random seed: {args.random_seed}")

    inserted = 0
    candidate_movie_id = args.start_movie_id
    movie_cache: Dict[int, bool] = {}
    valid_movie_pool: List[int] = []
    reused_ids = 0
    discovered_ids = 0

    while inserted < args.count:
        use_repeat = bool(valid_movie_pool) and rng.random() < args.repeat_valid_prob

        if use_repeat:
            valid_movie_id = rng.choice(valid_movie_pool)
            reused_ids += 1
        else:
            valid_movie_id = find_next_valid_movie_id(
                candidate_movie_id,
                args.api_base_url,
                movie_cache,
                args.api_timeout,
                args.scan_limit,
                args.strict_http_errors,
            )
            discovered_ids += 1

            # Move forward so the next discovery starts from the next candidate ID.
            candidate_movie_id = valid_movie_id + 1

            if valid_movie_id not in valid_movie_pool:
                valid_movie_pool.append(valid_movie_id)
                if len(valid_movie_pool) > args.repeat_pool_size:
                    valid_movie_pool.pop(0)

        rating_value = 1 + ((current_user_id + valid_movie_id) % 5)

        if args.execute:
            affected = insert_one_rating(cfg, current_user_id, valid_movie_id, rating_value)
            if affected == 1:
                inserted += 1
        else:
            inserted += 1

        current_user_id += 1

        if inserted % args.log_every == 0 or inserted == args.count:
            print(
                f"Processed {inserted}/{args.count} "
                f"(reused={reused_ids}, discovered={discovered_ids}, pool={len(valid_movie_pool)})"
            )

    if args.execute:
        after_count = get_count(cfg)
        print(f"Rows after: {after_count}")
        print(f"Inserted: {after_count - before_count}")
    else:
        print("Dry run finished. Re-run with --execute to insert.")

    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except Exception as exc:
        print(f"ERROR: {exc}", file=sys.stderr)
        raise SystemExit(1)
