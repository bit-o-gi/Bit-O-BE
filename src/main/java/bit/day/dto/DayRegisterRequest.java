package bit.day.dto;

import bit.base.BaseRequest;
import bit.day.domain.Day;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DayRegisterRequest implements BaseRequest<Day> {
    @NotNull
    private Long coupleId;
    @NotNull
    private String title;
    @NotNull
    private LocalDate startDate;

    @Override
    public DayRegisterCommand toCommand() {
        return new DayRegisterCommand(coupleId, title, startDate);
    }

}
