package models.response;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UpdatePlayerResponse {
    @JsonProperty("id")
    private int id;
    @JsonProperty("age")
    private int age;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("login")
    private String login;
    @JsonProperty("role")
    private String role;
    @JsonProperty("screenName")
    private String screenName;
}
