package bit.app.day.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DayFileResponse {
    private Long dayId;
    private String thumbnailUrl;

    public static DayFileResponse of(Long dayId, String thumbnailUrl) {
        return DayFileResponse.builder()
                .dayId(dayId)
                .thumbnailUrl(thumbnailUrl)
                .build();
    }
}
