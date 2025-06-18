# 构建阶段
FROM node:18-alpine as build-stage

# 添加构建参数
ARG NODE_ENV=production
ARG BUILD_DATE
ARG VCS_REF

# 设置环境变量
ENV NODE_ENV=$NODE_ENV
ENV CI=true

# 设置工作目录
WORKDIR /app

# 安装 git (某些 npm 包可能需要)
RUN apk add --no-cache git

# 复制 package.json 和 package-lock.json（利用 Docker 缓存层）
COPY package*.json ./

# 清理 npm 缓存并安装依赖
RUN npm ci --only=production --silent && \
    npm cache clean --force

# 复制其余源代码
COPY . .

# 构建项目
RUN npm run build

# 生产阶段
FROM nginx:stable-alpine as production-stage

# 添加标签信息
LABEL maintainer="your-email@example.com" \
      version="1.0.0" \
      description="Vue.js Frontend Application" \
      build-date=$BUILD_DATE \
      vcs-ref=$VCS_REF

# 安装 curl 用于健康检查
RUN apk add --no-cache curl

# 创建非特权用户
RUN addgroup -g 1001 -S nginx && \
    adduser -S -D -H -u 1001 -h /var/cache/nginx -s /sbin/nologin -G nginx -g nginx nginx

# 将构建好的文件从构建阶段复制到nginx服务器的默认目录
COPY --from=build-stage /app/dist /usr/share/nginx/html

# 复制自定义nginx配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 设置正确的权限
RUN chown -R nginx:nginx /usr/share/nginx/html && \
    chmod -R 755 /usr/share/nginx/html

# 切换到非特权用户
USER nginx

# 暴露80端口
EXPOSE 80

# 添加健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:80/ || exit 1

# 启动nginx
CMD ["nginx", "-g", "daemon off;"] 