package bit.day.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DayFileRequest {
    @NotNull
    private Long dayId;

    @Schema(
            description = "업로드할 파일",
            type = "string",
            format = "binary"
    )
    @NotNull
    private MultipartFile file;

    public DayFileRegisterCommand toCommand(Long userId) {
        return DayFileRegisterCommand.builder()
                .userId(userId)
                .dayId(dayId)
                .file(file)
                .build();
    }
}
