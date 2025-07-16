package tests;


import enums.GenderEnum;
import enums.RoleEnum;
import io.restassured.response.Response;
import model.request.CreatePlayerRequest;
import model.response.CreatePlayerResponse;
import model.response.GetAllPlayerResponse;
import model.response.GetPlayerByIdResponse;
import org.testng.annotations.*;
import playerControllerUtils.RandomPlayerBuilder;

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

    @Test(description = "Verify that player creation fails when the provided age is below 16 years",
            groups = "delete-created-player")
    public void verifyPlayerCreationFailsForUnderageUser() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData()
                .build();
        playerToCreate.setAge(15);

        Response playerWithUnderAge = apiService.createPlayer(playerToCreate);

        assertThat(playerWithUnderAge.getStatusCode())
                .as("After creating a player with age less than 16 years there should be 400 status code")
                .isEqualTo(400);
    }

    @Test(description = "Verify that player creation fails when the provided age is above 60 years",
            groups = "delete-created-player")
    public void verifyPlayerCreationFailsForOverageUser() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData()
                .build();
        playerToCreate.setAge(61);

        Response playerWithUnderAge = apiService.createPlayer(playerToCreate);

        assertThat(playerWithUnderAge.getStatusCode())
                .as("After creating a player with age more than 60 years there should be correct status code")
                .isEqualTo(400);
    }

    @Test(description = "Verify that player creation fails when the provided gender is invalid",
            groups = "delete-created-player")
    public void verifyPlayerCreationFailsForInvalidGender() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData()
                .build();
        playerToCreate.setGender(GenderEnum.INVALID_GENDER);

        Response playerWithNotAllowedGender = apiService.createPlayer(playerToCreate);

        assertThat(playerWithNotAllowedGender.getStatusCode())
                .as(String.format("After creating a player with not allowed gender ('%s') there should be correct status code", GenderEnum.INVALID_GENDER))
                .isNotIn(200, 201, 204);
    }

    @Test(description = "Verify that player creation fails when the provided password is less than 7 symbols",
            groups = "delete-created-player")
    public void verifyPlayerCreationFailsForShortPassword() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData().build();
        String shortPassword = "123";
        playerToCreate.setPassword("123");

        Response playerWithNotAllowedGender = apiService.createPlayer(playerToCreate);

        assertThat(playerWithNotAllowedGender.getStatusCode())
                .as(String.format("After creating a player with password less than 7 symbols (%s) there should correct status code", shortPassword))
                .isNotIn(200, 201, 204);
    }

    @Test(description = "Verify that player creation fails when the provided password is more than 15 symbols",
            groups = "delete-created-player")
    public void verifyPlayerCreationFailsForLongPassword() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData()
                .build();
        String longPassword = "1234567891012131";
        playerToCreate.setPassword(longPassword);

        Response playerWithNotAllowedGender = apiService.createPlayer(playerToCreate);

        assertThat(playerWithNotAllowedGender.getStatusCode())
                .as(String.format("After creating a player with password more than 15 symbols (%s) there should correct status code", longPassword))
                .isNotIn(200, 201, 204);
    }

    @Test(description = "Verify that player creation fails when the provided password contains special symbols",
            groups = "delete-created-player")
    public void verifyPlayerCreationFailsForPasswordWithSpecialSymbols() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData()
                .build();
        String invalidPassword = "@#$%^ ";
        playerToCreate.setPassword(invalidPassword);

        Response playerWithNotAllowedGender = apiService.createPlayer(playerToCreate);

        assertThat(playerWithNotAllowedGender.getStatusCode())
                .as(String.format("After creating a player with password with special symbols ('%s') there should correct status code", invalidPassword))
                .isNotIn(200, 201, 204);
    }

    @Test(description = "Verify that 'user' role cannot create new player",
            groups = "delete-created-player")
    public void verifyPlayerCreationFailsDoneByUserRole() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData()
                .build();
        playerToCreate.setEditor(RoleEnum.USER);

        Response playerWithNotAllowedGender = apiService.createPlayer(playerToCreate);

        assertThat(playerWithNotAllowedGender.getStatusCode())
                .as("Role 'user' should not be able to create new player")
                .isEqualTo(403);
    }

    @Test(description = "Verify that player creation fails when the provided role is 'supervisor'",
            groups = "delete-created-player")
    public void verifyPlayerCreationFailsForSupervisorRole() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData()
                .build();
        playerToCreate.setRole(RoleEnum.SUPERVISOR);

        Response playerWithNotAllowedGender = apiService.createPlayer(playerToCreate);

        assertThat(playerWithNotAllowedGender.getStatusCode())
                .as("After creating a player with role 'supervisor' there should be correct status code")
                .isEqualTo(400);
    }

    @Test(description = "Verify that a role 'user' should not be  able to delete its user")
    public void verifyPlayerDeletionNotAllowedForRoleUserForItsUser() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData().build();
        CreatePlayerResponse createdPlayer = playerClient.createPlayer(playerToCreate);

        playerClient.deletePlayer(createdPlayer.getId(), RoleEnum.USER);
        GetPlayerByIdResponse player = playerClient.getPlayer(createdPlayer.getId());

        assertThat(player)
                .as("Role model 'user' should not be able to delete its user")
                .isNotNull();
    }

    @Test(description = "Verify that getting a player with invalid id fails")
    public void verifyGetPlayerFailsWithInvalidId() {
        Random random = new Random();
        GetPlayerByIdResponse player = playerClient.getPlayer(random.nextInt(100));
        assertThat(player)
                .as("Getting a player with non valid id should return null")
                .isNull();
    }

    @Test(description = "Verify that updating a player fails when performed by a role without proper permissions")
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