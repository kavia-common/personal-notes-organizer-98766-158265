@echo off
REM Delegating Gradle wrapper for CI: runs the real wrapper in android_frontend
setlocal
set SCRIPT_DIR=%~dp0
pushd "%SCRIPT_DIR%\android_frontend"
call gradlew.bat %*
popd
endlocal
