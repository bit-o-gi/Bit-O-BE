package bit.app.day.controller;

import bit.app.auth.domain.UserPrincipal;
import bit.app.day.dto.DayFileRequest;
import bit.app.day.dto.DayFileResponse;
import bit.app.day.dto.DayRegisterRequest;
import bit.app.day.dto.DayResponse;
import bit.app.day.dto.DayUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Day API", description = "커플 시작일 관련 API")
public interface DayControllerDoc {
    @Operation(summary = "시작일 조회 API", description = "Day PK로 시작일 정보 조회하는 API"
            , responses = {
    })
    ResponseEntity<DayResponse> getDay(UserPrincipal userPrincipal, Long id);

    @Operation(summary = "시작일 조회 API", description = "유저 ID로 시작일 정보 조회하는 API"
            , responses = {
    })
    ResponseEntity<DayResponse> getDayByCouple(UserPrincipal userPrincipal);

    @Operation(summary = "시작일 생성 API", description = "시작일 생성하는 API"
            , responses = {
            @ApiResponse(responseCode = "201", description = "생성 성공")
    })
    ResponseEntity<Long> createDay(DayRegisterRequest dayRegisterRequest);

    @Operation(summary = "썸네일 생성 API", description = "시작일 썸네일 등록하는 API"
            , responses = {
            @ApiResponse(responseCode = "201", description = "등록 성공")
    })
    ResponseEntity<DayFileResponse> uploadDayFile(UserPrincipal userPrincipal, DayFileRequest dayFileRequest);

    @Operation(summary = "시작일 수정 API", description = "시작일 수정하는 API"
            , responses = {
            @ApiResponse(responseCode = "200", description = "수정 성공")
    })
    ResponseEntity<Long> updateDay(UserPrincipal userPrincipal, DayUpdateRequest dayUpdateRequest);


    @Operation(summary = "시작일 삭제 API", description = "시작일 삭제하는 API"
            , responses = {
            @ApiResponse(responseCode = "204", description = "삭제 성공")
    })
    ResponseEntity<Void> deleteDay(UserPrincipal userPrincipal, Long id);
}
