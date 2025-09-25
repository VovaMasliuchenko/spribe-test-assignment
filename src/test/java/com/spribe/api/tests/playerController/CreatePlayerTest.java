package com.spribe.api.tests.playerController;

import com.spribe.api.clients.PlayerApi;
import com.spribe.api.models.request.CreatePlayerRequest;
import com.spribe.config.BaseTest;
import com.spribe.enums.UserRole;
import com.spribe.utils.RandomDataUtils;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.spribe.utils.Constants.JsonSchemasPath.PLAYER_SCHEMA;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Feature("Player controller GET create method")
public class CreatePlayerTest extends BaseTest {

    @DataProvider(name = "validRangeAgeDataProvider")
    public Object[][] validRangeAgeDataProvider() {
        return new Object[][] {
                { 17 },
                { 59 }
        };
    }

    @DataProvider(name = "outOfRangeAgeDataProvider")
    public Object[][] outOfRangeAgeDataProvider() {
        return new Object[][] {
                { 16 },
                { 60 }
        };
    }

    @DataProvider(name = "testValidGenderDataProvider")
    public Object[][] testValidGenderDataProvider() {
        return new Object[][] {
                { "male" },
                { "female" }
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

    @DataProvider(name = "testCreatePLayerWithAllowedRolesDataProvider")
    public Object[][] testCreatePLayerWithAllowedRolesDataProvider() {
        return new Object[][] {
                { UserRole.ADMIN },
                { UserRole.SUPERVISOR }
        };
    }

    @Test(description = "Validate that user CANNOT be created with GET request")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("BUG-1")
    public void testCreatePlayerWithGetMethod() {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest(20, "male", username, password, UserRole.USER.getRole(), username);
        Response createdPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);
        Assert.assertEquals(createdPlayerResponse.getStatusCode(), 405,
                "User was created with GET request. Expected Method Not Allowed (405)");
    }

    @Test(description = "Duplicate login should not be allowed")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("BUG-2")
    public void testDuplicateLogin() {
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequestFirst =
                new CreatePlayerRequest(30, "male", "duplicateLogin", password, UserRole.USER.getRole(), "screen1");
        Response createdFirstPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequestFirst);
        Assert.assertEquals(createdFirstPlayerResponse.getStatusCode(), 200, "User wasn't successfully created");

        CreatePlayerRequest createPlayerRequestSecond =
                new CreatePlayerRequest(30, "male", "duplicateLogin", password, UserRole.USER.getRole(), "screen2");
        Response createdSecondPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequestSecond);

        Assert.assertEquals(createdSecondPlayerResponse.getStatusCode(), 400,
                "API allows creating user with duplicate login, overwriting existing user!");
    }

    @Test(description = "Duplicate screen name should not be allowed")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("BUG-3")
    public void testDuplicateScreenName() {
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequestFirst =
                new CreatePlayerRequest(30, "male", "login1", password, UserRole.USER.getRole(), "screenNameDuplicate");
        Response createdFirstPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequestFirst);
        Assert.assertEquals(createdFirstPlayerResponse.getStatusCode(), 200, "User wasn't successfully created");

        CreatePlayerRequest createPlayerRequestSecond =
                new CreatePlayerRequest(30, "male", "Login2", password, UserRole.USER.getRole(), "screenNameDuplicate");
        Response createdSecondPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequestSecond);

        Assert.assertEquals(createdSecondPlayerResponse.getStatusCode(), 400,
                "API allows creating user with duplicate screen name!");
    }

    @Test(description = "Player age out of range should not be created", dataProvider = "outOfRangeAgeDataProvider")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("BUG-4")
    public void testPlayerOutOfRangeAge(int age) {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest(age, "male", username, password, UserRole.USER.getRole(), username);
        Response createdPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);

        Assert.assertEquals(createdPlayerResponse.getStatusCode(), 400,
                "Incorrect status code for user age %s, valid range is [17-59]".formatted(age));
    }

    @Test(description = "Validate that password matches the requirements", dataProvider = "testInvalidPasswordDataProvider")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("BUG-5")
    public void testInvalidPassword(String password) {
        String username = RandomDataUtils.generateUsername();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest(30, "male", username, password, UserRole.USER.getRole(), username);
        Response createdPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);

        Assert.assertEquals(createdPlayerResponse.getStatusCode(), 400, "User was created with invalid password!");
    }

    @Test(description = "Validate gender can only be male or female")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("BUG-6")
    public void testInvalidGender() {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest(30, "other", username, password, UserRole.USER.getRole(), username);
        Response createdPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);

        Assert.assertEquals(createdPlayerResponse.getStatusCode(), 400, "Expected error for invalid gender value");
    }

    @Test(description = "Validate schema of player creation")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("BUG-7")
    public void testCreatePlayerSchema() {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest(30, "male", username, password, UserRole.USER.getRole(), username);
        Response createdPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);
        createdPlayerResponse.then().body(matchesJsonSchemaInClasspath(PLAYER_SCHEMA));
    }

    @Test(description = "User with valid age (17-59) should be created successfully", dataProvider = "validRangeAgeDataProvider")
    @Severity(SeverityLevel.CRITICAL)
    public void testValidAge(int age) {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest(age, "male", username, password, UserRole.USER.getRole(), username);
        Response createdPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);

        Assert.assertEquals(createdPlayerResponse.getStatusCode(), 200, "Incorrect status code for create user");
    }

    @Test(description = "Validate player can be created with gender male or female", dataProvider = "testValidGenderDataProvider")
    public void testValidGender(String gender) {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest(30, gender, username, password, UserRole.USER.getRole(), username);
        Response createdPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);

        Assert.assertEquals(createdPlayerResponse.getStatusCode(), 200, "User with valid gender wasn't created!");
    }

    @Test(description = "Supervisor role should not be allowed at creation")
    public void testSupervisorRoleCreation() {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest(30, "male", username, password, UserRole.SUPERVISOR.getRole(), username);
        Response createdPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);

        Assert.assertEquals(createdPlayerResponse.getStatusCode(), 400,
                "Supervisor role must not be creatable via API");
    }

    @Test(description = "Validate that players with allowed roles can create users", dataProvider = "testCreatePLayerWithAllowedRolesDataProvider")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("BUG-10")
    public void testCreatePLayerWithAllowedRoles(UserRole userRole) {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest(20, "male", username, password, UserRole.USER.getRole(), username);
        Response createdPlayerResponse = PlayerApi.createPlayer(userRole, createPlayerRequest);

        Assert.assertEquals(createdPlayerResponse.getStatusCode(), 200,
                "User wasn't created with allowed role %s".formatted(userRole.getRole()));
    }

    @Test(description = "Validate that player with not allowed role CANNOT create users")
    public void testCreatePLayerWithNotAllowedRole() {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest(20, "male", username, password, UserRole.USER.getRole(), username);
        Response createdPlayerResponse = PlayerApi.createPlayer(UserRole.USER, createPlayerRequest);

        Assert.assertEquals(createdPlayerResponse.getStatusCode(), 403,
                "User created with not allowed role %s".formatted(UserRole.USER.getRole()));
    }

}
