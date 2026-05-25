package models.reviews.update;

public record UpdateReviewsBodyModel(int club,
                                     String review,
                                     int assessment,
                                     int readPages) {
}