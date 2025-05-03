package bit.schedule.service;

import static bit.schedule.util.ScheduleFixture.getNewSchedule;
import static bit.schedule.util.ScheduleRequestFixture.getNewScheduleCreateRequest;
import static bit.schedule.util.ScheduleRequestFixture.getNewScheduleUpdateRequest;
import static bit.user.enums.OauthPlatformType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import bit.schedule.domain.Schedule;
import bit.schedule.dto.ScheduleCreateRequest;
import bit.schedule.dto.ScheduleResponse;
import bit.schedule.dto.ScheduleUpdateRequest;
import bit.schedule.exception.ScheduleNotFoundException;
import bit.schedule.repository.ScheduleRepository;
import bit.user.domain.User;
import bit.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ScheduleService scheduleService;

    @DisplayName("스케줄 ID로 스케줄을 찾지 못한 경우 에러를 발생시킨다.")
    @Test
    void getScheduleTest() {
        //Given
        Long userId = 1L;
        Long scheduleId = 1L;
        given(scheduleRepository.findSchedule(scheduleId, userId)).willReturn(Optional.empty());
        //When Then
        assertThatThrownBy(() -> scheduleService.getSchedule(userId, scheduleId))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("해당 스케줄이 존재하지 않습니다.");
    }

    @DisplayName("스케줄 조회가 에러없이 되는지 확인한다.")
    @Test
    void saveNewScheduleTest() {
        //Given
        Long userId = 1L;
        Long scheduleId = 1L;
        Schedule schedule = getNewSchedule(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        given(scheduleRepository.findSchedule(scheduleId, userId)).willReturn(
            Optional.ofNullable(schedule));
        //When
        ScheduleResponse result = scheduleService.getSchedule(userId, scheduleId);
        //Then
        assertThat(result).isEqualTo(new ScheduleResponse(Objects.requireNonNull(schedule)));
    }

    @DisplayName("저장시 스케줄의 시작시간이 종료시간보다 늦으면 에러를 발생시킨다.")
    @Test
    void saveValidateStartEndTime() {
        //Given
        Long userId = 1L;
        ScheduleCreateRequest scheduleCreateRequest = getNewScheduleCreateRequest(
            LocalDateTime.now(), LocalDateTime.now().minusHours(1));
        given(userService.getById(userId)).willReturn(User.builder()
            .id(userId)
            .email("test@test.com")
            .nickName("test")
            .platform(KAKAO)
            .build());
        //When
        //Then
        assertThatThrownBy(() -> scheduleService.saveSchedule(userId, scheduleCreateRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("시작 시간은 종료 시간보다 늦을 수 없습니다.");
    }

    @DisplayName("업데이트시 스케줄의 시작시간이 종료시간보다 늦으면 에러를 발생시킨다.")
    @Test
    void updateValidateStartEndTime() {
        //Given
        Long userId = 1L;
        Long scheduleId = 1L;
        ScheduleUpdateRequest scheduleUpdateRequest = getNewScheduleUpdateRequest(
            LocalDateTime.now(), LocalDateTime.now().minusHours(1));
        given(scheduleRepository.findSchedule(userId, scheduleId)).willReturn(Optional.ofNullable(
            getNewSchedule(LocalDateTime.now(), LocalDateTime.now().plusHours(1))));
        //When
        //Then
        assertThatThrownBy(
            () -> scheduleService.updateSchedule(userId, scheduleId, scheduleUpdateRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("시작 시간은 종료 시간보다 늦을 수 없습니다.");
    }

    @DisplayName("업데이트시 스케줄이 존재하지 않으면 에러를 발생시킨다.")
    @Test
    void updateScheduleScheduleNotFoundException() {
        //Given
        Long userId = 1L;
        Long scheduleId = 1L;
        ScheduleUpdateRequest scheduleUpdateRequest = getNewScheduleUpdateRequest(
            LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        given(scheduleRepository.findSchedule(userId, scheduleId)).willReturn(Optional.empty());
        //When
        //Then
        assertThatThrownBy(
            () -> scheduleService.updateSchedule(userId, scheduleId, scheduleUpdateRequest))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("해당 스케줄이 존재하지 않습니다.");
    }

    @DisplayName("업데이트시 스케줄을 생성하지 않은 사용자가 업데이트하면 에러를 발생시킨다.")
    @Test
    void updateScheduleUserNotFoundException() {
        //Given
        Long userId = 1L;
        Long scheduleId = 1L;
        ScheduleUpdateRequest scheduleUpdateRequest = getNewScheduleUpdateRequest(
            LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        given(scheduleRepository.findSchedule(userId, scheduleId)).willReturn(Optional.empty());
        //When
        //Then
        assertThatThrownBy(
            () -> scheduleService.updateSchedule(userId, scheduleId, scheduleUpdateRequest))
            .isInstanceOf(ScheduleNotFoundException.class);
    }

    @DisplayName("삭제시 스케줄이 존재하지 않으면 에러를 발생시킨다.")
    @Test
    void deleteScheduleScheduleNotFoundException() {
        //Given
        Long userId = 1L;
        Long scheduleId = 1L;
        given(scheduleRepository.findSchedule(userId, scheduleId)).willReturn(Optional.empty());
        //When
        //Then
        assertThatThrownBy(() -> scheduleService.deleteSchedule(userId, scheduleId))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("해당 스케줄이 존재하지 않습니다.");
    }

    @DisplayName("삭제시 스케줄을 생성하지 않은 사용자가 삭제하면 에러를 발생시킨다.")
    @Test
    void deleteScheduleUserNotFoundException() {
        //Given
        Long userId = 1L;
        Long scheduleId = 1L;
        given(scheduleRepository.findSchedule(userId, scheduleId)).willReturn(Optional.empty());
        //When
        //Then
        assertThatThrownBy(() -> scheduleService.deleteSchedule(userId, scheduleId))
            .isInstanceOf(ScheduleNotFoundException.class);
    }
}