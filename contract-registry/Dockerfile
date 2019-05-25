FROM java:8-jre-alpine

RUN mkdir -p /opt/bindingz/registry

COPY build/libs /opt/bindingz/registry/libs
COPY src/main/bin /opt/bindingz/registry/bin

ENTRYPOINT ["/opt/bindingz/registry/bin/start.sh", "--repository", "/opt/bindingz/registry/repository"]