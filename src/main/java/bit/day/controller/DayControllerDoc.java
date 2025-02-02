package bit.day.controller;

import bit.auth.dto.AccessTokenCreateRequest;
import bit.auth.dto.AccessTokenResponse;
import bit.day.dto.DayRequest;
import bit.day.dto.DayResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Day API", description = "커플 시작일 관련 API")
public interface DayControllerDoc {
    @Operation(summary = "시작일 조회 API", description = "PK로 시작일 정보 조회하는 API"
            , responses = {
    })
    DayResponse getDay(@PathVariable Long id);

    @Operation(summary = "시작일 생성 API", description = "시작일 생성하는 API"
            , responses = {
            @ApiResponse(responseCode = "201", description = "생성 성공")
    })
    DayResponse createDay(@Valid @RequestBody DayRequest dayRequest);

    @Operation(summary = "시작일 수정 API", description = "시작일 수정하는 API"
            , responses = {
            @ApiResponse(responseCode = "200", description = "수정 성공")
    })
    DayResponse updateDay(@PathVariable Long id,
                          @Valid @RequestBody DayRequest dayRequest);


    @Operation(summary = "시작일 삭제 API", description = "시작일 삭제하는 API"
            , responses = {
            @ApiResponse(responseCode = "204", description = "삭제 성공")
    })
    void deleteDay(@PathVariable Long id);
}
