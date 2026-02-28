# 🚀 Spring Boot Dockerized Application

This project demonstrates how to:
- Build a Spring Boot REST API
- Package it using Maven
- Dockerize the application
- Use Docker Port Binding
- Run containerized app locally

---

## 🛠 Tech Stack

- Java 21
- Spring Boot
- Maven
- Docker
- IntelliJ IDEA

---

## 📂 Project Structure
```
demo/
├── src/
├── pom.xml
├── Dockerfile
└── README.md
```

---

## ⚙️ Build the Application

```
mvn clean package
```
Jar will be created inside:
```
target/*.jar
```
🐳 Build Docker Image

```bash
docker build -t springboot-app .
```
🚀 Run Docker Container
```
docker run -p 8080:8080 springboot-app
```
Access in browser:
```
http://localhost:8080/hello
```
🔌 Port Binding Explanation
```
docker run -p <host_port>:<container_port>
```

Example:
```
docker run -p 9090:8080 springboot-app
```
