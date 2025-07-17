package tests;

import enums.GenderEnum;
import enums.RoleEnum;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import models.request.CreatePlayerRequest;
import models.response.CreatePlayerResponse;
import models.response.GetAllPlayerResponse;
import models.response.GetPlayerByIdResponse;
import org.testng.annotations.*;
import playerController.RandomPlayerBuilder;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerControllerNegativeTests extends BaseTest {
    private static List<GetAllPlayerResponse.PlayerResponse> allPlayerBeforeTests;

    @BeforeClass
    public void getAllPlayers() {
        allPlayerBeforeTests = playerClient.getAllPlayer().getPlayers();
    }

    @AfterSuite
    public void deleteCreatedPlayers() {
        List<GetAllPlayerResponse.PlayerResponse> allPlayerAfterTests = playerClient.getAllPlayer().getPlayers();
        allPlayerAfterTests.removeAll(allPlayerBeforeTests);
        allPlayerAfterTests.forEach(e -> apiService.deletePlayer(e.getId(), RoleEnum.SUPERVISOR));
    }

    @Epic("Player Controller")
    @Test(description = "Verify that player creation fails when the provided data is empty")
    public void verifyPlayerCreationFailsWithEmptyRequest() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withEmptyData().build();

        Response playerWithEmptyData = apiService.createPlayer(playerToCreate);

        assertThat(playerWithEmptyData.getStatusCode())
                .as("After creating a player with empty data there should be correct status code")
                .isEqualTo(404);
    }

    @Epic("Player Controller")
    @Test(description = "Verify that player creation fails when the provided age is below 16 years")
    public void verifyPlayerCreationFailsForUnderageUser() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData().build();
        playerToCreate.setAge(15);

        Response playerWithUnderAge = apiService.createPlayer(playerToCreate);

        assertThat(playerWithUnderAge.getStatusCode())
                .as("After creating a player with age less than 16 years there should be 400 status code")
                .isEqualTo(400);
    }

    @Epic("Player Controller")
    @Test(description = "Verify that player creation fails when the provided age is above 60 years")
    public void verifyPlayerCreationFailsForOverageUser() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData().build();
        playerToCreate.setAge(61);

        Response playerWithOverAge = apiService.createPlayer(playerToCreate);

        assertThat(playerWithOverAge.getStatusCode())
                .as("After creating a player with age more than 60 years there should be correct status code")
                .isEqualTo(400);
    }

    @Epic("Player Controller")
    @Test(description = "Verify that player creation fails when the provided gender is invalid")
    public void verifyPlayerCreationFailsForInvalidGender() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData().build();
        playerToCreate.setGender(GenderEnum.INVALID_GENDER);

        Response playerWithNotAllowedGender = apiService.createPlayer(playerToCreate);

        assertThat(playerWithNotAllowedGender.getStatusCode())
                .as(String.format("After creating a player with not allowed gender ('%s') there should be correct status code", GenderEnum.INVALID_GENDER.getName()))
                .isNotIn(200, 201, 204);
    }

    @Epic("Player Controller")
    @Test(description = "Verify that player creation fails when the provided password is less than 7 symbols")
    public void verifyPlayerCreationFailsForShortPassword() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData().build();
        String shortPassword = "123";
        playerToCreate.setPassword(shortPassword);

        Response playerWithShortPassword = apiService.createPlayer(playerToCreate);

        assertThat(playerWithShortPassword.getStatusCode())
                .as(String.format("After creating a player with password less than 7 symbols (%s) there should correct status code", shortPassword))
                .isNotIn(200, 201, 204);
    }

    @Epic("Player Controller")
    @Test(description = "Verify that player creation fails when the provided password is more than 15 symbols")
    public void verifyPlayerCreationFailsForLongPassword() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData().build();
        String longPassword = "1234567891012131";
        playerToCreate.setPassword(longPassword);

        Response playerWithLongPassword = apiService.createPlayer(playerToCreate);

        assertThat(playerWithLongPassword.getStatusCode())
                .as(String.format("After creating a player with password more than 15 symbols (%s) there should correct status code", longPassword))
                .isNotIn(200, 201, 204);
    }

    @Epic("Player Controller")
    @Test(description = "Verify that player creation fails when the provided password contains special symbols")
    public void verifyPlayerCreationFailsForPasswordWithSpecialSymbols() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData().build();
        String invalidPassword = "@#$%^ ";
        playerToCreate.setPassword(invalidPassword);

        Response playerWithSpecialSymbolsPassword = apiService.createPlayer(playerToCreate);

        assertThat(playerWithSpecialSymbolsPassword.getStatusCode())
                .as(String.format("After creating a player with a password with special symbols ('%s') there should correct status code", invalidPassword))
                .isNotIn(200, 201, 204);
    }

    @Epic("Player Controller")
    @Test(description = "Verify that 'user' editor cannot create new player")
    public void verifyPlayerCreationFailsDoneByUserEditor() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData().build();
        playerToCreate.setEditor(RoleEnum.USER);

        Response playerWithUserEditor = apiService.createPlayer(playerToCreate);

        assertThat(playerWithUserEditor.getStatusCode())
                .as("Editor 'user' should not be able to create new player")
                .isEqualTo(403);
    }

    @Epic("Player Controller")
    @Test(description = "Verify that player creation fails when the provided role is 'supervisor'")
    public void verifyPlayerCreationFailsForSupervisorRole() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData().build();
        playerToCreate.setRole(RoleEnum.SUPERVISOR);

        Response playerWithSupervisorRole = apiService.createPlayer(playerToCreate);

        assertThat(playerWithSupervisorRole.getStatusCode())
                .as("After creating a player with role 'supervisor' there should be correct status code")
                .isEqualTo(400);
    }

    @Epic("Player Controller")
    @Test(description = "Verify that a role 'user' should not be able to delete itself")
    public void verifyPlayerDeletionNotAllowedWhenUserDeleteItself() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData().build();
        CreatePlayerResponse createdPlayer = playerClient.createPlayer(playerToCreate);

        playerClient.deletePlayer(createdPlayer.getId(), RoleEnum.USER);
        GetPlayerByIdResponse player = playerClient.getPlayer(createdPlayer.getId());

        assertThat(player)
                .as("Role 'user' should not be able to delete itself")
                .isNotNull();
    }

    @Epic("Player Controller")
    @Test(description = "Verify that getting a player with invalid id fails")
    public void verifyGetPlayerFailsWithInvalidId() {
        Random random = new Random();
        GetPlayerByIdResponse playerWithInvalidId = playerClient.getPlayer(random.nextInt(100));

        assertThat(playerWithInvalidId)
                .as("Getting a player with non valid id should return null")
                .isNull();
    }

    @Epic("Player Controller")
    @Test(description = "Verify that updating a player fails when performed by a role without proper permissions ('user')")
    public void verifyUpdatePlayerFailsForRoleWithoutPermissions() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData().build();
        CreatePlayerResponse createdPlayer = playerClient.createPlayer(playerToCreate);

        CreatePlayerRequest playerToUpdate = new CreatePlayerRequest(playerToCreate);
        playerToUpdate.setGender(GenderEnum.FEMALE);
        playerToUpdate.setAge(25);

        Response updatedPlayer = apiService.updatePlayer(RoleEnum.USER, createdPlayer.getId(), playerToUpdate);

        assertThat(updatedPlayer.getStatusCode())
                .as("There should be 403 status code when a role 'user' try to update any user")
                .isEqualTo(403);
    }

    @Epic("Player Controller")
    @Epic("Player Controller")
    @Test(description = "Verify sending a POST request to '/player/get/all/' fails")
    public void verifyGetAllPlayersFailsWithPostHttpMethod() {
        Response allPlayerWithPost = apiService.getAllPlayerWithPost();

        assertThat(allPlayerWithPost.getStatusCode())
                .as("After trying to get all players with post method 405 error should returns")
                .isEqualTo(405);

        assertThat(allPlayerWithPost.getBody().asString())
                .as("After trying to get all players with post method there should be correct error message")
                .contains("Method Not Allowed");
    }
}