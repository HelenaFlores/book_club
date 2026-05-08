package models.clubs.create;

public record ReviewModel(int id,
                          int club,
                          MembersModel user,
                          String review,
                          int assessment,
                          int readPages,
                          String created,
                          String modified) {}
