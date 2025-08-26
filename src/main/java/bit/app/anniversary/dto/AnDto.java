package bit.app.anniversary.dto;

import bit.app.anniversary.entity.Anniversary;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnDto {

    private Long id;
    private String writerEmail;
    private String withPeopleEmail;
    private String writeTime;
    private String title;
    private String updateTime;
    private String content;
    private String anniversaryDate;

    public Anniversary toEntity() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime parsedAnniversaryDate = this.anniversaryDate != null
                ? LocalDateTime.parse(this.anniversaryDate, formatter)
                : null;

        return Anniversary.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .writeTime(this.writeTime)
                .updateTime(this.updateTime)
                .anniversaryDate(parsedAnniversaryDate)
                .build();
    }
}
