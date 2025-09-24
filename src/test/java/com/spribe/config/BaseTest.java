package com.spribe.config;

import com.spribe.api.clients.PlayerApi;
import com.spribe.api.models.request.DeletePlayerRequest;
import com.spribe.enums.UserRole;
import com.spribe.service.PlayerService;
import com.spribe.utils.ConfigReader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

import java.util.List;

public class BaseTest {

    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

    private final int retryCountValue = ConfigReader.getIntProperty("retry_count");

    @BeforeSuite
    public void setup() {
        LOGGER.info("Retry count value is set to : {}", retryCountValue);
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = ConfigReader.getStringProperty("base_url");
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterMethod
    public void clearPlayers() {
        List<String> playerIdsToRemove = PlayerService.getPlayerIdsToRemove();
        if (!playerIdsToRemove.isEmpty()) {
            playerIdsToRemove.forEach(player -> PlayerApi.deletePlayerById(UserRole.ADMIN, new DeletePlayerRequest(player)));
            PlayerService.clearList();
        } else {
            LOGGER.info("There are no players to remove");
        }
    }

}
