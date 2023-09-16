# Docker 镜像构建
FROM maven:3.8.1-jdk-8-slim as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# 复制自定义的settings.xml到Maven配置目录
COPY settings.xml /root/.m2/settings.xml

# 暴露应用程序所使用的端口
#EXPOSE 8102

# Build a release artifact.
RUN mvn package -DskipTests

# Run the web service on container startup.
CMD ["java","-jar","/app/target/bi-backend-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]
