package bit.schedule.controller;

import bit.auth.domain.UserPrincipal;
import bit.exception.GlobalExceptionHandler;
import bit.schedule.domain.Schedule;
import bit.schedule.dto.ScheduleCreateRequest;
import bit.schedule.dto.ScheduleResponse;
import bit.schedule.dto.ScheduleUpdateRequest;
import bit.schedule.exception.ScheduleNotFoundException;
import bit.schedule.service.ScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static bit.schedule.util.ScheduleFixture.getNewSchedule;
import static bit.schedule.util.ScheduleRequestFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ScheduleController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class ScheduleControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserPrincipal userPrincipal;

    @MockBean
    private ScheduleService scheduleService;

    @BeforeEach
    public void setUp() {
        given(userPrincipal.getId()).willReturn(0L);
    }

    @DisplayName("스케줄 ID로 조회 테스트")
    @Test
    void getByScheduleIdTest() throws Exception {
        //Given
        Schedule schedule = getNewSchedule(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        ScheduleResponse response = new ScheduleResponse(schedule);
        given(scheduleService.getSchedule(0L, 0L)).willReturn(response);
        //When
        //Then
        mockMvc.perform(
                        get("/api/v1/schedule/0")
                                .contentType("application/json")
                                .with(SecurityMockMvcRequestPostProcessors.user(userPrincipal)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("스케줄 유저 ID로 조회 테스트")
    @Test
    void getByUserIdTest() throws Exception {
        //Given
        Schedule schedule = getNewSchedule(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        List<ScheduleResponse> response = IntStream.range(0, 10)
                .mapToObj(i -> new ScheduleResponse(schedule))
                .toList();
        when(scheduleService.getSchedulesByUserId(0L)).thenReturn(response);
        //When
        //Then
        mockMvc.perform(
                        get("/api/v1/schedule/user")
                                .contentType("application/json")
                                .with(SecurityMockMvcRequestPostProcessors.user(userPrincipal)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("스케줄 저장 테스트")
    @Test
    void saveTest() throws Exception {
        //Given
        Schedule schedule = getNewSchedule(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        ScheduleCreateRequest request = getNewScheduleCreateRequest(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        ScheduleResponse response = new ScheduleResponse(schedule);
        when(scheduleService.saveSchedule(any(Long.class), any(ScheduleCreateRequest.class))).thenReturn(response);
        //When
        //Then
        mockMvc.perform(
                        post("/api/v1/schedule")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf())
                                .with(SecurityMockMvcRequestPostProcessors.user(userPrincipal)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }


    @DisplayName("스케줄 저장시 필수 요소가 없으면 에러가 발생한다.")
    @Test
    void saveValidTest() {
        //Given
        Schedule schedule = getNewSchedule(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        ScheduleResponse response = new ScheduleResponse(schedule);
        when(scheduleService.saveSchedule(any(Long.class), any(ScheduleCreateRequest.class))).thenReturn(response);
        List<ScheduleCreateRequest> scheduleCreateRequests = getNewScheduleRequests();
        List<String> errorMessages = List.of(
                "{\"title\":\"제목이 필요합니다.\"}",
                "{\"content\":\"내용이 필요합니다.\"}",
                "{\"startDateTime\":\"시작 일시가 필요합니다.\"}",
                "{\"endDateTime\":\"종료 일시가 필요합니다.\"}"
        );
        //When
        //Then
        for (int i = 0; i < scheduleCreateRequests.size(); i++) {
            ScheduleCreateRequest request = scheduleCreateRequests.get(i);
            String expectedErrorMessage = errorMessages.get(i);

            try {
                System.out.println("request = " + request);
                mockMvc.perform(
                                post("/api/v1/schedule")
                                        .contentType("application/json")
                                        .content(objectMapper.writeValueAsString(request))
                                        .with(csrf())
                                        .with(SecurityMockMvcRequestPostProcessors.user(userPrincipal)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(expectedErrorMessage))
                        .andDo(print());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @DisplayName("존재하지 않는 스케줄에 접근하면, 에러가 발생한다")
    @Test
    void getScheduleNotFoundTest() throws Exception {
        //Given
        when(scheduleService.getSchedule(0L, 0L)).thenThrow(new ScheduleNotFoundException());
        //When
        //Then
        mockMvc.perform(
                        get("/api/v1/schedule/0")
                                .contentType("application/json")
                                .with(SecurityMockMvcRequestPostProcessors.user(userPrincipal)))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().string("해당 스케줄이 존재하지 않습니다."));
    }


    @DisplayName("스케줄 수정 테스트")
    @Test
    void patchTest() throws Exception {
        //Given
        Schedule schedule = getNewSchedule(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        ScheduleUpdateRequest request = getNewScheduleUpdateRequest(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        ScheduleResponse response = new ScheduleResponse(schedule);
        when(scheduleService.updateSchedule(any(Long.class), eq(0L), any(ScheduleUpdateRequest.class))).thenReturn(response);
        //When
        //Then
        mockMvc.perform(
                        put("/api/v1/schedule/0")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf())
                                .with(SecurityMockMvcRequestPostProcessors.user(userPrincipal)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("스케줄 삭제 테스트")
    @Test
    void deleteTest() throws Exception {
        //Given
        Schedule schedule = getNewSchedule(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        ScheduleResponse response = new ScheduleResponse(schedule);
        when(scheduleService.deleteSchedule(0L, 0L)).thenReturn(response);
        //When
        //Then
        mockMvc.perform(
                        delete("/api/v1/schedule/0")
                                .contentType("application/json")
                                .with(csrf())
                                .with(SecurityMockMvcRequestPostProcessors.user(userPrincipal)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}