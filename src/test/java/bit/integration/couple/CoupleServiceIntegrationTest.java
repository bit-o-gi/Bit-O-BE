package bit.integration.couple;

import bit.couple.dto.CoupleCreateRequest;
import bit.couple.fixture.CoupleFixtures;
import bit.couple.service.CoupleService;
import bit.user.domain.User;
import bit.user.dto.UserCreateRequest;
import bit.user.service.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CoupleServiceIntegrationTest {

    @Autowired
    CoupleService coupleService;

    @Autowired
    UserServiceImpl userService;


    @Transactional
    @DisplayName("커플이 저장될 때, 유저의 정보가 update 되는지 확인한다.")
    @Test
    void createCoupleAndUserUpdate() {
        // given
        List<User> users = CoupleFixtures.initialUsers();

        userService.createUser(UserCreateRequest.fromUser(users.get(0)));
        userService.createUser(UserCreateRequest.fromUser(users.get(1)));

        String senderEmail = users.get(0).getEmail();
        String receiverEmail = users.get(1).getEmail();

        // when
        CoupleCreateRequest coupleCreateRequest = new CoupleCreateRequest(senderEmail, receiverEmail);
        coupleService.createCouple(coupleCreateRequest.toCommand());
        User user = userService.getByEmail(senderEmail);

        // then
        assertThat(user.getCouple()).isNotNull();
    }

}