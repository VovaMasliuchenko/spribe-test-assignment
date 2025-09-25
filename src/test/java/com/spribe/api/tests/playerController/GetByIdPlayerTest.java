package com.spribe.api.tests.playerController;

import com.spribe.api.clients.PlayerApi;
import com.spribe.api.models.request.CreatePlayerRequest;
import com.spribe.api.models.request.GetPlayerByIdRequest;
import com.spribe.api.models.response.PlayerResponse;
import com.spribe.config.BaseTest;
import com.spribe.enums.UserRole;
import com.spribe.utils.RandomDataUtils;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.spribe.utils.Constants.JsonSchemasPath.PLAYER_SCHEMA;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Feature("Player controller GET by ID method")
public class GetByIdPlayerTest extends BaseTest {

    @Test(description = "Validate schema for get player by id")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetPlayerByIdSchema() {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest(30, "male", username, password, UserRole.USER.getRole(), username);
        Response createPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);
        Response getPlayerByIdResponse = PlayerApi.getPlayerById(new GetPlayerByIdRequest(String.valueOf(createPlayerResponse.jsonPath().getInt("id"))));

        getPlayerByIdResponse.then().body(matchesJsonSchemaInClasspath(PLAYER_SCHEMA));
    }

    @Test(description = "Validate get user by id functionality")
    public void testGetPlayerById() {
        SoftAssert softAssert = new SoftAssert();
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest(30, "male", username, password, UserRole.USER.getRole(), username);
        Response createdPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);

        Response getPlayerResponse = PlayerApi.getPlayerById(new GetPlayerByIdRequest(String.valueOf(createdPlayerResponse.jsonPath().getInt("id"))));
        PlayerResponse playerResponse = getPlayerResponse.jsonPath().getObject("", PlayerResponse.class);

        Assert.assertEquals(getPlayerResponse.getStatusCode(), 200, "Wrong status code, user not found!");

        softAssert.assertEquals(playerResponse.getAge(), createPlayerRequest.getAge(), "Age field is incorrect");
        softAssert.assertEquals(playerResponse.getGender(), createPlayerRequest.getGender(), "Gender field is incorrect");
        softAssert.assertEquals(playerResponse.getRole(), createPlayerRequest.getRole(), "Role field is incorrect");
        softAssert.assertEquals(playerResponse.getPassword(), createPlayerRequest.getPassword(), "Age field is incorrect");
        softAssert.assertEquals(playerResponse.getLogin(), createPlayerRequest.getLogin(), "Login field is incorrect");
        softAssert.assertEquals(playerResponse.getScreenName(), createPlayerRequest.getScreenName(), "Screen name field is incorrect");
        softAssert.assertAll();
    }

}
