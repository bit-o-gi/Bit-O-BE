package bit.schedule.domain;

import bit.base.BaseEntity;
import bit.schedule.dto.ScheduleUpdateRequest;
import bit.schedule.enums.ScheduleColor;
import bit.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@EqualsAndHashCode(exclude = "user", callSuper = false)
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

    private String location;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @Enumerated(value = EnumType.STRING)
    private ScheduleColor color;

    @Builder
    public Schedule(UserEntity user, String title, String content, String location,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        ScheduleColor color) {
        checkStartEndDateTime(startDateTime, endDateTime);
        this.user = Objects.requireNonNull(user);
        this.title = Objects.requireNonNull(title);
        this.content = Objects.requireNonNull(content);
        this.location = Objects.requireNonNull(location);
        this.startDateTime = Objects.requireNonNull(startDateTime);
        this.endDateTime = Objects.requireNonNull(endDateTime);
        this.color = Objects.requireNonNull(color);
    }

    public void update(ScheduleUpdateRequest scheduleUpdateRequest) {
        Objects.requireNonNull(scheduleUpdateRequest);
        this.title = Objects.requireNonNullElse(scheduleUpdateRequest.getTitle(), this.title);
        this.content = Objects.requireNonNullElse(scheduleUpdateRequest.getContent(), this.content);
        this.location = Objects.requireNonNullElse(scheduleUpdateRequest.getLocation(),
            this.location);
        this.startDateTime = Objects.requireNonNullElse(scheduleUpdateRequest.getStartDateTime(),
            this.startDateTime);
        this.endDateTime = Objects.requireNonNullElse(scheduleUpdateRequest.getEndDateTime(),
            this.endDateTime);
        if (scheduleUpdateRequest.getColor() != null) {
            this.color = ScheduleColor.fromHexCode(scheduleUpdateRequest.getColor());
        }
        checkStartEndDateTime(this.startDateTime, this.endDateTime);
    }

    private void checkStartEndDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("시작 시간은 종료 시간보다 늦을 수 없습니다.");
        }
    }
}
