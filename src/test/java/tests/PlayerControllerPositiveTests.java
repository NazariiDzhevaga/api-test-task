package tests;

import enums.GenderEnum;
import enums.RoleEnum;
import io.restassured.response.Response;
import model.request.CreatePlayerRequest;
import model.response.*;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.*;
import playerControllerUtils.RandomPlayerBuilder;

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
        allPlayerAfterTests.forEach(e -> apiService.deletePlayer(e.getId(), RoleEnum.SUPERVISOR));
    }

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

    @Test(description = "Verify that an existing player can be returned by a valid player id",
            groups = "delete-created-player")
    public void verifyExistingPlayerIsReturnedById() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData()
                .build();
        CreatePlayerResponse createdPlayer = playerClient.createPlayer(playerToCreate);

        GetPlayerByIdResponse fetched = playerClient.getPlayer(createdPlayer.getId());

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(fetched.getLogin())
                .as("Returned player by id does not have the expected login")
                .isEqualTo(playerToCreate.getLogin());
        softAssertions.assertThat(fetched.getPassword())
                .as("Returned player by id does not have the expected password")
                .isEqualTo(playerToCreate.getPassword());
        softAssertions.assertThat(fetched.getAge())
                .as("Returned player by id does not have the expected age")
                .isEqualTo(playerToCreate.getAge());
        softAssertions.assertThat(fetched.getRole())
                .as("Returned player by id does not have the expected role")
                .isEqualTo(playerToCreate.getRole().getName());
        softAssertions.assertThat(fetched.getScreenName())
                .as("Returned player by id does not have the screen name")
                .isEqualTo(playerToCreate.getScreenName());
        softAssertions.assertThat(fetched.getGender())
                .as("Returned player by id does not have the gender")
                .isEqualTo(playerToCreate.getGender().getName());
        softAssertions.assertAll();
    }

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

    @Test(description = "Verify that a 'supervisor' role can successfully delete an existing player")
    public void verifyPlayerIsDeletedSuccessfullyBySupervisor() {
        CreatePlayerRequest playerToCreate = new RandomPlayerBuilder().withRandomValidData().build();
        CreatePlayerResponse createdPlayer = playerClient.createPlayer(playerToCreate);
//need to add if empty then 204
//if 200 then should be body
        DeletePlayerResponse deleted = playerClient.deletePlayer(createdPlayer.getId(), RoleEnum.SUPERVISOR);
        assertThat(deleted.getPlayers())
                .as("Player should be deleted")
                .isEmpty();
    }

    @Test(description = "Verify that  players have unique fields: id, login, screenName")
    public void verifyPlayersUniqueFields() {
        GetAllPlayerResponse allPlayers = playerClient.getAllPlayer();

        if (!allPlayers.getPlayers().isEmpty()) {
            List<CreatePlayerRequest> requestModelList = new RandomPlayerBuilder().createNumberOfUsers(10);
            playerClient.createPlayers(requestModelList);
            allPlayers = playerClient.getAllPlayer();
        }

        Set<Integer> playerWithUniqueId = allPlayers.getPlayers()
                .stream()
                .map(GetAllPlayerResponse.PlayerResponse::getId)
                .collect(Collectors.toSet());
        Set<String> playerWithUniqueLogin = allPlayers.getPlayers()
                .stream()
                .map(pl -> playerClient.getPlayer(pl.getId()))
                .map(GetPlayerByIdResponse::getLogin)
                .collect(Collectors.toSet());
        Set<String> playerWithUniqueScreenName = allPlayers.getPlayers()
                .stream()
                .map(GetAllPlayerResponse.PlayerResponse::getScreenName)
                .collect(Collectors.toSet());

        assertThat(allPlayers.getPlayers().size())
                .as("There should not be 'id' duplication for the players")
                .isEqualTo(playerWithUniqueId.size());
        assertThat(allPlayers.getPlayers().size())
                .as("There should not be 'login' duplication for the players")
                .isEqualTo(playerWithUniqueLogin.size());
        assertThat(allPlayers.getPlayers().size())
                .as("There should not be 'screenName' duplication for the players")
                .isEqualTo(playerWithUniqueScreenName.size());
    }
}
