package api.player.createPlayer;

import lombok.Data;

@Data
public class CreatePlayerResponseDto {
  private Integer id;
  private String login;
  private String password;
  private String screenName;
  private String gender;
  private Integer age;
  private String role;
}
