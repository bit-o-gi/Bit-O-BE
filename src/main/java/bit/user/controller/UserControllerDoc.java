package bit.user.controller;
import bit.user.dto.AccessTokenCreateRequest;
import bit.user.dto.AccessTokenResponse;
import bit.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User API", description = "유저 관련 API")
public interface UserControllerDoc {
    @Operation(summary = "이메일 조회 API", description = "이메일 주소로 유저 정보를 조회한다."
            , responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email);

    @Operation(summary = "엑세스 토큰 발급 API", description = "리프레시 토큰을 보내 액세스 토큰을 발급한다."
            , responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<AccessTokenResponse> createAccessToken(@RequestBody AccessTokenCreateRequest request);
}