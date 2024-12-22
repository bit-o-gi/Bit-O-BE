package bit.user.oauth.kakao.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@Tag(name = "KaKao Login API", description = "카카오 로그인 API")
public interface KaKaoLoginControllerDoc {
    @Operation(summary = "카카오 로그인 요청 API", description = "카카오 provider api에 로그인 요청하는 API"
            , responses = {
            @ApiResponse(responseCode = "302", description = "카카오 로그인 페이지로 redirect")
    })
    void getAuthKakao(HttpServletResponse response) throws IOException;

    @Operation(
            summary = "카카오 토큰 발급 요청 API",
            description = "카카오 에서 발급 받은 인가 코드를 사용해 access token을 발급받는 API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "토큰 발급 성공")
            }
    )
    ResponseEntity<String> postAuthTokenToKakao(@RequestBody @Schema(description = "카카오에 요청하는 인가 코드") String code) throws JsonProcessingException;

    @Operation(
            summary = "카카오 사용자 정보 가져오기 API",
            description = "카카오에서 동의한 사용자 정보 가져오는 API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "정보 get 성공"),
                    @ApiResponse(responseCode = "400", description = "카카오 통신 중 문제가 발생"),
            }
    )
    ResponseEntity<?> postGetKaKaoUserInfo(@RequestBody String accessToken) throws JsonProcessingException;
}
