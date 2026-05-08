package models.users.registration;


import java.util.List;

public record WrongRegistrationWithoutPasswordResponseModel(List<String> password) {}