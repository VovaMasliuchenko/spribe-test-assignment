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
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("Player Controller")
public class NegativePlayerTest extends BaseTest {

    @DataProvider(name = "outOfRangeAgeDataProvider")
    public Object[][] outOfRangeAgeDataProvider() {
        return new Object[][] {
            { "16" },
            { "60" }
        };
    }

    @DataProvider(name = "testInvalidPasswordDataProvider")
    public Object[][] testInvalidPasswordDataProvider() {
        return new Object[][] {
                { RandomStringUtils.randomAlphanumeric(6) },
                { RandomStringUtils.randomAlphanumeric(16) },
                { RandomStringUtils.randomAlphanumeric(7) + "@" },
                { RandomStringUtils.randomAlphabetic(7) },
                { RandomStringUtils.randomNumeric(7) }
        };
    }

    //Bug №1
    @Test(description = "Validate that user CANNOT be created with GET request", retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateUserWithGetMethod() {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest("20", "male", username, password, "user", username);
        Response createdPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);
        Assert.assertEquals(createdPlayerResponse.getStatusCode(), 405,
                "User was created with GET request. Expected Method Not Allowed (405)");
    }

    //Bug №2
    @Test(description = "Duplicate login should not be allowed", retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    public void testDuplicateLogin() {
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequestFirst =
                new CreatePlayerRequest("30", "male", "duplicateLogin", password, "user", "screen1");
        Response createdFirstPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequestFirst);
        Assert.assertEquals(createdFirstPlayerResponse.getStatusCode(), 200, "User wasn't successfully created");

        CreatePlayerRequest createPlayerRequestSecond =
                new CreatePlayerRequest("30", "male", "duplicateLogin", password, "user", "screen2");
        Response createdSecondPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequestSecond);

        Assert.assertEquals(createdSecondPlayerResponse.getStatusCode(), 400,
                "API allows creating user with duplicate login, overwriting existing user!");
    }

    //Bug №3
    @Test(description = "Duplicate screen name should not be allowed", retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    public void testDuplicateScreenName() {
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequestFirst =
                new CreatePlayerRequest("30", "male", "login1", password, "user", "screenNameDuplicate");
        Response createdFirstPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequestFirst);
        Assert.assertEquals(createdFirstPlayerResponse.getStatusCode(), 200, "User wasn't successfully created");

        CreatePlayerRequest createPlayerRequestSecond =
                new CreatePlayerRequest("30", "male", "Login2", password, "user", "screenNameDuplicate");
        Response createdSecondPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequestSecond);

        Assert.assertEquals(createdSecondPlayerResponse.getStatusCode(), 400,
                "API allows creating user with duplicate screen name!");
    }

    //BUG №4
    @Test(description = "User age out of range should not be created", dataProvider = "outOfRangeAgeDataProvider", retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    public void testUserOutOfRangeAge(String age) {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest(age, "male", username, password, "user", username);
        Response createdPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);

        Assert.assertEquals(createdPlayerResponse.getStatusCode(), 400,
                "Incorrect status code for user age %s, valid range is [17-59]".formatted(age));
    }

    //BUG №5
    @Test(description = "Validate that password matches the requirements", dataProvider = "testInvalidPasswordDataProvider", retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    public void testInvalidPassword(String password) {
        String username = RandomDataUtils.generateUsername();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest("30", "male", username, password, "user", username);
        Response createdPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);

        Assert.assertEquals(createdPlayerResponse.getStatusCode(), 400, "User was created with invalid password!");
    }

    //BUG №6
    @Test(description = "Validate gender can only be male or female", retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    public void testInvalidGender() {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest("30", "other", username, password, "user", username);
        Response createdPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);

        Assert.assertEquals(createdPlayerResponse.getStatusCode(), 400, "Expected error for invalid gender value");
    }

    @Test(description = "Supervisor role should not be allowed at creation", retryAnalyzer = RetryAnalyzer.class)
    public void testSupervisorRoleCreation() {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest("30", "male", username, password, "supervisor", username);
        Response createdPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);

        Assert.assertEquals(createdPlayerResponse.getStatusCode(), 400,
                "Supervisor role must not be creatable via API");
    }

}
