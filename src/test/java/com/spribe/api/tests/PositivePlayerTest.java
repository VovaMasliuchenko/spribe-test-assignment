package com.spribe.api.tests;

import com.spribe.api.clients.PlayerApi;
import com.spribe.api.models.request.CreatePlayerRequest;
import com.spribe.config.BaseTest;
import com.spribe.enums.UserRole;
import com.spribe.utils.RandomDataUtils;
import com.spribe.utils.RetryAnalyzer;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("Player Controller")
public class PositivePlayerTest extends BaseTest {

    @DataProvider(name = "validRangeAgeDataProvider")
    public Object[][] validRangeAgeDataProvider() {
        return new Object[][] {
                { "17" },
                { "59" }
        };
    }

    @DataProvider(name = "testValidGenderDataProvider")
    public Object[][] testValidGenderDataProvider() {
        return new Object[][] {
                { "male" },
                { "female" }
        };
    }

    @Test(description = "User with valid age (17-59) should be created successfully",
          dataProvider = "validRangeAgeDataProvider",
          retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    public void testValidAge(String age) {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest(age, "male", username, password, "user", username);
        Response createdPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);

        Assert.assertEquals(createdPlayerResponse.getStatusCode(), 200, "Incorrect status code for create user");
    }

    @Test(description = "Validate player can be created with gender male or female",
          dataProvider = "testValidGenderDataProvider",
          retryAnalyzer = RetryAnalyzer.class)
    public void testValidGender(String gender) {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest("30", gender, username, password, "user", username);
        Response createdPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);
        Assert.assertEquals(createdPlayerResponse.getStatusCode(), 200, "User with valid gender wasn't created!");
    }
}

