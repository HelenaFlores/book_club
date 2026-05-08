package models.clubs.create;

import java.util.List;

public record SuccessfulCreateClubResponseModel(int id,
                                                String bookTitle,
                                                String bookAuthors,
                                                int publicationYear,
                                                String description,
                                                String telegramChatLink,
                                                int owner,
                                                List<Integer> members,
                                                List<ReviewModel> reviews,
                                                String created,
                                                String modified) {}
