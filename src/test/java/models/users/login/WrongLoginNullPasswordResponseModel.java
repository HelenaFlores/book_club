package models.users.login;

import java.util.List;

public record WrongLoginNullPasswordResponseModel(List<String> password) {}