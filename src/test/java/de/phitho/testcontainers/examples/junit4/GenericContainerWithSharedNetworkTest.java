package de.phitho.testcontainers.examples.junit4;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;

import java.io.File;

public class GenericContainerWithSharedNetworkTest {

    @Rule
    public final BrowserWebDriverContainer chromeContainer = new BrowserWebDriverContainer<>()
                    .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("build"))
                    .withCapabilities(new ChromeOptions())
                    .withNetwork(Network.SHARED);

    @ClassRule
    public static final GenericContainer appToTest = new GenericContainer("solr:latest")
            .withNetwork(Network.SHARED)
            .withNetworkAliases("solr");

    @Test
    public void browse_solr() {
        final WebDriver webDriver = chromeContainer.getWebDriver();

        // use the alias 'solr' as defined for the shared docker network
        // using Network.SHARED both containers communicate within the network
        webDriver.get("http://solr:8983/solr");
    }
}
