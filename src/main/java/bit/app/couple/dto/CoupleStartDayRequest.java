package bit.app.couple.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "커플 디데이 (시작일) 등록 정보 요청하는 DTO")
public class CoupleStartDayRequest {

    @Schema(description = "커플 시작일")
    @NotNull
    private LocalDate startDate;

    @Schema(description = "커플 애칭")
    private String coupleTitle;
}
