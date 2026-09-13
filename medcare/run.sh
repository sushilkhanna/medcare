#!/bin/bash
echo "=========================================="
echo "  MedCare - Healthcare Platform"
echo "=========================================="
echo ""

# Check Java
if ! command -v java &> /dev/null; then
    echo "[ERROR] Java not found! Install Java 17:"
    echo "  Ubuntu/Debian: sudo apt install openjdk-17-jdk"
    echo "  Mac:           brew install openjdk@17"
    echo "  Download:      https://adoptium.net/temurin/releases/?version=17"
    exit 1
fi

echo "[CHECK] Java version:"
java -version
echo ""

# Check Maven
if ! command -v mvn &> /dev/null; then
    echo "[ERROR] Maven not found! Install Maven:"
    echo "  Ubuntu/Debian: sudo apt install maven"
    echo "  Mac:           brew install maven"
    echo "  Download:      https://maven.apache.org/download.cgi"
    exit 1
fi

echo "[1/2] Building project (first run downloads ~50MB dependencies)..."
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "[ERROR] Build failed. Check errors above."
    exit 1
fi

echo ""
echo "[2/2] Starting MedCare Server..."
echo ""
echo " ============================================"
echo "  Open your browser: http://localhost:8080"
echo " ============================================"
echo ""
echo " Demo accounts:"
echo "   Patient : patient@medcare.com / patient123"
echo "   Doctor  : arjun@medcare.com   / doctor123"
echo "   Admin   : admin@medcare.com   / admin123"
echo ""
echo " Press Ctrl+C to stop the server."
echo ""
java -jar target/medcare-1.0.0.jar
