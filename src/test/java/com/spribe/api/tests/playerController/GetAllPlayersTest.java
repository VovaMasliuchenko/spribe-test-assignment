package com.spribe.api.tests.playerController;

import com.spribe.api.clients.PlayerApi;
import com.spribe.api.models.request.CreatePlayerRequest;
import com.spribe.api.models.response.GetAllPlayersResponse;
import com.spribe.config.BaseTest;
import com.spribe.enums.UserRole;
import com.spribe.utils.RandomDataUtils;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Feature("Player controller GET all method")
public class GetAllPlayersTest extends BaseTest {

    @Test(description = "Validate schema of get all players")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetAllPlayersSchema() {
        for (int i = 0; i < 5; i++) {
            String username = RandomDataUtils.generateUsername();
            String password = RandomDataUtils.generatePassword();

            CreatePlayerRequest createPlayerRequest =
                    new CreatePlayerRequest(30, "male", username, password, UserRole.USER.getRole(), username);
            PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);
        }
        Response getAllPlayersResponse = PlayerApi.getAllPlayers();

        getAllPlayersResponse.then().body(matchesJsonSchemaInClasspath("jsonSchemas/getAllPlayersResponse.json"));
    }

    @Test(description = "Validate get all users functionality")
    public void testGetAllPlayers() {
        for (int i = 0; i < 5; i++) {
            String username = RandomDataUtils.generateUsername();
            String password = RandomDataUtils.generatePassword();

            CreatePlayerRequest createPlayerRequest =
                    new CreatePlayerRequest(30, "male", username, password, UserRole.USER.getRole(), username);
            PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);
        }
        Response getAllPlayersResponse = PlayerApi.getAllPlayers();
        List<GetAllPlayersResponse> getAllPlayers = getAllPlayersResponse.jsonPath().getList("players", GetAllPlayersResponse.class);

        Assert.assertEquals(getAllPlayersResponse.getStatusCode(), 200, "Wrong status code, users not found!");
        Assert.assertFalse(getAllPlayers.isEmpty(), "Users not found, users list is empty!");
    }

}
