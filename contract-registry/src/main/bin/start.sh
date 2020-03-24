#!/usr/bin/env sh

java -cp $(find /opt/bindingz/registry/libs -iname "*assembly.jar") com.monadiccloud.bindingz.contract.registry.ApplicationCli "$@"
