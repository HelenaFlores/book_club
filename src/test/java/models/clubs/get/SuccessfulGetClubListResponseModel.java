package models.clubs.get;

import java.util.List;

public record SuccessfulGetClubListResponseModel(
        int count,
        String next,
        String previous,
        List<ClubItemModel> results
) { }
