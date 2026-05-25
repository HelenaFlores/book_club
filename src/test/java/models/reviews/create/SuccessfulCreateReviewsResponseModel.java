package models.reviews.create;

public record SuccessfulCreateReviewsResponseModel(int id,
                                                   int club,
                                                   String review,
                                                   int assessment,
                                                   int readPages,
                                                   String created,
                                                   String modified,
                                                   UserModel user) {
}