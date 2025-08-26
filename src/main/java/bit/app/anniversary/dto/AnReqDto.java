package bit.app.anniversary.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AnReqDto {

    private final String writerEmail;
    private final String withPeopleEmail;
    private final String writeTime;
    private final String title;
    private final String updateTime;
    private final String content;
    private final String anniversaryDate;

    @Builder
    public AnReqDto(String writerEmail, String withPeopleEmail, String writeTime, String title, String updateTime,
                    String content, String anniversaryDate) {
        this.writerEmail = writerEmail;
        this.withPeopleEmail = withPeopleEmail;
        this.writeTime = writeTime;
        this.title = title;
        this.updateTime = updateTime;
        this.content = content;
        this.anniversaryDate = anniversaryDate;
    }

    public AnDto toAnDto() {
        return AnDto.builder()
                .writerEmail(this.writerEmail)
                .withPeopleEmail(this.withPeopleEmail)
                .writeTime(this.writeTime)
                .title(this.title)
                .updateTime(this.updateTime)
                .content(this.content)
                .anniversaryDate(this.anniversaryDate)
                .build();
    }
}
