package bit.day.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bit.day.domain.Day;
import bit.day.dto.DayCommand;
import bit.day.dto.DayRequest;
import bit.day.fixture.DayTestFixture;
import bit.day.service.DayService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = DayController.class)
@WithMockUser
class DayControllerTest {

    private final String DDAY_PATH = "/api/v1/day";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DayService dayService;

    @DisplayName("디데이 조회 시, 정상 조회 성공한다.")
    @Test
    void successDayRetrieve() throws Exception {
        // given
        Long dayId = 1L;
        Day targetDay = DayTestFixture.createSampleDay();
        when(dayService.getDay(dayId)).thenReturn(targetDay);

        // when
        ResultActions result = mockMvc.perform(get(DDAY_PATH + "/" + dayId).with(csrf()));

        // then
        result.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(targetDay.getId()))
                .andExpect(jsonPath("$.title").value(targetDay.getTitle()))
                .andExpect(jsonPath("$.startDate").value(targetDay.getStartDate().toString()));
    }

    @DisplayName("디데이 생성 성공시, 디데이가 정상적으로 생성된다.")
    @Test
    void successDayCreate() throws Exception {
        // given
        DayRequest testRequest = new DayRequest(1L, "testRequest", LocalDate.of(2024, 11, 12));
        Day targetDay = DayTestFixture.createSampleDay();
        DayCommand command = testRequest.toCommand();
        when(dayService.createDay(command)).thenReturn(targetDay);

        // when
        ResultActions result = mockMvc.perform(post(DDAY_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(command)).with(csrf()));

        // then
        result.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(targetDay.getId()))
                .andExpect(jsonPath("$.title").value(targetDay.getTitle()))
                .andExpect(jsonPath("$.startDate").value(targetDay.getStartDate().toString()));
    }

    @DisplayName("디데이 수정 요청 시, 디데이가 성공적으로 수정된다.")
    @Test
    void successDayUpdate() throws Exception {
        // given
        long id = 1L;
        DayRequest testRequest = new DayRequest(1L, "testRequest", LocalDate.of(2024, 11, 12));
        Day targetDay = DayTestFixture.createSampleDay();
        when(dayService.updateDay(id, testRequest.toCommand())).thenReturn(targetDay);

        //when
        ResultActions result = mockMvc.perform(
                put(DDAY_PATH + "/" + id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(testRequest)).with(csrf()));

        //then
        result.andDo(print())
                .andExpect(jsonPath("$.id").value(targetDay.getId()))
                .andExpect(jsonPath("$.title").value(targetDay.getTitle()))
                .andExpect(jsonPath("$.startDate").value(targetDay.getStartDate().toString()));
    }
}
