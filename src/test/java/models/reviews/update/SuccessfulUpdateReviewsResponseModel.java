package models.reviews.update;

import models.reviews.create.UserModel;

public record SuccessfulUpdateReviewsResponseModel(int id,
                                                   int club,
                                                   String review,
                                                   int assessment,
                                                   int readPages,
                                                   String created,
                                                   String modified,
                                                   UserModel user) {
}