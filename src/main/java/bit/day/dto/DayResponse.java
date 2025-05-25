package bit.day.dto;

import bit.day.domain.Day;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DayResponse {
    @Schema(description = "시작일 ID")
    private Long id;

    @Schema(description = "제목")
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
