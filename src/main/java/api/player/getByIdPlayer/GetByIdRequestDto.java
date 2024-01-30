package api.player.getByIdPlayer;

import api.player.RequestDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetByIdRequestDto implements RequestDto {
  private Integer playerId;
}
