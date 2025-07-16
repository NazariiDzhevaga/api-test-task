package client;

import constants.EndpointApiConstants;
import enums.RoleEnum;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.request.CreatePlayerRequest;
import service.SpecificationApiFactory;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class PlayerControllerApiService {

    public Response createPlayer(CreatePlayerRequest requestModel) {
        return given(SpecificationApiFactory.getApiRequestSpecification())
                .contentType(ContentType.JSON)
                .pathParam("editor", requestModel.getEditor().getName())
                .queryParam("age", requestModel.getAge())
                .queryParam("gender", requestModel.getGender().getName())
                .queryParam("login", requestModel.getLogin())
                .queryParam("password", requestModel.getPassword())
                .queryParam("role", requestModel.getRole().getName())
                .queryParam("screenName", requestModel.getScreenName())
                .when()
                .get(EndpointApiConstants.CREATE_PLAYER)
                .then()
                .extract()
                .response();
    }

    public Response deletePlayer(int playerIdToDelete, RoleEnum roleEnum) {
        return given(SpecificationApiFactory.getApiRequestSpecification())
                .contentType(ContentType.JSON)
                .accept("*/*")
                .pathParam("editor", roleEnum.getName())
                .body(Map.of("playerId", playerIdToDelete))
                .when()
                .delete(EndpointApiConstants.DELETE_PLAYER)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response getPlayer(int playerIdToGet) {
        return given(SpecificationApiFactory.getApiRequestSpecification())
                .contentType(ContentType.JSON)
                .accept("*/*")
                .body(Map.of("playerId", playerIdToGet))
                .when()
                .post(EndpointApiConstants.GET_PLAYER_BY_PLAYER_ID)
                .then()
                .extract()
                .response();
    }

    public Response getAllPlayer() {
        return given(SpecificationApiFactory.getApiRequestSpecification())
                .when()
                .get(EndpointApiConstants.GET_ALL_PLAYERS)
                .then()
                .extract()
                .response();
    }

    public Response getAllPlayerWithPost() {
        return given(SpecificationApiFactory.getApiRequestSpecification())
                .when()
                .post(EndpointApiConstants.GET_ALL_PLAYERS)
                .then()
                .extract()
                .response();
    }

    public Response updatePlayer(RoleEnum editor, long playerIdToUpdate, CreatePlayerRequest updatedPlayer) {
        return given(SpecificationApiFactory.getApiRequestSpecification())
                .contentType(ContentType.JSON)
                .accept("*/*")
                .pathParam("editor", editor)
                .pathParam("id", playerIdToUpdate)
                .body(updatedPlayer)
                .when()
                .patch(EndpointApiConstants.UPDATE_PLAYERS)
                .then()
                .extract()
                .response();
    }
}
