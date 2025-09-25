package com.spribe.api.tests.playerController;

import com.spribe.api.clients.PlayerApi;
import com.spribe.api.models.request.CreatePlayerRequest;
import com.spribe.api.models.request.GetPlayerByIdRequest;
import com.spribe.api.models.request.UpdatePlayerRequest;
import com.spribe.config.BaseTest;
import com.spribe.enums.UserRole;
import com.spribe.utils.RandomDataUtils;
import com.spribe.utils.RetryAnalyzer;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Feature("Player controller UPDATE method")
public class UpdatePlayerTest extends BaseTest {

    @DataProvider(name = "testUpdatePlayerWrongAgeDataProvider")
    public Object[][] testUpdatePlayerWrongAgeDataProvider() {
        return new Object[][] {
                { 16 },
                { 60 }
        };
    }

    @Test(description = "Validate schema for update player")
    public void testUpdatePlayerSchema() {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        String newUsername = RandomDataUtils.generateUsername();
        UpdatePlayerRequest updatePlayerRequest = new UpdatePlayerRequest();
        updatePlayerRequest.setLogin(newUsername);

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest(30, "male", username, password, UserRole.USER.getRole(), username);
        Response createPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);

        int playerId = createPlayerResponse.jsonPath().getInt("id");
        Response updatePlayerResponse = PlayerApi.updatePlayerById(UserRole.SUPERVISOR, playerId, updatePlayerRequest);

        updatePlayerResponse.then().body(matchesJsonSchemaInClasspath("jsonSchemas/PlayerResponse.json"));
    }

    @Test(description = "Validate update player age to not permitted value", dataProvider = "testUpdatePlayerWrongAgeDataProvider")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("BUG-9")
    public void testUpdatePlayerWrongAge(int age) {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        UpdatePlayerRequest updatePlayerRequest = new UpdatePlayerRequest();
        updatePlayerRequest.setAge(age);

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest(30, "male", username, password, UserRole.USER.getRole(), username);
        Response createPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);

        int playerId = createPlayerResponse.jsonPath().getInt("id");
        Response updatePlayerResponse = PlayerApi.updatePlayerById(UserRole.SUPERVISOR, playerId, updatePlayerRequest);

        Assert.assertEquals(updatePlayerResponse.statusCode(), 400,
                "Wrong status code for update, player age updated with not permitted value");
    }

    @Test(description = "Validate update player login to duplicate value")
    public void testUpdatePlayerDuplicateLogin() {
        SoftAssert softAssert = new SoftAssert();
        List<Response> createPlayerResponseList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            String username = RandomDataUtils.generateUsername();
            String password = RandomDataUtils.generatePassword();
            CreatePlayerRequest createPlayerRequest =
                    new CreatePlayerRequest(30, "male", username, password, UserRole.USER.getRole(), username);
            createPlayerResponseList.add(PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest));
        }

        Response firstPlayerResponse = createPlayerResponseList.get(0);
        Response secondPlayerResponse = createPlayerResponseList.get(1);

        UpdatePlayerRequest updatePlayerRequest = new UpdatePlayerRequest();
        updatePlayerRequest.setLogin(firstPlayerResponse.jsonPath().getString("login"));

        int secondPlayerId = secondPlayerResponse.jsonPath().getInt("id");
        Response updateSecondPlayerResponse = PlayerApi.updatePlayerById(UserRole.SUPERVISOR, secondPlayerId, updatePlayerRequest);
        Response getSecondPlayerResponse = PlayerApi.getPlayerById(new GetPlayerByIdRequest(secondPlayerId));

        softAssert.assertEquals(updateSecondPlayerResponse.statusCode(), 409,
                "Wrong status code for update, player login updated with duplicate value");
        softAssert.assertEquals(
                getSecondPlayerResponse.jsonPath().getString("login"),
                secondPlayerResponse.jsonPath().getString("login"),
                "Login is changed to duplicate value!"
        );
        softAssert.assertAll();
    }



}
