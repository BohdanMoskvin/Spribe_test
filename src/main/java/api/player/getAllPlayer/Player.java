package api.player.getAllPlayer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Player {
  private Integer id;
  private String screenName;
  private String gender;
  private Integer age;
  private String role;

}
