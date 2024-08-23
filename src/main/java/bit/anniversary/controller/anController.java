package bit.anniversary.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import bit.anniversary.dto.AnDto;
import bit.anniversary.dto.AnReqDto;
import bit.anniversary.sevice.AnService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class anController {

	private final AnService anniversaryservice;

	// 	TODO: 기념일 설정 기능
	// 	TODO: 함께 하는 사람 , 글쓴이 회원가입 이후 구현.
	@MutationMapping
	public AnReqDto createAnniversary(@Argument AnDto andto) {

		return anniversaryservice.saveAnniverSary(andto).createAnReqDto();
	}

	//	TODO: 기념일 업데이트 기능
	// 	Input : 기념일 시간 , 입력 시간 , 수정 시간 , 기념일 제목 ,글쓴이 , 함께 하는 사람 ,기념일 내용
	// 	TODO: 함께 하는 사람 , 글쓴이 회원가입 이후 구현
	@MutationMapping
	public AnReqDto updateAnniversary(@Argument AnDto andto) {
		return anniversaryservice.updateAnniverSary(andto).createAnReqDto();
	}

	//	TODO: 기념일 삭제 기능
	//  Input : 기념일 id
	//  TODO:
	@MutationMapping
	public AnReqDto deleteAnniversary(@Argument AnDto andto) {
		anniversaryservice.deleteAnniverSary(andto);
		return andto.createAnReqDto();
	}

	//  TODO: 기념일 가져오는기능
	@QueryMapping
	public AnReqDto getAnniversary(@Argument AnDto andto) {
		return anniversaryservice.getAnniverSary(andto);
	}

	// TODO : 기념일 리스트 가져오는기능
	@QueryMapping
	public List<AnReqDto> getListAnniversary() {
		return anniversaryservice.getAnniverSaryList();
	}

}