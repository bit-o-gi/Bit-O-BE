package bit.user.service;

import static bit.user.oauth.enums.OauthPlatformType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import bit.mock.FakeUserRepository;
import bit.user.domain.User;
import bit.user.dto.UserCreateRequest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserServiceImplTest {

    private UserServiceImpl userService;

    @BeforeEach
    void init() {
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        userService = new UserServiceImpl(fakeUserRepository);
        fakeUserRepository.save(User.builder()
                .id(1L)
                .email("pjhwork97@gmail.com")
                .nickName("MR_JO")
                .gender("Male")
                .platform(KAKAO)
                .registerDate(LocalDateTime.of(2021, 1, 1, 23, 22, 43))
                .build()
        );
    }

    @DisplayName("getById로 유저를 찾아 올 수 있다.")
    @Test
    void getByIdTestCorrect() {
        // given
        // when
        User user = userService.getById(1L);

        // then
        assertThat(user).extracting(
                "email",
                "nickName",
                "gender",
                "platform",
                "registerDate"
        ).containsExactly(
                "pjhwork97@gmail.com",
                "MR_JO",
                "Male",
                KAKAO,
                LocalDateTime.of(2021, 1, 1, 23, 22, 43)
        );
    }

    @DisplayName("존재하지 않는 Id로 getById 할 시 예외가 발생한다.")
    @Test
    void getByIdTestThrowException() {
        // given
        long notExistsId = 325L;

        // when // then
        assertThatThrownBy(() -> userService.getById(notExistsId)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found");
    }

    @DisplayName("이메일이 기가입되어 있는지 확인한다.")
    @Test
    void getByEmailTest() {
        // given
        String exist_email = "pjhwork97@gmail.com";
        String non_exist_email = "pjhwork979@gmail.com";

        // when then
        assertThat(userService.isRegisteredEmail(exist_email)).isTrue();
        assertThat(userService.isRegisteredEmail(non_exist_email)).isFalse();
    }

    @DisplayName("유저 정보를 저장한다")
    @Test
    void createUserTest() {
        // given
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .email("pjhwork97@gmail.com")
                .nickName("MR_JO")
                .gender("Male")
                .platform(KAKAO)
                .build();

        // when
        User user = userService.create(userCreateRequest);

        // then
        assertThat(user).extracting(
                        "email",
                        "nickName",
                        "gender",
                        "platform")
                .containsExactly(
                        "pjhwork97@gmail.com",
                        "MR_JO",
                        "Male",
                        KAKAO);
    }

}