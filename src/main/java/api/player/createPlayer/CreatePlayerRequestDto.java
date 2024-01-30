package api.player.createPlayer;

import api.player.RequestDto;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreatePlayerRequestDto implements RequestDto {
  private Integer age;
  private String gender;
  private String login;
  private String password;
  private String role;
  private String screenName;
}
