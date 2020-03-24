/*
 * Copyright (c) 2019 Connor Goulding
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.monadiccloud.bindingz.contract.registry;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import org.springframework.boot.SpringApplication;

public class ApplicationCli {

    @Parameter(names = { "--repository", "-r" }, description = "Schema repository")
    String repository = null;

    @Parameter(names = { "--help", "-h" }, help = true)
    boolean help;

    public static void main(String[] args) {
        ApplicationCli cli = new ApplicationCli();
        JCommander commander = JCommander.newBuilder()
                .addObject(cli)
                .build();
        commander.setProgramName("Contract Registry");

        try {
            commander.parse(args);

            if (cli.help) {
                commander.usage();
            } else {
                if (cli.repository != null) {
                    System.setProperty("repository.filebacked.directory", cli.repository);
                    System.setProperty("spring.profiles.active", "filebacked");
                } else {
                    System.setProperty("spring.profiles.active", "prod");
                }

                SpringApplication.run(Application.class, args);
            }
        } catch (ParameterException e) {
            commander.usage();
        }
    }
}