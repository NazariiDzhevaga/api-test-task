package model.request;

import enums.GenderEnum;
import enums.RoleEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreatePlayerRequest {
    public int age;
    public RoleEnum editor;
    public GenderEnum gender;
    public String login;
    public String password;
    public RoleEnum role;
    public String screenName;

    public CreatePlayerRequest(CreatePlayerRequest player) {
        this.age = player.age;
        this.editor = player.editor;
        this.gender = player.gender;
        this.login = player.login;
        this.password = player.password;
        this.role = player.role;
        this.screenName = player.screenName;
    }
}
