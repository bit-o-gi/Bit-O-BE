package bit.anniversary.service;

import bit.anniversary.dto.AnDto;
import bit.anniversary.dto.AnReqDto;
import bit.anniversary.dto.AnResDto;
import bit.anniversary.entity.Anniversary;
import bit.anniversary.repository.AnRepository;
import bit.user.dto.UserResponse;
import bit.user.entity.UserEntity;
import bit.user.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnService {

    private final AnRepository anRepository;
    private final UserJpaRepository userRepository;
    private final ModelMapper modelMapper;

    public AnResDto createAnniversary(AnReqDto anReqDto) {
        UserEntity writer = userRepository.findByEmail(anReqDto.getWriterEmail())
                .orElseThrow(() -> new EntityNotFoundException("Writer not found"));
        UserEntity withPeople = userRepository.findByEmail(anReqDto.getWithPeopleEmail())
                .orElseThrow(() -> new EntityNotFoundException("WithPeople not found"));

        AnDto anDto = modelMapper.map(anReqDto, AnDto.class);
        Anniversary anniversary = anDto.toEntity(modelMapper);
        anniversary.updateAnniversary(anDto, writer, withPeople);
        anRepository.save(anniversary);

        UserResponse writerRes = UserResponse.from(writer.toDomain());
        UserResponse withPeopleRes = UserResponse.from(withPeople.toDomain());

        return AnResDto.from(anDto, writerRes, withPeopleRes, anniversary.calculateDaysToAnniversary());
    }

    @Transactional
    public AnResDto updateAnniversary(Long id, AnReqDto anReqDto) {
        // 기존 Anniversary 엔티티 조회
        Anniversary anniversary = anRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        // 작성자와 관련된 사용자를 찾기
        UserEntity writer = userRepository.findByEmail(anReqDto.getWriterEmail())
                .orElseThrow(() -> new EntityNotFoundException("Writer not found"));
        UserEntity withPeople = userRepository.findByEmail(anReqDto.getWithPeopleEmail())
                .orElseThrow(() -> new EntityNotFoundException("WithPeople not found"));

        // AnReqDto -> AnDto 변환 및 업데이트
        AnDto anDto = anReqDto.toAnDto();
        anniversary.updateAnniversary(anDto, writer, withPeople);

        // 변경 사항 저장
        Anniversary updatedAnniversary = anRepository.save(anniversary); // save 호출 추가

        // 변환하여 반환
        UserResponse writerRes = UserResponse.from(writer.toDomain());
        UserResponse withPeopleRes = UserResponse.from(withPeople.toDomain());

        return AnResDto.from(anDto, writerRes, withPeopleRes, updatedAnniversary.calculateDaysToAnniversary());
    }


    @Transactional
    public void deleteAnniversary(Long id) {
        anRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public AnResDto getAnniversary(Long id) {
        Anniversary anniversary = anRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return AnResDto.from(
                anniversary.toDto(modelMapper),
                UserResponse.from(anniversary.getWriter().toDomain()),
                UserResponse.from(anniversary.getWithPeople().toDomain()),
                anniversary.calculateDaysToAnniversary()
        );
    }

    @Transactional(readOnly = true)
    public List<AnResDto> findAnniversariesInRange(LocalDateTime startDate, LocalDateTime endDate) {
        return anRepository.findAllByAnniversaryDateBetween(startDate, endDate)
                .stream()
                .map(anniversary -> AnResDto.from(
                        anniversary.toDto(modelMapper),
                        UserResponse.from(anniversary.getWriter().toDomain()),
                        UserResponse.from(anniversary.getWithPeople().toDomain()),
                        anniversary.calculateDaysToAnniversary()
                ))
                .collect(Collectors.toList());
    }

    public void sendAnniversaryNotification(AnResDto anniversary, String message) throws Exception {
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] buffer = message.getBytes();
            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 9876);
            socket.send(packet);
        }
    }

    public LocalDateTime calculateNextAnniversary(Long id) {
        Anniversary anniversary = anRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return anniversary.calculateNextAnniversary();
    }
}
