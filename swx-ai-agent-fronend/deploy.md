# 部署指南

## 开发环境部署

### 1. 启动后端服务
确保后端Spring Boot应用在 `localhost:8123` 运行：

```bash
cd /Users/swxswx/Desktop/code/mc-ai-agent/swx-ai-agent
mvn spring-boot:run
```

### 2. 启动前端服务
```bash
cd /Users/swxswx/Desktop/code/mc-ai-agent/swx-ai-agent/swx-ai-agent-fronend
npm install
npm run dev
```

前端将在 `http://localhost:3000` 启动

## 生产环境部署

### 1. 构建前端项目
```bash
npm run build
```

### 2. 部署到Web服务器
将 `dist` 目录的内容部署到Nginx或Apache等Web服务器。

### 3. Nginx配置示例
```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    # 前端静态文件
    location / {
        root /path/to/dist;
        try_files $uri $uri/ /index.html;
    }
    
    # API代理到后端
    location /api/ {
        proxy_pass http://localhost:8123/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # SSE支持
        proxy_buffering off;
        proxy_cache off;
        proxy_set_header Connection '';
        proxy_http_version 1.1;
        chunked_transfer_encoding off;
    }
}
```

## Docker部署

### 1. 创建Dockerfile
```dockerfile
FROM node:18-alpine as build

WORKDIR /app
COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

### 2. 构建和运行
```bash
docker build -t mc-ai-frontend .
docker run -p 80:80 mc-ai-frontend
```

## 环境变量配置

可以通过环境变量配置后端API地址：

```bash
# .env.production
VITE_API_BASE_URL=https://your-api-domain.com
```

然后在 `vite.config.js` 中使用：

```javascript
export default defineConfig({
  // ...
  define: {
    __API_BASE_URL__: JSON.stringify(process.env.VITE_API_BASE_URL || 'http://localhost:8123')
  }
})
```

## 性能优化

### 1. 启用Gzip压缩
在Nginx中启用Gzip：

```nginx
gzip on;
gzip_types text/plain text/css application/json application/javascript text/xml application/xml application/xml+rss text/javascript;
```

### 2. 设置缓存策略
```nginx
location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
    expires 1y;
    add_header Cache-Control "public, immutable";
}
```

### 3. 启用HTTP/2
```nginx
listen 443 ssl http2;
```

## 监控和日志

### 1. 前端错误监控
可以集成Sentry等错误监控服务：

```javascript
import * as Sentry from "@sentry/vue";

Sentry.init({
  app,
  dsn: "YOUR_SENTRY_DSN",
});
```

### 2. 访问日志
配置Nginx访问日志：

```nginx
access_log /var/log/nginx/mc-ai-frontend.access.log;
error_log /var/log/nginx/mc-ai-frontend.error.log;
```

## 安全配置

### 1. HTTPS配置
```nginx
server {
    listen 443 ssl;
    ssl_certificate /path/to/cert.pem;
    ssl_certificate_key /path/to/key.pem;
    
    # 安全头
    add_header X-Frame-Options DENY;
    add_header X-Content-Type-Options nosniff;
    add_header X-XSS-Protection "1; mode=block";
}
```

### 2. CORS配置
在后端Spring Boot中配置CORS：

```java
@CrossOrigin(origins = "https://your-frontend-domain.com")
```

## 故障排除

### 常见问题

1. **API请求失败**
   - 检查后端服务是否运行
   - 验证代理配置是否正确
   - 检查CORS设置

2. **静态资源加载失败**
   - 确认构建产物正确
   - 检查Web服务器配置
   - 验证文件权限

3. **SSE连接问题**
   - 确认代理配置支持流式传输
   - 检查防火墙设置
   - 验证浏览器兼容性

## 备份和恢复

### 1. 代码备份
定期备份源代码到Git仓库

### 2. 配置备份
备份Nginx配置和环境变量文件

### 3. 数据库备份
定期备份后端数据库（如果有）

---

**部署完成后，访问您的域名即可使用Minecraft AI Assistant平台！** 