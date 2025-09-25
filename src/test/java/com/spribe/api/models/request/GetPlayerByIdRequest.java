package com.spribe.api.models.request;

public class GetPlayerByIdRequest {

    private int playerId;

    public GetPlayerByIdRequest(int playerId) {
        this.playerId = playerId;
    }

    public GetPlayerByIdRequest() {}

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

}
