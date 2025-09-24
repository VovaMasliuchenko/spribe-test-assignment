package com.spribe.api.clients;

import com.spribe.api.models.request.CreatePlayerRequest;
import com.spribe.enums.UserRole;
import com.spribe.service.PlayerService;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PlayerApi {

    public static Response createPlayer(UserRole role, CreatePlayerRequest body) {
        Response response = given()
                .contentType(ContentType.JSON)
                .queryParam("age", body.getAge())
                .queryParam("gender", body.getGender())
                .queryParam("login", body.getLogin())
                .queryParam("password", body.getPassword())
                .queryParam("role", body.getRole())
                .queryParam("screenName", body.getScreenName())
                .when()
                .get("/player/create/%s".formatted(role))
                .then()
                .log().all()
                .extract().response();
        if (response.getStatusCode() == 200) {
            PlayerService.addPlayerIdToRemoveList(String.valueOf(response.jsonPath().getInt("id")));
        }
        return response;
    }

    public static Response getPlayerById(Object body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/player/get")
                .then()
                .log().all()
                .extract().response();
    }

    public static Response getAllPlayers() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/player/get/all")
                .then()
                .log().all()
                .extract().response();
    }

    public static Response updatePlayerById(UserRole role, int id, Object body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .patch("/player/update/%s/%s".formatted(role, id))
                .then()
                .log().all()
                .extract().response();
    }

    public static Response deletePlayerById(UserRole role, Object body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .delete("/player/delete/%s".formatted(role))
                .then()
                .log().all()
                .extract().response();
    }

}
