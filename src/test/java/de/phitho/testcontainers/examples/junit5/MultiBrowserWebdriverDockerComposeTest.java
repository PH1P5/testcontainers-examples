package de.phitho.testcontainers.examples.junit5;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Testcontainers
public class MultiBrowserWebdriverDockerComposeTest {

    private static final int CHRONOGRAF_PORT = 8888;
    private static final int SELENIUM_HUB_PORT = 4444;

    @Container
    public static DockerComposeContainer chronograf =new DockerComposeContainer(
                    new File("selenium-grid-compose.yml"),
                    new File("docker-compose.yml"))
                    .withExposedService("chronograf", CHRONOGRAF_PORT, Wait.forHttp("/"))
                    .withExposedService("hub", SELENIUM_HUB_PORT)
                    .withLocalCompose(true);


    private static Stream<WebDriver> testDrivers() {
        return Stream.of(chrome, firefox);
    }

    private static WebDriver chrome;
    private static WebDriver firefox;

    @BeforeAll
    static void setUp() throws MalformedURLException {
        chrome = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.chrome());
        chrome.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        firefox = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.firefox());
        firefox.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @ParameterizedTest
    @MethodSource("testDrivers")
    void browse_chronograf_with_multiple_driver(WebDriver driver) {
        driver.get("http://host.docker.internal:"+CHRONOGRAF_PORT);
    }
}
