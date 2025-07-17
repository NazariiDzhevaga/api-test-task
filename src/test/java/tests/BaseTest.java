package tests;

import playerController.PlayerControllerApi;
import playerController.PlayerControllerClient;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import utils.Config;

public class BaseTest {
    protected final PlayerControllerClient playerClient = new PlayerControllerClient();
    protected final PlayerControllerApi apiService = new PlayerControllerApi();
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @BeforeSuite
    public void setup() {
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        Config.initialize();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(ITestResult iTestResult) {
        logger.info("===============TEST STARTED: {}, THREAD: {}===============", iTestResult.getMethod().getDescription(), Thread.currentThread().getName());
    }
}
