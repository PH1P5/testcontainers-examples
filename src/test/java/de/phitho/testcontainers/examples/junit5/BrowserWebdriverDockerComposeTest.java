package de.phitho.testcontainers.examples.junit5;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.TestDescription;

import java.io.File;
import java.util.Optional;

@Testcontainers
public class BrowserWebdriverDockerComposeTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrowserWebdriverDockerComposeTest.class);
    private static final int CHRONOGRAF_PORT = 8888;

    @Container
    public final BrowserWebDriverContainer chromeContainer =
            new BrowserWebDriverContainer()
                    .withCapabilities(new ChromeOptions())
                    .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("build"));

    @Container
    public static DockerComposeContainer chronograf =
            new DockerComposeContainer(new File("docker-compose.yml"))
                    .withExposedService("chronograf", CHRONOGRAF_PORT) // implicitly waits
                    .withLocalCompose(true)
                    .withLogConsumer("influxdb", new Slf4jLogConsumer(LOGGER)); // check the logs if influx is talking

    private WebDriver chromeDriver;

    @BeforeAll
    public static void beforeAll() {
        org.testcontainers.Testcontainers.exposeHostPorts(CHRONOGRAF_PORT);
    }

    @Test
    void browse_chronograph() {
        chromeDriver = chromeContainer.getWebDriver();
        chromeDriver.get("http://host.testcontainers.internal:"+CHRONOGRAF_PORT);
    }

/*
    VNC recording is broken in JUnit 5, see https://github.com/testcontainers/testcontainers-java/issues/1341

    This is a workaround to enable VNC recording.
 */
//    @AfterEach
//    void tearDown() {
//        chromeContainer.afterTest(new TestDescription() {
//
//            @Override
//            public String getTestId() {
//                return "test_id";
//            }
//
//            @Override
//            public String getFilesystemFriendlyName() {
//                return "FileName";
//            }
//        }, Optional.empty());
//    }
}
