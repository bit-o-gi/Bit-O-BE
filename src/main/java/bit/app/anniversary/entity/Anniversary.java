package bit.app.anniversary.entity;

import bit.app.anniversary.dto.AnDto;
import bit.app.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Anniversary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String writeTime;
    private String updateTime;
    private String title;
    private String content;
    private LocalDateTime anniversaryDate;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private UserEntity writer;

    @ManyToOne
    @JoinColumn(name = "with_people_id")
    private UserEntity withPeople;

    public AnDto toDto() {
        return AnDto.builder()
                .id(this.id)
                .writerEmail(this.writer != null ? this.writer.getEmail() : null)
                .withPeopleEmail(this.withPeople != null ? this.withPeople.getEmail() : null)
                .writeTime(this.writeTime)
                .updateTime(this.updateTime)
                .title(this.title)
                .content(this.content)
                .anniversaryDate(this.anniversaryDate != null ? this.anniversaryDate.toString() : null)
                .build();
    }

    public void updateAnniversary(AnDto anDto, UserEntity writer, UserEntity withPeople) {
        this.title = anDto.getTitle();
        this.content = anDto.getContent();
        this.updateTime = anDto.getUpdateTime();
        this.writer = writer;
        this.withPeople = withPeople;
        this.updateTime = LocalDateTime.now().toString();
    }

    public void createAnniversary(AnDto anDto, UserEntity writer, UserEntity withPeople) {
        this.title = anDto.getTitle();
        this.content = anDto.getContent();
        this.updateTime = anDto.getUpdateTime();
        this.writer = writer;
        this.withPeople = withPeople;
        this.writeTime = LocalDateTime.now().toString();
        this.updateTime = LocalDateTime.now().toString();
    }

    public long calculateDaysToAnniversary() {
        return ChronoUnit.DAYS.between(LocalDateTime.now(), this.anniversaryDate);
    }

    public LocalDateTime calculateNextAnniversary() {
        return this.anniversaryDate.plusYears(1);
    }
}
