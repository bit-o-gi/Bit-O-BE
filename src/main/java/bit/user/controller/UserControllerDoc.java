package bit.user.controller;
import bit.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "User API", description = "유저 관련 API")
public interface UserControllerDoc {
    @Operation(summary = "이메일 조회 API", description = "이메일 주소로 유저 정보를 조회한다."
            , responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email);


}