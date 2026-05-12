package helpers;

import models.clubs.get.ClubItemModel;
import models.clubs.get.SuccessfulGetClubListResponseModel;

public class ClubHelper {
    public static ClubItemModel findClubByTitle(SuccessfulGetClubListResponseModel response, String bookTitle) {
        return response.results().stream()
                .filter(club -> club.bookTitle().equals(bookTitle))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "Клуб с названием '" + bookTitle + "' не найден в списке. " +
                                "Всего клубов: " + response.count()
                ));
    }
}
