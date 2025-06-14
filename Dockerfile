# Docker 镜像构建
# 使用 Eclipse Temurin JRE 21 精简版作为基础镜像
FROM eclipse-temurin:21-jre-alpine

# 复制jar包到容器中
COPY ./target/swx-ai-agent-0.0.1-SNAPSHOT.jar /tmp/swx-ai-agent-0.0.1-SNAPSHOT.jar

# 暴露端口（根据Spring Boot默认端口设置）
EXPOSE 8123


# 指定容器启动时运行的指令
ENTRYPOINT ["java", "-jar", "/tmp/swx-ai-agent-0.0.1-SNAPSHOT.jar"]


