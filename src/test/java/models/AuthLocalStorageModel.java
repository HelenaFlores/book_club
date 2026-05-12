package models;

public record AuthLocalStorageModel(
        UserInfo user,
        String accessToken,
        String refreshToken,
        boolean isAuthenticated
) {
    public record UserInfo(
            int id,
            String username,
            String firstName,
            String lastName,
            String email,
            String remoteAddr
    ) {}
}
