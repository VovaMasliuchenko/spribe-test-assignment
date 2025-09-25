package com.spribe.api.tests.playerController;

import com.spribe.api.clients.PlayerApi;
import com.spribe.api.models.request.CreatePlayerRequest;
import com.spribe.api.models.request.GetPlayerByIdRequest;
import com.spribe.config.BaseTest;
import com.spribe.enums.UserRole;
import com.spribe.utils.RandomDataUtils;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Feature("Player controller GET by ID method")
public class GetByIdPlayerTest extends BaseTest {

    @Test(description = "Validate schema of get player by id")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetPlayerByIdSchema() {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest(30, "male", username, password, UserRole.USER.getRole(), username);
        Response createPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);
        Response getPlayerByIdResponse = PlayerApi.getPlayerById(new GetPlayerByIdRequest(String.valueOf(createPlayerResponse.jsonPath().getInt("id"))));

        getPlayerByIdResponse.then().body(matchesJsonSchemaInClasspath("jsonSchemas/PlayerResponse.json"));
    }

    @Test(description = "Validate get user by id functionality")
    public void testGetPlayerById() {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest(30, "male", username, password, UserRole.USER.getRole(), username);
        Response createdPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);

        Response getPlayerResponse = PlayerApi.getPlayerById(new GetPlayerByIdRequest(String.valueOf(createdPlayerResponse.jsonPath().getInt("id"))));
        Assert.assertEquals(getPlayerResponse.getStatusCode(), 200, "Wrong status code, user not found!");
    }

}
