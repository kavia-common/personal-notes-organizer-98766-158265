#!/usr/bin/env bash
# Delegating Gradle wrapper for CI: runs the real wrapper in android_frontend
set -euo pipefail
SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" &> /dev/null && pwd)"
cd "${SCRIPT_DIR}/android_frontend"
exec ./gradlew "$@"
