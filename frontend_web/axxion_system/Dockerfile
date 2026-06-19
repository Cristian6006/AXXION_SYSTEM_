# Stage 1: Build the Vue application
FROM node:20-alpine AS builder

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm install --ignore-scripts --force

COPY . .
RUN npm run build --ignore-scripts --force

# Stage 2: Serve with nginx
FROM nginx:alpine

COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]