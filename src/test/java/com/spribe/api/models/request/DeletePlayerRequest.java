package com.spribe.api.models.request;

public class DeletePlayerRequest {

    private int playerId;

    public DeletePlayerRequest(int playerId) {
        this.playerId = playerId;
    }

    public DeletePlayerRequest() {}

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

}
