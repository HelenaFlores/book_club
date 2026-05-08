package models.reviews.create;

import models.clubs.create.ReviewModel;

import java.util.List;

public record SuccessfulCreateReviewsResponseModel(int id,
                                                   int club,
                                                   String review,
                                                   int assessment,
                                                   int readPages,
                                                   String created,
                                                   String modified,
                                                   UserModel user) {}