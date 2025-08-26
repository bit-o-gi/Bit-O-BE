package bit.app.anniversary.dto;

import bit.app.user.dto.UserResponse;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AnResDto {

    private Long id;
    private String writeTime;
    private String title;
    private String updateTime;
    private String content;
    private LocalDateTime anniversaryDate;
    private UserResponse writer;
    private UserResponse withPeople;
    private long daysToAnniversary;

    public static AnResDto from(AnDto anDto, UserResponse writer, UserResponse withPeople, long daysToAnniversary) {
        AnResDto resDto = new AnResDto();
        resDto.id = anDto.getId();
        resDto.writeTime = anDto.getWriteTime();
        resDto.title = anDto.getTitle();
        resDto.updateTime = anDto.getUpdateTime();
        resDto.content = anDto.getContent();
        resDto.anniversaryDate =
                anDto.getAnniversaryDate() != null ? LocalDateTime.parse(anDto.getAnniversaryDate()) : null;
        resDto.writer = writer;
        resDto.withPeople = withPeople;
        resDto.daysToAnniversary = daysToAnniversary;
        return resDto;
    }
}
