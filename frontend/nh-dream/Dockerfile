FROM node:lts-alpine as build-stage-front

WORKDIR /homepage/front

COPY frontend/nh-dream/package*.json .

RUN npm install

COPY frontend/nh-dream .

RUN npm run build




FROM node:lts-alpine as build-stage-admin

WORKDIR /homepage/admin

COPY admin/nhdream/package*.json .

RUN npm install

COPY admin/frontend .

RUN npm run build



FROM nginx:stable-alpine as production-stage

COPY --from=build-stage-front ./homepage/front/dist /usr/share/nginx/html/front

COPY --from=build-stage-admin ./homepage/admin/dist /usr/share/nginx/html/admin

CMD ["nginx", "-g", "daemon off;"]