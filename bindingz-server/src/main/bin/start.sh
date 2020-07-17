#!/usr/bin/env sh

java -cp $(find /opt/bindingz/registry/libs -iname "*assembly.jar") io.bindingz.server.ApplicationCli "$@"
