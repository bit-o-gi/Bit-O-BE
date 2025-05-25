package bit.day.domain;

import bit.base.BaseEntity;
import bit.couple.domain.Couple;
import bit.day.dto.DayRegisterCommand;
import bit.day.dto.DayUpdateCommand;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "day")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Day extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "couple_id")
    private Couple couple;

    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;

    public static Day create(DayRegisterCommand command, Couple couple) {
        return Day.builder()
                .title(command.title())
                .startDate(command.startDate())
                .couple(couple)
                .build();
    }

    public void update(DayUpdateCommand command) {
        this.title = command.title();
        this.startDate = command.startDate();
    }

    public void checkCouple(Couple couple) {
        if (!this.couple.equals(couple)) {
            throw new IllegalArgumentException("not match couple in day");
        }
    }
}
