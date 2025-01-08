package net.mathimomos.wormhole_artifact.server.message;

public class PlayerData {
    private final String playerName;
    private final String playerDimension;
    private final int playerDistance;

    public PlayerData(String playerName, String playerDimension, int playerDistance) {
        this.playerName = playerName;
        this.playerDimension = playerDimension;
        this.playerDistance = playerDistance;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerDimension() {
        return playerDimension;
    }

    public int getPlayerDistance() {
        return playerDistance;
    }
}
