# Test Commands for Catering Project

## 🎯 **All Tests with Coverage (Recommended for CI/CD)**
```bash
mvn clean test
```
- ✅ **Forking enabled** (default)
- ✅ **JaCoCo coverage** enabled
- ✅ **All modules** tested
- ⏱️ **Slower** but provides coverage reports

## 🚀 **Fast Tests (No Coverage, for Development)**
```bash
mvn test -DforkCount=0 -Djacoco.skip=true
```
- ❌ **No forking** (faster)
- ❌ **No coverage** (no JaCoCo agent)
- ✅ **All modules** tested
- ⚡ **Much faster** for development feedback

## 📊 **Generate Coverage Reports Only**
```bash
mvn jacoco:report
```
- Generates HTML coverage reports in `target/site/jacoco/`
- Requires previous test run with coverage

## 🧪 **Test Individual Modules**

### Domain Module
```bash
cd domain && mvn test
```

### Application Module
```bash
cd application && mvn test
```

### Infrastructure Module
```bash
cd infrastructure && mvn test
```

### Presentation Module
```bash
cd presentation && mvn test
```

## 📁 **Coverage Report Locations**
After running tests with coverage, find reports at:
- `domain/target/site/jacoco/index.html`
- `application/target/site/jacoco/index.html`
- `infrastructure/target/site/jacoco/index.html`
- `presentation/target/site/jacoco/index.html`

## 🔧 **Configuration Details**
- **Java Version:** 17
- **Spring Boot:** 3.4.0
- **JaCoCo:** 0.8.8
- **Surefire:** 3.2.5
- **Coverage Threshold:** 80% line coverage

## 🚨 **Troubleshooting**
If you encounter issues:
1. Try the fast test command first: `mvn test -DforkCount=0 -Djacoco.skip=true`
2. If that works, the issue is with JaCoCo/Surefire forking
3. Check Java version: `java -version`
4. Check Maven version: `mvn -version` 