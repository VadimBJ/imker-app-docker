FROM node:18.19.0-slim as builder

COPY package.json package-lock.json ./

RUN npm install && mkdir /imker && mv ./node_modules ./imker

WORKDIR /imker

COPY . .

RUN npm run build


FROM nginx:1.25.3-alpine

COPY ./.nginx/nginx.conf /etc/nginx/nginx.conf

RUN mkdir /usr/share/nginx/html/imker
COPY --from=builder /imker/build /usr/share/nginx/html/imker
RUN cp -r /usr/share/nginx/html/imker/. /usr/share/nginx/html/

EXPOSE 3000 80

ENTRYPOINT ["nginx", "-g", "daemon off;"]
