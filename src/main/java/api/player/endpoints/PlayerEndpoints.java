package api.player.endpoints;

public enum PlayerEndpoints {
  CREATE("/player/create/{editor}/"),
  DELETE("/player/delete/{editor}/"),
  GET_BY_ID("/player/get/"),
  GET_ALL("/player/get/all/"),
  UPDATE("/player/update/{editor}/{id}/");

  private final String endpoint;

  PlayerEndpoints(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getEndpoint() {
    return endpoint;
  }
}
