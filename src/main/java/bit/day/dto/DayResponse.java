package bit.day.dto;

import bit.day.domain.Day;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DayResponse {
    @Schema(description = "시작일 ID")
    private Long id;

    @Schema(description = "제목")
    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;

    @Schema(description = "썸네일 URL")
    private String thumbnailUrl;

    public static DayResponse of(Day day, String fileUrl) {
        return DayResponse.builder()
                .id(day.getId())
                .title(day.getTitle())
                .startDate(day.getStartDate())
                .thumbnailUrl(fileUrl)
                .build();
    }
}
