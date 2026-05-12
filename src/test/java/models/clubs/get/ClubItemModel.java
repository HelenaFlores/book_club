package models.clubs.get;

import models.clubs.create.ReviewModel;

import java.util.List;


public record ClubItemModel(
        int id,
        String bookTitle,
        String bookAuthors,
        int publicationYear,
        String description,
        String telegramChatLink,
        int owner,
        List<Integer> members,
        List<ReviewModel> reviews,
        String created,
        String modified
) {}