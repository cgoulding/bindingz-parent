FROM java:8-jre-alpine

RUN mkdir -p /opt/bindingz/registry

COPY build/libs/*assembly.jar /opt/bindingz/registry/libs
COPY src/main/bin /opt/bindingz/registry/bin

ENTRYPOINT ["/opt/bindingz/registry/bin/start.sh"]
CMD ["--repository", "/opt/bindingz/registry/repository"]