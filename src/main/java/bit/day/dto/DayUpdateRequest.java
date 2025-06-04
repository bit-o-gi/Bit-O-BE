package bit.day.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DayUpdateRequest {
    private Long dayId;
    private String title;
    private LocalDate startDate;

    public DayUpdateCommand toCommand(Long userId) {
        return new DayUpdateCommand(dayId, userId, title, startDate);
    }

}
