package helpers;

import api.ClubsApiClient;
import models.clubs.get.ClubItemModel;
import models.clubs.get.SuccessfulGetClubListResponseModel;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.sleep;

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

    public static ClubItemModel waitForClubInList(
            String accessToken,
            String bookTitle,
            ClubsApiClient api,
            int timeoutSec) {

        long endTime = System.currentTimeMillis() + (timeoutSec * 1000L);

        while (System.currentTimeMillis() < endTime) {
            try {
                var response = api.getClubList(accessToken);
                var found = response.results().stream()
                        .filter(club -> club.bookTitle().equals(bookTitle))
                        .findFirst();

                if (found.isPresent()) {
                    return found.get();
                }
            } finally {
                sleep(Duration.ofMillis(500).toMillis());
            }

            throw new AssertionError(
                    "Клуб '" + bookTitle + "' не появился в списке за " + timeoutSec + " секунд"
            );
        }
        return null;
    }
}