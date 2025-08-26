package bit.app.anniversary.service;

import bit.app.anniversary.dto.AnDto;
import bit.app.anniversary.dto.AnReqDto;
import bit.app.anniversary.dto.AnResDto;
import bit.app.anniversary.entity.Anniversary;
import bit.app.anniversary.repository.AnRepository;
import bit.app.auth.domain.UserPrincipal;
import bit.app.couple.dto.CoupleResponseDto;
import bit.app.couple.service.CoupleService;
import bit.app.user.dto.UserResponse;
import bit.app.user.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnService {

    private final AnRepository anRepository;
    private final CoupleService coupleService;

    @Transactional
    public AnResDto createAnniversary(UserPrincipal userPrincipal, AnReqDto anReqDto) {

        CoupleResponseDto coupleByUserId = coupleService.getCoupleByUserId(userPrincipal.getId());
        UserEntity writer = UserEntity.from(coupleByUserId.getInitiatorUser());

        UserEntity withPeople = UserEntity.from(coupleByUserId.getPartnerUser());

        AnDto anDto = anReqDto.toAnDto();
        Anniversary anniversary = anDto.toEntity();
        anniversary.createAnniversary(anDto, writer, withPeople);
        Anniversary saveAnniversary = anRepository.save(anniversary);

        UserResponse writerRes = UserResponse.from(writer.toDomain());
        UserResponse withPeopleRes = UserResponse.from(withPeople.toDomain());

        return AnResDto.from(saveAnniversary.toDto(), writerRes, withPeopleRes,
                anniversary.calculateDaysToAnniversary());
    }

    @Transactional
    public AnResDto updateAnniversary(UserPrincipal userPrincipal, Long id, AnReqDto anReqDto) {
        CoupleResponseDto coupleByUserId = coupleService.getCoupleByUserId(userPrincipal.getId());
        UserEntity writer = UserEntity.from(coupleByUserId.getInitiatorUser());
        UserEntity withPeople = UserEntity.from(coupleByUserId.getPartnerUser());

        // 기존 Anniversary 엔티티 조회
        Anniversary anniversary = anRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        // AnReqDto -> AnDto 변환 및 업데이트
        AnDto anDto = anReqDto.toAnDto();
        anniversary.updateAnniversary(anDto, writer, withPeople);

        // 변경 사항 저장
        Anniversary updatedAnniversary = anRepository.save(anniversary); // save 호출 추가

        // 변환하여 반환
        UserResponse writerRes = UserResponse.from(writer.toDomain());
        UserResponse withPeopleRes = UserResponse.from(withPeople.toDomain());

        return AnResDto.from(updatedAnniversary.toDto(), writerRes, withPeopleRes,
                updatedAnniversary.calculateDaysToAnniversary());
    }


    @Transactional
    public void deleteAnniversary(Long id) {
        anRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public AnResDto getAnniversary(Long id) {
        Anniversary anniversary = anRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return AnResDto.from(
                anniversary.toDto(),
                UserResponse.from(anniversary.getWriter().toDomain()),
                anniversary.getWithPeople() != null ?
                        UserResponse.from(anniversary.getWithPeople().toDomain())
                        : null,
                anniversary.calculateDaysToAnniversary()
        );
    }

    @Transactional(readOnly = true)
    public List<AnResDto> findAnniversariesInRange(UserPrincipal userPrincipal, LocalDateTime startDate,
                                                   LocalDateTime endDate,
                                                   Pageable pageable
    ) {

        return anRepository.findAllByDateRangeAndUserInvolvedById(startDate, endDate, userPrincipal.getId(), pageable)
                .stream()
                .map(anniversary -> AnResDto.from(
                        anniversary.toDto(),
                        UserResponse.from(anniversary.getWriter().toDomain()),
                        anniversary.getWithPeople() != null ?
                                UserResponse.from(anniversary.getWithPeople().toDomain())
                                : null,
                        anniversary.calculateDaysToAnniversary()
                ))
                .collect(Collectors.toList());
    }

    public LocalDateTime calculateNextAnniversary(Long id) {
        Anniversary anniversary = anRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return anniversary.calculateNextAnniversary();
    }
}
