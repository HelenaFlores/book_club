package models.users.logout;


public record WrongLogoutNoValidTokenResponseModel(String detail, String code) {}