# MedCare — Troubleshooting Guide

---

## ❌ Error: `ExceptionInInitializerError: TypeTag::UNKNOWN`

**Cause:** Java version mismatch with Lombok or Maven compiler.

**Fix — Option 1: Use Java 17 (Recommended)**
```bash
# Check your current Java version
java -version

# If it shows Java 8, 11, or 21 with old Maven, switch to Java 17
# Download Java 17: https://adoptium.net/temurin/releases/?version=17

# On Windows - set JAVA_HOME in System Environment Variables:
# JAVA_HOME = C:\Program Files\Eclipse Adoptium\jdk-17.x.x.x-hotspot
# PATH      = %JAVA_HOME%\bin;%PATH%

# On Linux/Mac:
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

**Fix — Option 2: Clear Maven cache (corrupted download)**
```bash
# Windows
rmdir /s /q %USERPROFILE%\.m2\repository\org\projectlombok

# Linux/Mac
rm -rf ~/.m2/repository/org/projectlombok

# Then rebuild
mvn clean package -DskipTests
```

**Fix — Option 3: Use IntelliJ IDEA**
1. Open IntelliJ → File → Open → select the `medcare` folder
2. IntelliJ auto-detects `pom.xml` and imports the project
3. Go to File → Project Structure → Project → set SDK to Java 17
4. Enable annotation processing: Settings → Build → Compiler → Annotation Processors → ✅ Enable
5. Run `MedCareApplication.java`

---

## ❌ Error: `Communications link failure` / `Access denied for user 'root'`

**Cause:** MySQL not running or wrong password.

**Fix:**
```bash
# Start MySQL
# Windows:   net start mysql  (or start from Services)
# Linux:     sudo systemctl start mysql
# Mac:       brew services start mysql

# Set your password in application.properties:
spring.datasource.password=YOUR_ACTUAL_MYSQL_PASSWORD

# If you have no password set:
spring.datasource.password=
```

---

## ❌ Error: `Unknown database 'medcare_db'`

**Fix:**
```sql
mysql -u root -p
CREATE DATABASE medcare_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
exit;
```

---

## ❌ Error: `Port 8080 already in use`

**Fix:**
```bash
# Windows - find and kill the process using port 8080
netstat -ano | findstr :8080
taskkill /PID <PID_NUMBER> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```
Or change port in `application.properties`:
```properties
server.port=9090
```

---

## ❌ Error: `mvn: command not found`

**Fix:**
- **Windows:** Download from https://maven.apache.org/download.cgi, extract, add `bin` folder to PATH
  - Or: `winget install Apache.Maven`
- **Linux:** `sudo apt install maven`
- **Mac:** `brew install maven`

---

## ❌ Error: `No Lombok-generated methods` in IntelliJ

**Fix:**
1. Install Lombok plugin: Settings → Plugins → search "Lombok" → Install
2. Enable annotation processing: Settings → Build, Execution, Deployment → Compiler → Annotation Processors → ✅ Enable annotation processing
3. Restart IntelliJ

---

## ✅ Verified Working Setup

| Component | Version |
|-----------|---------|
| Java      | 17 (LTS) — **recommended** |
| Maven     | 3.8+ |
| MySQL     | 8.0+ |
| Spring Boot | 3.2.5 |
| Lombok    | 1.18.32 |

---

## 🚀 Quick Start (after fixing errors)

```bash
# 1. Navigate to project folder
cd medcare

# 2. Set MySQL password in src/main/resources/application.properties

# 3. Clean build
mvn clean package -DskipTests

# 4. Run
java -jar target/medcare-1.0.0.jar

# 5. Open browser
http://localhost:8080
```
