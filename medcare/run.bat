@echo off
echo ==========================================
echo   MedCare - Healthcare Platform
echo ==========================================
echo.

REM Check Java version
echo [CHECK] Java version:
java -version
echo.
echo NOTE: This project requires Java 17 or higher.
echo If you see errors, download Java 17 from:
echo   https://adoptium.net/temurin/releases/?version=17
echo.

REM Check Maven
where mvn >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Maven not found!
    echo Download Maven from: https://maven.apache.org/download.cgi
    echo Or install via: winget install Apache.Maven
    pause
    exit /b 1
)

echo [1/2] Building project (first run downloads dependencies ~50MB)...
call mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo [ERROR] Build failed. Check errors above.
    pause
    exit /b 1
)

echo.
echo [2/2] Starting MedCare Server...
echo.
echo  ============================================
echo   Open your browser: http://localhost:8080
echo  ============================================
echo.
echo  Demo accounts:
echo    Patient : patient@medcare.com / patient123
echo    Doctor  : arjun@medcare.com   / doctor123
echo    Admin   : admin@medcare.com   / admin123
echo.
echo  Press Ctrl+C to stop the server.
echo.
java -jar target\medcare-1.0.0.jar
pause
