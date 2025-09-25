package com.spribe.api.models.response;

public class GetAllPlayersResponse {

    public int id;

    public String screenName;

    public String gender;

    public int age;

    public GetAllPlayersResponse(int id, String screenName, String gender, int age) {
        this.id = id;
        this.screenName = screenName;
        this.gender = gender;
        this.age = age;
    }

    public GetAllPlayersResponse() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
