package api.player.updatePlayer;

import lombok.Data;

@Data
public class UpdatePlayerResponseDto {
    private Integer age;
    private String gender;
    private Integer id;
    private String login;
    private String password;
    private String role;
    private String screenName;

}
