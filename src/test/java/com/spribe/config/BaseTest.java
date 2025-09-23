package com.spribe.config;

import com.spribe.api.clients.PlayerApi;
import com.spribe.api.models.request.DeletePlayerRequest;
import com.spribe.enums.UserRole;
import com.spribe.service.PlayerService;
import com.spribe.utils.ConfigReader;
import io.restassured.RestAssured;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.util.List;

public class BaseTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigReader.getProperty("base_url");
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterMethod
    public void clearPlayers() {
        List<String> playerIdsToRemove = PlayerService.getPlayerIdsToRemove();
        playerIdsToRemove.forEach(player -> PlayerApi.deletePlayerById(UserRole.ADMIN, new DeletePlayerRequest(player)));
        PlayerService.clearList();
    }

}
