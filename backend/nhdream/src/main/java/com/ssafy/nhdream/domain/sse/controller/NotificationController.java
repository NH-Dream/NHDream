package com.ssafy.nhdream.domain.sse.controller;

import com.ssafy.nhdream.common.response.BaseResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
@Tag(name = "9. NOTIFICATION", description = "NOTIFICATION API")
public class NotificationController {

    @PostMapping(value = "/test", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<? extends BaseResponseBody> test(
            @RequestParam(name = "test")
            MultipartFile file
    ) {



        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, 0));
    }

}
