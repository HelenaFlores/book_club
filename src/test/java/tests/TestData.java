package tests;

public class TestData {

    public static final String LOGIN_TOKEN_PREFIX = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    public static final String LOGIN_WRONG_CREDENTIALS_ERROR = "Invalid username or password.";
    public static final String LOGIN_WRONG_PASSWORD_OR_USERNAME_NULL =
            null;
    public static final String LOGIN_WRONG_PASSWORD_OR_USERNAME_NULL_ERROR =
            "This field may not be null.";

    public static final String REGISTRATION_EXISTING_USER_ERROR =
            "A user with that username already exists.";

    public static final String REGISTRATION_WRONG_WITHOUT_PASSWORD_OR_LOGIN =
            "This field is required.";

    public static final String REGISTRATION_IP_REGEXP =
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}"
                    + "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";

    public static final String ADDITIONAL_SYMBOLS = String.valueOf(System.currentTimeMillis());

    public static final String LOGOUT_WRONG_DETAIL_ERROR =
            "Token is invalid";

    public static final String LOGOUT_WRONG_CODE_ERROR =
            "token_not_valid";

    public static final String LOGOUT_WRONG_REFRESH_ERROR =
            "This field is required.";

    public static final String UPDATE_WRONG_DETAIL_ERROR = "Method \"POST\" not allowed.";

}
