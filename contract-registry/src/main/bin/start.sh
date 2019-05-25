#!/usr/bin/env sh

java -jar $(find /opt/bindingz/registry/libs -iname "contract-registry*.jar") "$@"
