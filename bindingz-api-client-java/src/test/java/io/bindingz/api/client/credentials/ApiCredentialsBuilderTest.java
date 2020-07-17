package io.bindingz.api.client.credentials;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ApiCredentialsBuilderTest {

    @Before
    public void setUp() {
        System.clearProperty("BINDINGZ_API_KEY");
        System.clearProperty("BINDINGZ_HOSTNAME");
    }

    @Test
    public void testDefault() {
        Assert.assertEquals(null, new ApiCredentialsBuilder().build().getApiKey());
        Assert.assertEquals("https://api.bindingz.io", new ApiCredentialsBuilder().build().getHostname());
    }

    @Test
    public void testEnvironment() {
        System.setProperty("BINDINGZ_API_KEY", "asdf");

        Assert.assertEquals("asdf", new ApiCredentialsBuilder().build().getApiKey());
        Assert.assertEquals("https://api.bindingz.io", new ApiCredentialsBuilder().build().getHostname());

        System.setProperty("BINDINGZ_HOSTNAME", "http://somewhere.com");

        Assert.assertEquals("asdf", new ApiCredentialsBuilder().build().getApiKey());
        Assert.assertEquals("http://somewhere.com", new ApiCredentialsBuilder().build().getHostname());
    }

    @Test
    public void testFile() throws IOException {
        File config = createConfigFile("api1", "hostname2");
        Assert.assertEquals("api1", new ApiCredentialsBuilder(config.toString()).build().getApiKey());
        Assert.assertEquals("hostname2", new ApiCredentialsBuilder(config.toString()).build().getHostname());
    }

    @Test
    public void testEnvironmentFirst() throws IOException {
        System.setProperty("BINDINGZ_API_KEY", "asdf");
        System.setProperty("BINDINGZ_HOSTNAME", "http://somewhere.com");

        File config = createConfigFile("api1", "hostname2");

        Assert.assertEquals("asdf", new ApiCredentialsBuilder(config.toString()).build().getApiKey());
        Assert.assertEquals("http://somewhere.com", new ApiCredentialsBuilder(config.toString()).build().getHostname());
    }

    @Test
    public void testPartialEnvironmentFirst() throws IOException {
        System.setProperty("BINDINGZ_API_KEY", "asdf");
        File config = createConfigFile("api1", "hostname2");

        Assert.assertEquals("asdf", new ApiCredentialsBuilder(config.toString()).build().getApiKey());
        Assert.assertEquals("hostname2", new ApiCredentialsBuilder(config.toString()).build().getHostname());
    }

    @Test
    public void testConfiguredFirst() throws IOException {
        System.setProperty("BINDINGZ_API_KEY", "asdf");
        System.setProperty("BINDINGZ_HOSTNAME", "http://somewhere.com");

        File config = createConfigFile("api1", "hostname2");

        Assert.assertEquals("mykey", new ApiCredentialsBuilder(config.toString()).apiKey("mykey").build().getApiKey());
        Assert.assertEquals("myhostname", new ApiCredentialsBuilder(config.toString()).hostname("myhostname").build().getHostname());
    }

    private File createConfigFile(String apiKey, String hostname) throws IOException {
        ApiCredentials credentials = new ApiCredentials(apiKey, hostname);
        File config = Files.createTempFile("ApiCredentialsBuilderTest", "bindingz.io").toFile();
        config.deleteOnExit();
        new ObjectMapper().writeValue(config, credentials);
        return config;
    }
}
