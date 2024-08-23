package bit.anniversary.entity;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;

import bit.anniversary.dto.AnDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Anniversary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String writetime;

	private String title;

	private String withpeople;

	private String content;

	private static ModelMapper modelMapper = new ModelMapper();

	public AnDto creatAnniversary() {
		return modelMapper.map(this, AnDto.class);
	}

	public void update(AnDto anDto) {
		this.writetime = anDto.getWritetime();
		this.title = anDto.getTitle();
		this.withpeople = anDto.getWithpeople();
		this.content = anDto.getContent();
	}

}

