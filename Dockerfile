## 使用官方的 OpenJDK 镜像作为基础镜像
#FROM amazoncorretto:17
#
## 设置工作目录
#WORKDIR /app
#
## 将 JAR 文件复制到工作目录
#COPY target/warehouse-0.0.1-SNAPSHOT.jar /app/warehouse-0.0.1-SNAPSHOT.jar
#
## 暴露应用程序运行的端口（假设应用程序运行在8080端口）
#EXPOSE 8080
#
## 运行 Java 应用程序
#CMD ["java", "-jar", "warehouse-0.0.1-SNAPSHOT.jar"]








# 第一阶段：准备 MySQL 数据库
FROM mysql:8.0 AS mysql

# 设置环境变量
ENV MYSQL_ROOT_PASSWORD=2021302930@Xjb
ENV MYSQL_DATABASE=storage

# 将 SQL 初始化脚本复制到容器中
COPY init.sql /docker-entrypoint-initdb.d/

# 使用官方的 OpenJDK 镜像作为基础镜像
FROM amazoncorretto:17

# 设置工作目录
WORKDIR /app

# 将 JAR 文件复制到工作目录
COPY target/warehouse-0.0.1-SNAPSHOT.jar /app/warehouse-0.0.1-SNAPSHOT.jar

# 暴露应用程序运行的端口（假设应用程序运行在8080端口）
EXPOSE 8080

# 安装 MySQL 客户端和 Redis 客户端
RUN yum update -y && \
    yum install -y mysql redis

# 运行 Java 应用程序
CMD ["java", "-jar", "warehouse-0.0.1-SNAPSHOT.jar"]
