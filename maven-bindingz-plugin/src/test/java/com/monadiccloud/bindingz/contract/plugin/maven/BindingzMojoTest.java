package com.monadiccloud.bindingz.contract.plugin.maven;


import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BindingzMojoTest {
    @Rule
    public MojoRule rule = new MojoRule() {
        @Override
        protected void before() throws Throwable {
        }

        @Override
        protected void after() {
        }
    };

    @Test
    public void testProcessResources() throws Exception {
        File pom = new File( "target/test-classes/project-to-test" );
        assertNotNull( pom );
        assertTrue(pom.exists());

        BindingzMojo myMojo = (BindingzMojo) rule.lookupConfiguredMojo(pom, "processResources");
        assertNotNull(myMojo);
        myMojo.execute();
    }
}

