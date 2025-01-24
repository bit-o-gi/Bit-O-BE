package bit.couple.controller;

import bit.couple.dto.CoupleCreateRequest;
import bit.couple.service.CoupleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/couple")
public class CoupleController {

    private final CoupleService coupleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCouple(@RequestBody CoupleCreateRequest coupleCreateRequest) {
        coupleService.createCouple(coupleCreateRequest.toCommand());
    }

    @PutMapping("/{code1}/{code2}")
    @ResponseStatus(HttpStatus.OK)
    public void approveCouple(@PathVariable String code1, @PathVariable String code2) {
        coupleService.approveCouple(code1, code2);
    }

    @DeleteMapping("/{coupleId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCouple(@PathVariable Long coupleId) {
        coupleService.deleteCouple(coupleId);
    }
}
