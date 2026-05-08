package models.users.logout;


import java.util.List;

public record WrongLogoutWithoutTokenResponseModel(List<String> refresh) {}