package bit.user.controller;

import bit.auth.domain.UserPrincipal;
import bit.user.dto.UserResponse;
import bit.user.service.UserDetailService;
import bit.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController implements UserControllerDoc {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal UserPrincipal principal){
        return ResponseEntity.ok(UserResponse.from(userService.getById(principal.getId())));
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(UserResponse.from(userService.getByEmail(email)));
    }


}
