package bit.user.service;

import static bit.app.user.enums.OauthPlatformType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import bit.app.user.domain.User;
import bit.app.user.dto.UserCreateRequest;
import bit.app.user.service.UserServiceImpl;
import bit.user.mock.FakeUserRepository;
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
                .platform(KAKAO)
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
                "platform"
        ).containsExactly(
                "pjhwork97@gmail.com",
                "MR_JO",
                KAKAO
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
        String existEmail = "pjhwork97@gmail.com";
        String nonExistEmail = "pjhwork979@gmail.com";

        // when then
        assertThat(userService.isRegisteredEmail(existEmail)).isTrue();
        assertThat(userService.isRegisteredEmail(nonExistEmail)).isFalse();
    }

    @DisplayName("유저 정보를 저장한다")
    @Test
    void createUserUserTest() {
        // given
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .email("pjhwork97@gmail.com")
                .nickName("MR_JO")
                .platform(KAKAO)
                .build();

        // when
        User user = userService.createUser(userCreateRequest);

        // then
        assertThat(user).extracting(
                        "email",
                        "nickName",
                        "platform")
                .containsExactly(
                        "pjhwork97@gmail.com",
                        "MR_JO",
                        KAKAO);
    }

}
