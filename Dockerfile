# 第一阶段：构建应用
FROM maven:3.9-eclipse-temurin-21-alpine AS build

# 设置工作目录
WORKDIR /app

 # 配置Maven使用阿里云镜像源
 COPY settings.xml /usr/share/maven/conf/settings.xml

# 首先复制 pom.xml 文件，以利用 Docker 缓存层
COPY pom.xml .

# 复制 Maven 包装器文件
COPY .mvn/ .mvn/
COPY mvnw mvnw.cmd ./
# 设置 Maven 参数，确保使用全局 settings
ENV MAVEN_OPTS="-Dmaven.repo.local=/root/.m2/repository"

# 下载依赖项（使用系统 Maven 确保 settings.xml 生效）
RUN mvn dependency:go-offline -B -s /usr/share/maven/conf/settings.xml

# 复制源代码
COPY src/ src/

# 构建应用程序（使用系统 Maven）
RUN mvn package -DskipTests -s /usr/share/maven/conf/settings.xml

# 第二阶段：创建最终镜像
FROM eclipse-temurin:21-jre-alpine

# 设置工作目录
WORKDIR /app

# 创建目录 - 如果需要挂载数据卷
RUN mkdir -p /app/logs /app/chatMemory

# 从构建阶段复制构建好的jar文件
COPY --from=build /app/target/*.jar app.jar

# 暴露应用程序端口（根据Spring Boot项目通常是8080）
EXPOSE 8123

# 设置JVM参数和应用启动命令
ENTRYPOINT ["java", "-Xms512m", "-Xmx1024m", "-jar", "app.jar"]
