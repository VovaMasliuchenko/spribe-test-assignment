package com.spribe.api.tests.playerController;

import com.spribe.api.clients.PlayerApi;
import com.spribe.api.models.request.CreatePlayerRequest;
import com.spribe.api.models.request.DeletePlayerRequest;
import com.spribe.config.BaseTest;
import com.spribe.enums.UserRole;
import com.spribe.utils.RandomDataUtils;
import com.spribe.utils.RetryAnalyzer;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Feature("Player controller DELETE method")
public class DeletePlayerTest extends BaseTest {

    @Test(description = "Validate delete player functionality")
    public void testDeletePlayer() {
        String username = RandomDataUtils.generateUsername();
        String password = RandomDataUtils.generatePassword();

        CreatePlayerRequest createPlayerRequest =
                new CreatePlayerRequest(30, "male", username, password, UserRole.USER.getRole(), username);
        Response createdPlayerResponse = PlayerApi.createPlayer(UserRole.SUPERVISOR, createPlayerRequest);
        int createdPlayerId = createdPlayerResponse.jsonPath().getInt("id");

        Response deletePlayerResponse = PlayerApi.deletePlayerById(UserRole.SUPERVISOR,
                new DeletePlayerRequest(String.valueOf(createdPlayerId)));
        List<Integer> playerIds = PlayerApi.getAllPlayers().jsonPath().getList("players.id");

        Assert.assertEquals(deletePlayerResponse.getStatusCode(), 204, "Wrong status code, player wasn't deleted!");
        Assert.assertFalse(playerIds.contains(createdPlayerId), "Deleted player ID is still present in the list!");
    }

    @Test(description = "Validate that cannot delete not found player functionality")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("BUG-8")
    public void testNotFoundDeletePlayer() {
        String playerId = RandomStringUtils.randomNumeric(8);

        Response deletePlayerResponse = PlayerApi.deletePlayerById(UserRole.SUPERVISOR,
                new DeletePlayerRequest(playerId));

        Assert.assertEquals(deletePlayerResponse.getStatusCode(), 404, "Wrong status code, should be (404 Not Found)");
    }

}
