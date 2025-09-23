package com.spribe.api.models.request;

public class GetPlayerByIdRequest {

    private String playerId;

    public GetPlayerByIdRequest(String playerId) {
        this.playerId = playerId;
    }

    public GetPlayerByIdRequest() {}

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

}
