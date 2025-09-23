package com.spribe.api.models.request;

public class DeletePlayerRequest {

    private String playerId;

    public DeletePlayerRequest(String playerId) {
        this.playerId = playerId;
    }

    public DeletePlayerRequest() {}

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

}
