FROM openjdk:14-alpine

## Install git
RUN apk update && apk upgrade && \
    apk add --no-cache bash git openssh

# Setup app
WORKDIR /app
COPY build/dockerbin bin
COPY src/bin bin
COPY src/config-default config-default

RUN chmod +x bin/start.sh

##
## Volumes
##
VOLUME [ "/app/config" ]
EXPOSE 80
ENTRYPOINT ["bin/start.sh"]
