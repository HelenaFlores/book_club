package models.clubs.get;

public record ReviewModel(
        int id,
        int club,
        UserModel user,
        String review,
        int assessment,
        int readPages,
        String created,
        String modified
) {}