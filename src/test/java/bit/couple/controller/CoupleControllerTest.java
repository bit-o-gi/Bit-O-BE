//package bit.couple.controller;
//
//import bit.couple.domain.Couple;
//import bit.couple.dto.CoupleCreateRequest;
//import bit.couple.service.CoupleService;
//import bit.couple.fixture.CoupleFixtures;
//import bit.user.domain.User;
//import bit.user.dto.UserCreateRequest;
//import bit.user.service.UserService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.doNothing;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = CoupleController.class)
//@WithMockUser
//@ActiveProfiles("test")
//class CoupleControllerTest {
//
//    private final String COUPLE_PATH = "/api/v1/couple";
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CoupleService coupleService;
//
//    @MockBean
//    private UserService userService;
//
//    @Test
//    @DisplayName("커플 생성 요청시, 커플이 생성된다.")
//    void createCouple() throws Exception {
//        // given
//        List<User> users = CoupleFixtures.initialUsers();
//
//        userService.createUser(UserCreateRequest.fromUser(users.get(0)));
//        userService.createUser(UserCreateRequest.fromUser(users.get(1)));
//
//        String receiverEmail = users.get(0).getEmail();
//        String senderEmail = users.get(1).getEmail();
//
//        CoupleCreateRequest coupleCreateRequest = makeCoupleRequest(receiverEmail, senderEmail);
//
//        // when
//        // then
//        mockMvc.perform(
//                        post(COUPLE_PATH)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(coupleCreateRequest))
//                                .with(csrf())
//
//                ).andDo(print())
//                .andExpect(status().isCreated());
//    }
//
//    private CoupleCreateRequest makeCoupleRequest(String receiverEmail, String senderEmail) {
//        return CoupleCreateRequest.builder()
//                .receiverEmail(receiverEmail)
//                .senderEmail(senderEmail)
//                .build();
//    }
//
//    @Test
//    @DisplayName("커플 승인 요청 시, 커플이 승인된다.")
//    void approveCouple() throws Exception {
//        // given
//        Couple couple = CoupleFixtures.initialCouple();
//        doNothing().when(coupleService).approveCouple(any());
//
//        // when
//        mockMvc.perform(
//                        put(COUPLE_PATH + "/" + couple.getId())
//                                .with(csrf()))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("커플 삭제 요청 시, 커플이 삭제된다 (hard delete)")
//    void deleteCouple() throws Exception {
//        // given
//        Couple couple = CoupleFixtures.initialCouple();
//        doNothing().when(coupleService).deleteCouple(any());
//
//        // when //then
//        mockMvc.perform(
//                        delete(COUPLE_PATH + "/" + couple.getId()).with(csrf()))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//}
