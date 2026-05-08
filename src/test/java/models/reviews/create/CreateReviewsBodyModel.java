package models.reviews.create;

public record CreateReviewsBodyModel(int club,
                                     String review,
                                     int assessment,
                                     int readPages) {}
