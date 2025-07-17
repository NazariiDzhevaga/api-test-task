package tests;

import enums.RoleEnum;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import models.request.CreatePlayerRequest;
import models.response.*;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.*;
import playerController.RandomPlayerBuilder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerControllerPositiveTests extends BaseTest {
    private static List<GetAllPlayerResponse.PlayerResponse> allPlayerBeforeTests;

    @BeforeSuite
    public void getAllPlayers() {
        allPlayerBeforeTests = playerClient.getAllPlayer().getPlayers();
    }

    @AfterSuite
    public void deleteCreatedPlayers() {
        List<GetAllPlayerResponse.PlayerResponse> allPlayerAfterTests = playerClient.getAllPlayer().getPlayers();
        allPlayerAfterTests.removeAll(allPlayerBeforeTests);
        allPlayerAfterTests.forEach(pl -> apiService.deletePlayer(pl.getId(), RoleEnum.SUPERVISOR));
    }

    @Epic("Player Controller")
    @Test(description = "Verify that a new player is created successfully by role 'supervisor'",
            groups = "delete-created-player")
    public void verifyPlayerIsCreatedSuccessfullyBySupervisor() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData().build();
        Response createdPlayerResponse = apiService.createPlayer(playerToCreate);

        assertThat(createdPlayerResponse.getStatusCode())
                .as("After creating a user by 'supervisor' role there should be correct status code")
                .isIn(200, 201, 204);
        CreatePlayerResponse createdPlayer = playerClient.createPlayer(playerToCreate);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(createdPlayer.getLogin())
                .as("New created user response body has not expected login")
                .isEqualTo(playerToCreate.getLogin());
        softAssertions.assertThat(createdPlayer.getPassword())
                .as("New created user response body has not expected password")
                .isEqualTo(playerToCreate.getPassword());
        softAssertions.assertThat(createdPlayer.getAge())
                .as("New created user response body has not expected age")
                .isEqualTo(playerToCreate.getAge());
        softAssertions.assertThat(createdPlayer.getRole())
                .as("New created user response body has not expected role")
                .isEqualTo(playerToCreate.getRole().getName());
        softAssertions.assertThat(createdPlayer.getScreenName())
                .as("New created user response body has not expected screen name")
                .isEqualTo(playerToCreate.getScreenName());
        softAssertions.assertThat(createdPlayer.getGender())
                .as("New created user response body has not expected gender")
                .isEqualTo(playerToCreate.getGender().getName());
        softAssertions.assertAll();
    }

    @Epic("Player Controller")
    @Test(description = "Verify that a new player is created successfully without 'password'",
            groups = "delete-created-player")
    public void verifyPlayerIsCreatedSuccessfullyWithoutPassword() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData().build();
        playerToCreate.setPassword("");
        Response createdPlayerResponse = apiService.createPlayer(playerToCreate);

        assertThat(createdPlayerResponse.getStatusCode())
                .as("After creating a user without 'password' there should be correct status code")
                .isIn(200, 201, 204);
        CreatePlayerResponse createdPlayer = playerClient.createPlayer(playerToCreate);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(createdPlayer.getLogin())
                .as("New created user response body has not expected login")
                .isEqualTo(playerToCreate.getLogin());
        softAssertions.assertThat(createdPlayer.getPassword())
                .as("New created user response body has not expected password")
                .isEqualTo(playerToCreate.getPassword());
        softAssertions.assertThat(createdPlayer.getAge())
                .as("New created user response body has not expected age")
                .isEqualTo(playerToCreate.getAge());
        softAssertions.assertThat(createdPlayer.getRole())
                .as("New created user response body has not expected role")
                .isEqualTo(playerToCreate.getRole().getName());
        softAssertions.assertThat(createdPlayer.getScreenName())
                .as("New created user response body has not expected screen name")
                .isEqualTo(playerToCreate.getScreenName());
        softAssertions.assertThat(createdPlayer.getGender())
                .as("New created user response body has not expected gender")
                .isEqualTo(playerToCreate.getGender().getName());
        softAssertions.assertAll();
    }

    @Epic("Player Controller")
    @Test(description = "Verify that a new player is created successfully by role 'admin'",
            groups = "delete-created-player")
    public void verifyPlayerIsCreatedSuccessfullyByAdmin() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData().build();
        playerToCreate.setEditor(RoleEnum.ADMIN);
        Response createdPlayerResponse = apiService.createPlayer(playerToCreate);

        assertThat(createdPlayerResponse.getStatusCode())
                .as("After creating a user by 'admin' role there should be correct status code")
                .isIn(200, 201, 204);

        CreatePlayerResponse createdPlayer = createdPlayerResponse.as(CreatePlayerResponse.class);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(createdPlayer.getLogin())
                .as("New created user response body has not expected login")
                .isEqualTo(playerToCreate.getLogin());
        softAssertions.assertThat(createdPlayer.getPassword())
                .as("New created user response body has not expected password")
                .isEqualTo(playerToCreate.getPassword());
        softAssertions.assertThat(createdPlayer.getAge())
                .as("New created user response body has not expected age")
                .isEqualTo(playerToCreate.getAge());
        softAssertions.assertThat(createdPlayer.getRole())
                .as("New created user response body has not expected role")
                .isEqualTo(playerToCreate.getRole().getName());
        softAssertions.assertThat(createdPlayer.getScreenName())
                .as("New created user response body has not expected screen name")
                .isEqualTo(playerToCreate.getScreenName());
        softAssertions.assertThat(createdPlayer.getGender())
                .as("New created user response body has not expected gender")
                .isEqualTo(playerToCreate.getGender().getName());
        softAssertions.assertAll();
    }

    @Epic("Player Controller")
    @Test(description = "Verify that an existing player can be returned by a valid player id",
            groups = "delete-created-player")
    public void verifyExistingPlayerIsReturnedById() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData()
                .build();
        CreatePlayerResponse createdPlayer = playerClient.createPlayer(playerToCreate);
        GetPlayerByIdResponse getPlayerById = playerClient.getPlayer(createdPlayer.getId());

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(getPlayerById.getLogin())
                .as("Returned player by id does not have the expected login")
                .isEqualTo(playerToCreate.getLogin());
        softAssertions.assertThat(getPlayerById.getPassword())
                .as("Returned player by id does not have the expected password")
                .isEqualTo(playerToCreate.getPassword());
        softAssertions.assertThat(getPlayerById.getAge())
                .as("Returned player by id does not have the expected age")
                .isEqualTo(playerToCreate.getAge());
        softAssertions.assertThat(getPlayerById.getRole())
                .as("Returned player by id does not have the expected role")
                .isEqualTo(playerToCreate.getRole().getName());
        softAssertions.assertThat(getPlayerById.getScreenName())
                .as("Returned player by id does not have the screen name")
                .isEqualTo(playerToCreate.getScreenName());
        softAssertions.assertThat(getPlayerById.getGender())
                .as("Returned player by id does not have the gender")
                .isEqualTo(playerToCreate.getGender().getName());
        softAssertions.assertAll();
    }

    @Epic("Player Controller")
    @Test(description = "Verify that the '/player/get/all/' endpoint returns a list of all created players",
            groups = "delete-created-player")
    public void verifyAllPlayersAreReturned() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData().build();
        CreatePlayerResponse createdPlayer = playerClient.createPlayer(playerToCreate);

        GetAllPlayerResponse getAllPlayer = playerClient.getAllPlayer();
        Optional<GetAllPlayerResponse.PlayerResponse> playerFromAll = getAllPlayer.getPlayers()
                .stream()
                .filter(p -> p.getId() == createdPlayer.getId())
                .findFirst();

        if (playerFromAll.isPresent()) {
            SoftAssertions softAssertions = new SoftAssertions();
            softAssertions.assertThat(playerFromAll.get().getAge())
                    .as("New created player has not correct age")
                    .isEqualTo(playerToCreate.getAge());
            softAssertions.assertThat(playerFromAll.get().getGender())
                    .as("New created player has not correct gender")
                    .isEqualTo(playerToCreate.getGender().getName());
            softAssertions.assertThat(playerFromAll.get().getScreenName())
                    .as("New created player has not correct screen name")
                    .isEqualTo(playerToCreate.getScreenName());
            softAssertions.assertAll();
        } else
            throw new RuntimeException("There is no new create player after getting all players data");
    }

    @Epic("Player Controller")
    @Test(description = "Verify that a player information can be successfully updated",
            groups = "delete-created-player")
    public void verifyPlayerIsUpdatedSuccessfully() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData().build();
        CreatePlayerResponse createdPlayer = playerClient.createPlayer(playerToCreate);

        CreatePlayerRequest playerToUpdate = new CreatePlayerRequest(playerToCreate);
        playerToUpdate.setScreenName("new_updated_name");
        playerToUpdate.setPassword("new_updated_password");
        playerToUpdate.setAge(50);

        UpdatePlayerResponse updatedPlayer = playerClient.updatePlayer(RoleEnum.SUPERVISOR, createdPlayer.getId(), playerToUpdate);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(updatedPlayer.getLogin())
                .as("Retrieved player does not have the expected login")
                .isEqualTo(playerToUpdate.getLogin());
        softAssertions.assertThat(updatedPlayer.getAge())
                .as("Retrieved player does not have the expected age")
                .isEqualTo(playerToUpdate.getAge());
        softAssertions.assertThat(updatedPlayer.getRole())
                .as("Retrieved player does not have the expected role")
                .isEqualTo(playerToUpdate.getRole().getName());
        softAssertions.assertThat(updatedPlayer.getScreenName())
                .as("Retrieved player does not have the screen name")
                .isEqualTo(playerToUpdate.getScreenName());
        softAssertions.assertThat(updatedPlayer.getGender())
                .as("Retrieved player does not have the gender")
                .isEqualTo(playerToUpdate.getGender().toString());
        softAssertions.assertAll();
    }

    @Epic("Player Controller")
    @Test(description = "Verify that a 'supervisor' role can successfully delete an existing player")
    public void verifyPlayerIsDeletedSuccessfullyBySupervisor() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData().build();
        CreatePlayerResponse createdPlayer = playerClient.createPlayer(playerToCreate);

        DeletePlayerResponse deletedPlayerResponse = playerClient.deletePlayer(createdPlayer.getId(), RoleEnum.SUPERVISOR);
        if (!deletedPlayerResponse.getPlayers().isEmpty()) {
            assertThat(deletedPlayerResponse.getPlayers().get(0))
                    .as("With status code 200 player should be deleted with correct response body")
                    .isEqualTo(createdPlayer);
        } else {
            assertThat(deletedPlayerResponse.getPlayers())
                    .as("With status code 204 player should be deleted with empty response body")
                    .isEmpty();
        }
    }

    @Epic("Player Controller")
    @Test(description = "Verify that players have unique fields: id, login, screenName")
    public void verifyPlayersUniqueFields() {
        List<CreatePlayerRequest> requestModelList = new RandomPlayerBuilder().createNumberOfUsers(5);
        playerClient.createPlayers(requestModelList);
        GetAllPlayerResponse allPlayers = playerClient.getAllPlayer();

        Set<Integer> playersWithUniqueId = allPlayers.getPlayers()
                .stream()
                .map(GetAllPlayerResponse.PlayerResponse::getId)
                .collect(Collectors.toSet());
        Set<String> playersWithUniqueLogin = allPlayers.getPlayers()
                .stream()
                .map(pl -> playerClient.getPlayer(pl.getId()))
                .map(GetPlayerByIdResponse::getLogin)
                .collect(Collectors.toSet());
        Set<String> playersWithUniqueScreenName = allPlayers.getPlayers()
                .stream()
                .map(GetAllPlayerResponse.PlayerResponse::getScreenName)
                .collect(Collectors.toSet());

        assertThat(allPlayers.getPlayers().size())
                .as("There should not be 'id' duplication for the players")
                .isEqualTo(playersWithUniqueId.size());
        assertThat(allPlayers.getPlayers().size())
                .as("There should not be 'login' duplication for the players")
                .isEqualTo(playersWithUniqueLogin.size());
        assertThat(allPlayers.getPlayers().size())
                .as("There should not be 'screenName' duplication for the players")
                .isEqualTo(playersWithUniqueScreenName.size());
    }
}
