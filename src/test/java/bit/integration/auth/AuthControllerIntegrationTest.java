package bit.integration.auth;

import bit.config.jwt.JwtFactory;
import bit.config.jwt.JwtProperties;
import bit.auth.domain.RefreshToken;
import bit.user.domain.User;
import bit.auth.dto.AccessTokenCreateRequest;
import bit.auth.repository.RefreshTokenRepository;
import bit.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {
    @Autowired
    protected MockMvc mockmvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void setUp() {
        this.mockmvc = MockMvcBuilders.webAppContextSetup(context).build();
        userRepository.deleteAll();
    }

    @DisplayName("회원 가입 요청 시 새로운 액세스 토큰을 발급 받을 수 있다.")
    @Test
    void createTokenIsSuccessful() throws Exception {
        // given
        User testUser = userRepository.save(
                User.builder()
                        .email("test@test.com")
                        .nickName("테스트 유저")
                        .build()
        );

        String refreshToken = JwtFactory.builder().claims(Map.of("id", testUser.getId())).build().createToken(jwtProperties);

        refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToken));

        AccessTokenCreateRequest requestDto = new AccessTokenCreateRequest();
        requestDto.setRefreshToken(refreshToken);

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        ResultActions resultActions = mockmvc.perform(post("/api/v1/auth/token").contentType(MediaType.APPLICATION_JSON).content(requestBody));

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }
}