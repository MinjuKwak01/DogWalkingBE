package com.kakaoseventeen.dogwalking.review.controller;

import com.kakaoseventeen.dogwalking._core.utils.ApiResponse;
import com.kakaoseventeen.dogwalking._core.utils.ApiResponseGenerator;
import com.kakaoseventeen.dogwalking._core.utils.exception.walk.WalkNotExistException;
import com.kakaoseventeen.dogwalking._core.utils.exception.review.ReceiveMemberIdNotExistException;
import com.kakaoseventeen.dogwalking.review.dto.GetReviewResDTO;
import com.kakaoseventeen.dogwalking.review.dto.WriteReviewReqDTO;
import com.kakaoseventeen.dogwalking.review.service.ReviewReadService;
import com.kakaoseventeen.dogwalking.review.service.ReviewWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewWriteService reviewWriteService;
    private final ReviewReadService reviewReadService;

    @PostMapping("/review/{walkId}")
    public ResponseEntity<?> writeReview(@PathVariable("walkId") Long walkId, @RequestBody WriteReviewReqDTO writeReviewReqDTO) throws WalkNotExistException, ReceiveMemberIdNotExistException {

        reviewWriteService.writeReview(walkId, writeReviewReqDTO);

        return ApiResponseGenerator.success(HttpStatus.OK);

    }

    @GetMapping("/review/{memberId}")
    public ApiResponse<ApiResponse.CustomBody<GetReviewResDTO>> getReview(@PathVariable Long memberId){

        GetReviewResDTO response = reviewReadService.getReviewPreview(memberId);

        return ApiResponseGenerator.success(response, HttpStatus.OK);

    }
}
