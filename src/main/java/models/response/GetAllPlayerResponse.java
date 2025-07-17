package models.response;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Data
public class GetAllPlayerResponse {
    @JsonProperty("players")
    private List<PlayerResponse> players;

    @Getter
    public static class PlayerResponse {
        @JsonProperty("id")
        private int id;
        @JsonProperty("age")
        private int age;
        @JsonProperty("screenName")
        private String screenName;
        @JsonProperty("gender")
        private String gender;

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            PlayerResponse that = (PlayerResponse) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}


