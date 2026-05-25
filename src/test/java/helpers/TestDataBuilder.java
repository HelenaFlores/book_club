package helpers;

import models.users.registration.RegistrationBodyModel;
import net.datafaker.Faker;

public class TestDataBuilder {
    private static final Faker faker = new Faker();

    // Поля для хранения данных
    private String username;
    private String password;
    private String usernameSecond;
    private String passwordSecond;
    private String bookTitle;
    private String bookAuthors;
    private Integer publicationYear;
    private String description;
    private String telegramChatLink;
    private String review;
    private Integer assessment;
    private Integer readPages;
    private String updatedReview;
    private Integer updatedAssessment;
    private Integer updateReadPages;
    private String firstname;
    private String lastName;
    private String email;
    private String accessToken;
    private String accessTokenSecond;
    private Boolean userCreated;
    private RegistrationBodyModel registrationData;

    // Базовые генераторы
    private String generateUniqueSuffix() {
        return String.valueOf(System.nanoTime());
    }

    // Методы-билдеры (каждый возвращает this для цепочки)
    public TestDataBuilder withUser() {
        this.username = "user_" + System.nanoTime();
        this.password = "pass_" + System.nanoTime();
        this.userCreated = false;
        return this;
    }

    public TestDataBuilder withSecondUser() {
        this.usernameSecond = "user_" + System.nanoTime();
        this.passwordSecond = "pass_" + System.nanoTime();
        return this;
    }

    public TestDataBuilder withBook() {
        String suffix = generateUniqueSuffix();
        this.bookTitle = faker.book().title() + "_" + suffix;
        this.bookAuthors = faker.book().author();
        this.publicationYear = faker.number().numberBetween(1900, 2026);
        this.description = faker.lorem().sentence(10);
        this.telegramChatLink = "https://t.me/club_" + suffix;
        return this;
    }

    public TestDataBuilder withReview() {
        String suffix = generateUniqueSuffix();
        this.review = faker.book().title() + "_" + suffix;
        this.assessment = faker.number().numberBetween(1, 4);
        this.readPages = faker.number().positive();
        return this;
    }

    public TestDataBuilder withUpdatedReview() {
        this.updatedReview = faker.book().title() + "_updated";
        this.updatedAssessment = faker.number().numberBetween(1, 4);
        this.updateReadPages = faker.number().positive();
        return this;
    }

    public TestDataBuilder withPersonalInfo() {
        this.firstname = faker.name().firstName();
        this.lastName = faker.name().lastName();
        this.email = faker.internet().emailAddress();
        return this;
    }

    public TestDataBuilder withAccessToken(String token) {
        this.accessToken = token;
        return this;
    }

    public TestDataBuilder withSecondAccessToken(String token) {
        this.accessTokenSecond = token;
        return this;
    }

    public TestDataBuilder withRegistrationData() {
        this.registrationData = new RegistrationBodyModel(username, password);
        return this;
    }

    // Геттеры для получения значений
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUsernameSecond() {
        return usernameSecond;
    }

    public String getPasswordSecond() {
        return passwordSecond;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookAuthors() {
        return bookAuthors;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public String getDescription() {
        return description;
    }

    public String getTelegramChatLink() {
        return telegramChatLink;
    }

    public String getReview() {
        return review;
    }

    public Integer getAssessment() {
        return assessment;
    }

    public Integer getReadPages() {
        return readPages;
    }

    public String getUpdatedReview() {
        return updatedReview;
    }

    public Integer getUpdatedAssessment() {
        return updatedAssessment;
    }

    public Integer getUpdateReadPages() {
        return updateReadPages;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getAccessTokenSecond() {
        return accessTokenSecond;
    }

    public Boolean getUserCreated() {
        return userCreated;
    }

    public RegistrationBodyModel getRegistrationData() {
        return registrationData;
    }

    // Метод для создания копии с другим пользователем (для тестов с двумя пользователями)
    public TestDataBuilder cloneForSecondUser() {
        TestDataBuilder clone = new TestDataBuilder();
        clone.username = this.usernameSecond;
        clone.password = this.passwordSecond;
        clone.accessToken = this.accessTokenSecond;
        return clone;
    }
}