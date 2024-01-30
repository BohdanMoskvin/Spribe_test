package api.player.playerData;

public enum PlayerRoleData {
  ADMIN("admin"),
  SUPERVISOR("supervisor"),
  USER("user");

  private final String role;

  PlayerRoleData(String role) {

    this.role = role;
  }

  public String getRole() {

    return role;
  }
}
