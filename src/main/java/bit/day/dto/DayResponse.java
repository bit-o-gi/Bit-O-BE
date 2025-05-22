package bit.day.dto;

import bit.day.domain.Day;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DayResponse {

    private Long id;

    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;

    public static DayResponse from(Day day) {
        return DayResponse.builder()
                .id(day.getId())
                .title(day.getTitle())
                .startDate(day.getStartDate())
                .build();
    }
}
