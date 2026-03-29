#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TARGET_DIR="$ROOT_DIR/projectlibre-app/target"

find_app_jar() {
	find "$TARGET_DIR" -maxdepth 1 -type f -name 'projectlibre-app-*.jar' ! -name '*-sources.jar' | sort | tail -n 1
}

APP_JAR="$(find_app_jar || true)"

if [[ -z "${APP_JAR:-}" ]]; then
	mvn -q -DskipTests package
	APP_JAR="$(find_app_jar)"
fi

exec java --add-opens=java.base/java.lang=ALL-UNNAMED -jar "$APP_JAR" "$@"
