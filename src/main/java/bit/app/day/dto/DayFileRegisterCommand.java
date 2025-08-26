package bit.app.day.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class DayFileRegisterCommand {
    private final Long userId;
    private final Long dayId;
    private final MultipartFile file;
}
