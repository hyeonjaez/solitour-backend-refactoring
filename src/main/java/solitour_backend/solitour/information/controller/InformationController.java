package solitour_backend.solitour.information.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import solitour_backend.solitour.auth.support.CookieExtractor;
import solitour_backend.solitour.auth.support.JwtTokenProvider;
import solitour_backend.solitour.auth.config.Authenticated;
import solitour_backend.solitour.error.Utils;
import solitour_backend.solitour.information.dto.request.InformationModifyRequest;
import solitour_backend.solitour.information.dto.request.InformationRegisterRequest;
import solitour_backend.solitour.information.dto.response.InformationBriefResponse;
import solitour_backend.solitour.information.dto.response.InformationDetailResponse;
import solitour_backend.solitour.information.dto.response.InformationRankResponse;
import solitour_backend.solitour.information.dto.response.InformationResponse;
import solitour_backend.solitour.information.service.InformationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/informations")
public class InformationController {

    private final InformationService informationService;
    public static final int PAGE_SIZE = 12;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping
    @Authenticated
    public ResponseEntity<InformationResponse> createInformation(@Valid @RequestPart("request") InformationRegisterRequest informationRegisterRequest,
                                                                 @RequestPart("thumbNailImage") MultipartFile thumbnail,
                                                                 @RequestPart("contentImages") List<MultipartFile> contentImages,
                                                                 BindingResult bindingResult) {
        Utils.validationRequest(bindingResult);
        InformationResponse informationResponse = informationService.registerInformation(
                informationRegisterRequest, thumbnail, contentImages);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(informationResponse);
    }

    @GetMapping("/{informationId}")
    public ResponseEntity<InformationDetailResponse> getDetailInformation(@PathVariable Long informationId) {
        InformationDetailResponse informationDetailResponse = informationService.getDetailInformation(
                informationId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(informationDetailResponse);
    }

    @Authenticated
    @PutMapping("/{informationId}")
    public ResponseEntity<InformationResponse> modifyInformation(@PathVariable Long informationId,
                                                                 @RequestPart(value = "thumbNailImage", required = false) MultipartFile thumbnail,
                                                                 @RequestPart(value = "contentImages", required = false) List<MultipartFile> contentImages,
                                                                 @Valid @RequestPart("request") InformationModifyRequest informationModifyRequest,
                                                                 BindingResult bindingResult) {
        Utils.validationRequest(bindingResult);

        InformationResponse informationResponse = informationService.modifyInformation(
                informationId, informationModifyRequest, thumbnail, contentImages);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(informationResponse);
    }

    @Authenticated
    @DeleteMapping("/{informationId}")
    public ResponseEntity<Void> deleteInformation(@PathVariable Long informationId) {
        informationService.deleteInformation(informationId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    //default
    @GetMapping("/parent-category/{parentCategoryId}")
    public ResponseEntity<Page<InformationBriefResponse>> pageInformationByParentCategory(@RequestParam(defaultValue = "0") int page,
                                                                                          @PathVariable("parentCategoryId") Long categoryId,
                                                                                          HttpServletRequest request) {

        Long userId = findUser(request);

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<InformationBriefResponse> briefInformationPage = informationService.getBriefInformationPageByParentCategory(pageable, categoryId, userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(briefInformationPage);
    }

    @GetMapping("/child-category/{childCategoryId}")
    public ResponseEntity<Page<InformationBriefResponse>> pageInformationByChildCategory(@RequestParam(defaultValue = "0") int page,
                                                                                         @PathVariable("childCategoryId") Long categoryId,
                                                                                         HttpServletRequest request) {
        Long userId = findUser(request);

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<InformationBriefResponse> briefInformationPage = informationService.getBriefInformationPageByChildCategory(pageable, categoryId, userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(briefInformationPage);
    }


    //좋아요순
    @GetMapping("/parent-category/{parentCategoryId}/like-count")
    public ResponseEntity<Page<InformationBriefResponse>> pageInformationByParentCategoryFilterLikeCount(@RequestParam(defaultValue = "0") int page,
                                                                                                         @PathVariable("parentCategoryId") Long categoryId,
                                                                                                         HttpServletRequest request) {
        Long userId = findUser(request);

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<InformationBriefResponse> briefInformationPage = informationService.getBriefInformationPageByParentCategoryFilterLikeCount(pageable, categoryId, userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(briefInformationPage);
    }

    @GetMapping("/child-category/{childCategoryId}/like-count")
    public ResponseEntity<Page<InformationBriefResponse>> pageInformationByChildCategoryFilterLikeCount(@RequestParam(defaultValue = "0") int page,
                                                                                                        @PathVariable("childCategoryId") Long categoryId,
                                                                                                        HttpServletRequest request) {
        Long userId = findUser(request);

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<InformationBriefResponse> briefInformationPage = informationService.getBriefInformationPageByChildCategoryFilterLikeCount(pageable, categoryId, userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(briefInformationPage);
    }


    //조회순
    @GetMapping("/parent-category/{parentCategoryId}/view-count")
    public ResponseEntity<Page<InformationBriefResponse>> pageInformationByParentCategoryViewCount(@RequestParam(defaultValue = "0") int page,
                                                                                                   @PathVariable("parentCategoryId") Long categoryId,
                                                                                                   HttpServletRequest request) {

        Long userId = findUser(request);

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<InformationBriefResponse> briefInformationPage = informationService.getBriefInformationPageByParentCategoryFilterViewCount(pageable, categoryId, userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(briefInformationPage);
    }

    @GetMapping("/child-category/{childCategoryId}/view-count")
    public ResponseEntity<Page<InformationBriefResponse>> pageInformationByChildCategoryViewCount(@RequestParam(defaultValue = "0") int page,
                                                                                                  @PathVariable("childCategoryId") Long categoryId,
                                                                                                  HttpServletRequest request) {
        Long userId = findUser(request);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<InformationBriefResponse> briefInformationPage = informationService.getBriefInformationPageByChildCategoryFilterViewCount(pageable, categoryId, userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(briefInformationPage);
    }

    //지역카테고리별
    @GetMapping("/parent-category/{parentCategoryId}/zone-category/{zoneCategory}")
    public ResponseEntity<Page<InformationBriefResponse>> pageInformationByParentCategoryAndZoneCategory(@RequestParam(defaultValue = "0") int page,
                                                                                                         @PathVariable("parentCategoryId") Long categoryId,
                                                                                                         @PathVariable("zoneCategory") Long zoneCategoryId,
                                                                                                         HttpServletRequest request) {
        Long userId = findUser(request);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<InformationBriefResponse> briefInformationPage = informationService.getBriefInformationPageByParentCategoryAndZoneCategory(pageable, categoryId, userId, zoneCategoryId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(briefInformationPage);
    }

    @GetMapping("/child-category/{childCategoryId}/zone-category/{zoneCategory}")
    public ResponseEntity<Page<InformationBriefResponse>> pageInformationChildCategoryAndZoneCategory(@RequestParam(defaultValue = "0") int page,
                                                                                                      @PathVariable("childCategoryId") Long categoryId,
                                                                                                      @PathVariable("zoneCategory") Long zoneCategoryId,
                                                                                                      HttpServletRequest request) {
        Long userId = findUser(request);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<InformationBriefResponse> briefInformationPage = informationService.getBriefInformationPageByChildCategoryAndZoneCategory(pageable, categoryId, userId, zoneCategoryId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(briefInformationPage);
    }


    @GetMapping("/ranks")
    public ResponseEntity<List<InformationRankResponse>> rankInformation() {
        List<InformationRankResponse> rankInformation = informationService.getRankInformation();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(rankInformation);
    }


    private Long findUser(HttpServletRequest request) {
        String token = CookieExtractor.findToken("access_token", request.getCookies());

        if (Objects.isNull(token)) {
            token = CookieExtractor.findToken("refresh_token", request.getCookies());
        }
        if (Objects.isNull(token)) {
            return (long) 0;
        }

        return jwtTokenProvider.getPayload(token);
    }

}

