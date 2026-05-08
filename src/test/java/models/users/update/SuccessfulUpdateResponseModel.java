package models.users.update;

public record SuccessfulUpdateResponseModel(String id, String username,
                                            String firstName, String lastName,
                                            String email, String remoteAddr) {}