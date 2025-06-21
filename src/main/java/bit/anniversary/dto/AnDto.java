package bit.anniversary.dto;

import bit.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import bit.anniversary.entity.Anniversary;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

	public static AnDto fromEntity(Anniversary anniversary, ModelMapper modelMapper) {
		return modelMapper.map(anniversary, AnDto.class);
	}
}
