package bit.user.controller;

import bit.user.dto.AccessTokenCreateRequest;
import bit.user.dto.AccessTokenResponse;
import bit.user.dto.UserResponse;
import bit.user.service.TokenService;
import bit.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController implements UserControllerDoc {

    private final UserService userService;
    private final TokenService tokenService;

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(UserResponse.from(userService.getByEmail(email)));
    }

    @PostMapping("/token")
    public ResponseEntity<AccessTokenResponse> createAccessToken(@RequestBody AccessTokenCreateRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(new AccessTokenResponse(newAccessToken));
    }

}
