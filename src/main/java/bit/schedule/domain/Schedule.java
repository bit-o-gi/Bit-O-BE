package bit.schedule.domain;

import bit.base.BaseEntity;
import bit.schedule.dto.ScheduleUpdateRequest;
import bit.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String title;

    private String content;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @Builder
    public Schedule(UserEntity user, String title, String content, LocalDateTime startDateTime,
                    LocalDateTime endDateTime) {
        checkStartEndDateTime(startDateTime, endDateTime);
        this.user = Objects.requireNonNull(user);
        this.title = Objects.requireNonNull(title);
        this.content = Objects.requireNonNull(content);
        this.startDateTime = Objects.requireNonNull(startDateTime);
        this.endDateTime = Objects.requireNonNull(endDateTime);
    }

    public void update(ScheduleUpdateRequest scheduleUpdateRequest) {
        Objects.requireNonNull(scheduleUpdateRequest);
        checkStartEndDateTime(scheduleUpdateRequest.getStartDateTime(), scheduleUpdateRequest.getEndDateTime());
        this.title = Objects.requireNonNull(scheduleUpdateRequest.getTitle());
        this.content = Objects.requireNonNull(scheduleUpdateRequest.getContent());
        this.startDateTime = Objects.requireNonNull(scheduleUpdateRequest.getStartDateTime());
        this.endDateTime = Objects.requireNonNull(scheduleUpdateRequest.getEndDateTime());
    }

    private void checkStartEndDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("시작 시간은 종료 시간보다 늦을 수 없습니다.");
        }
    }
}
