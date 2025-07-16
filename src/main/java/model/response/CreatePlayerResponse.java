package model.response;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class CreatePlayerResponse {
    @JsonProperty("id")
    private int id;
    @JsonProperty("age")
    private int age;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("login")
    private String login;
    @JsonProperty("password")
    private String password;
    @JsonProperty("role")
    private String role;
    @JsonProperty("screenName")
    private String screenName;
}
