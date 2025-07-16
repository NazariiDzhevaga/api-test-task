package client;

import enums.RoleEnum;
import io.restassured.response.Response;
import model.request.CreatePlayerRequest;
import model.response.*;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerControllerClient {
    private final PlayerControllerApiService apiService = new PlayerControllerApiService();

    public CreatePlayerResponse createPlayer(CreatePlayerRequest requestModel) {
        return apiService.createPlayer(requestModel).as(CreatePlayerResponse.class);
    }

    public List<CreatePlayerResponse> createPlayers(List<CreatePlayerRequest> requestModelList) {
        return requestModelList.stream()
                .map(e -> apiService.createPlayer(e).as(CreatePlayerResponse.class))
                .collect(Collectors.toList());
    }

    public DeletePlayerResponse deletePlayer(int playerIdToDelete, RoleEnum roleEnum) {
        Response response = apiService.deletePlayer(playerIdToDelete, roleEnum);
        int statusCode = response.getStatusCode();

        if (statusCode == 200 && response.getHeader("Content-Type") != null) {
            return response.as(DeletePlayerResponse.class);
        } else if (statusCode == 204) {
            return new DeletePlayerResponse();
        } else {
            throw new RuntimeException("Unexpected status code: " + statusCode + ". Response body: " + response.getBody().asString());
        }
    }

    public GetPlayerByIdResponse getPlayer(int playerId) {
        return apiService.getPlayer(playerId).as(GetPlayerByIdResponse.class);
    }

    public GetAllPlayerResponse getAllPlayer() {
        return apiService.getAllPlayer().as(GetAllPlayerResponse.class);
    }

    public UpdatePlayerResponse updatePlayer(RoleEnum editor, long playerId, CreatePlayerRequest playerToUpdate) {
        return apiService.updatePlayer(editor, playerId, playerToUpdate).as(UpdatePlayerResponse.class);
    }
}
