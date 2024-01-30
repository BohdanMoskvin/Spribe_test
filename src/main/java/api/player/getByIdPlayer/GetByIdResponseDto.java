package api.player.getByIdPlayer;

import api.player.RequestDto;
import lombok.Data;

@Data
public class GetByIdResponseDto implements RequestDto {
  private Integer id;
  private String login;
  private String password;
  private String screenName;
  private String gender;
  private Integer age;
  private String role;
}
