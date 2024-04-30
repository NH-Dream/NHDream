FROM node:lts-alpine as build-stage

WORKDIR /homepage

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

FROM nginx:stable-alpine as production-stage

COPY --from=build-stage ./homepage/build /usr/share/nginx/html/

CMD ["nginx", "-g", "daemon off;"]