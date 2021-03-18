FROM nginx:latest
MAINTAINER Rafael Marcos
COPY ./config /etc/nginx
EXPOSE 80 443
ENTRYPOINT ["nginx"]
# Parametros extras para o entrypoint
CMD ["-g", "daemon off;"]

