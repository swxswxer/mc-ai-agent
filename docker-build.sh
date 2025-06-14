#!/bin/bash

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}开始构建 swx-ai-agent Docker 镜像...${NC}"

# 检查jar包是否存在
if [ ! -f "target/swx-ai-agent-0.0.1-SNAPSHOT.jar" ]; then
    echo -e "${RED}错误: 找不到 jar 包，请先运行 'mvn clean package' 构建项目${NC}"
    exit 1
fi

# 构建Docker镜像
echo -e "${YELLOW}构建Docker镜像...${NC}"
docker build -t swx-ai-agent:0.0.1-SNAPSHOT .

if [ $? -eq 0 ]; then
    echo -e "${GREEN}Docker 镜像构建成功！${NC}"
    echo -e "${GREEN}镜像名称: swx-ai-agent:0.0.1-SNAPSHOT${NC}"
    echo ""
    echo -e "${YELLOW}运行容器命令:${NC}"
    echo "docker run -d -p 8080:8080 --name swx-ai-agent swx-ai-agent:0.0.1-SNAPSHOT"
    echo ""
    echo -e "${YELLOW}查看容器日志:${NC}"
    echo "docker logs -f swx-ai-agent"
else
    echo -e "${RED}Docker 镜像构建失败！${NC}"
    exit 1
fi 