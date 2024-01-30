package api.player.endpoints;

public enum PlayerEndpoints {
  CREATE("/player/create/{editor}/"),
  DELETE("/player/delete/{editor}/"),
  GET_BY_ID("/player/get/"),
  GET_ALL("/player/get/all/"),
  UPDATE("/player/update/{editor}/{id}/");

  private final String value;

  PlayerEndpoints(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
