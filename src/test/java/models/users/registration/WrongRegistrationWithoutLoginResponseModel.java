package models.users.registration;


import java.util.List;

public record WrongRegistrationWithoutLoginResponseModel(List<String> username) {}