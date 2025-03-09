package com.digital;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.digital.steps",
    plugin = {
        "pretty",
        "html:target/cucumber-reports.html",
        "json:target/cucumber-reports.json",
        "junit:target/cucumber-reports.xml",
        "rerun:target/rerun.txt"
    },
    tags = "not @ignore"
)
public class CucumberTestRunner {
    
    static {
        java.util.logging.Logger.getLogger("org.hibernate.SQL").setLevel(java.util.logging.Level.INFO);
        java.util.logging.Logger.getLogger("org.hibernate.type.descriptor.sql").setLevel(java.util.logging.Level.INFO);
        
    }
} 