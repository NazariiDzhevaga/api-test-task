package models.response;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class DeletePlayerResponse {
    @JsonProperty("players")
    private List<Player> players = Collections.emptyList();

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    public static class Player {
        @JsonProperty("age")
        private int age;
        @JsonProperty("gender")
        private String gender;
        @JsonProperty("id")
        private long id;
        @JsonProperty("role")
        private String role;
        @JsonProperty("screenName")
        private String screenName;
    }
}


