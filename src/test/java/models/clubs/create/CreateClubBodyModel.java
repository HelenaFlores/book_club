package models.clubs.create;

public record CreateClubBodyModel(String bookTitle,
                                  String bookAuthors,
                                  int publicationYear,
                                  String description,
                                  String telegramChatLink) {}
