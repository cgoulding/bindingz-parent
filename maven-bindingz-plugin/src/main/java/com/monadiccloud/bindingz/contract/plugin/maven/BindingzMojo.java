package com.monadiccloud.bindingz.contract.plugin.maven;

import com.monadiccloud.bindingz.contract.plugin.maven.tasks.ProcessResourcesTask;
import com.monadiccloud.bindingz.contract.plugin.maven.tasks.PublishResourcesTask;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Mojo(name = "processResources", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class BindingzMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "http://localhost:8080", property = "registry", required = true)
    private String registry;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/bindingz", property = "targetSourceDirectory")
    private File targetSourceDirectory;

    @Parameter(defaultValue = "${project.build.directory}/generated-resources/bindingz", property = "targetResourceDirectory")
    private File targetResourceDirectory;

    @Parameter(defaultValue = "${project.build.directory}/bindingz/distribution", property = "targetDistributionDirectory")
    private File targetDistributionDirectory;

    @Parameter(property = "processConfigurations")
    private Collection<ProcessConfiguration> processConfigurations;

    @Parameter(property = "publishConfigurations")
    private Collection<PublishConfiguration> publishConfigurations;

    public void execute() throws MojoExecutionException {
        if (!targetSourceDirectory.exists()) {
            targetSourceDirectory.mkdirs();
        }
        if (!targetResourceDirectory.exists()) {
            targetResourceDirectory.mkdirs();
        }
        if (!targetDistributionDirectory.exists()) {
            targetDistributionDirectory.mkdirs();
        }

        if (processConfigurations != null) {
            try {
                new ProcessResourcesTask(registry, targetSourceDirectory, targetResourceDirectory, processConfigurations).execute();
            } catch (IOException e) {
                throw new MojoExecutionException(e.getMessage(), e);
            }
        }

        if (publishConfigurations != null) {
            try {
                List<URL> urls = new ArrayList<>();
                for (String element : project.getCompileClasspathElements()) {
                    urls.add(new File(element).toURI().toURL());
                }
                ClassLoader classLoader = new URLClassLoader(urls.toArray(new URL[]{}), this.getClass().getClassLoader());
                Arrays.stream(((URLClassLoader) classLoader).getURLs()).forEach(x -> System.out.println(x));
                new PublishResourcesTask(registry, targetDistributionDirectory, publishConfigurations, classLoader).execute();
            } catch (IOException e) {
                throw new MojoExecutionException(e.getMessage(), e);
            } catch (DependencyResolutionRequiredException e) {
                throw new MojoExecutionException(e.getMessage(), e);
            }
        }
    }
}
