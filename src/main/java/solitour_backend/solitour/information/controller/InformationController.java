package solitour_backend.solitour.information.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import solitour_backend.solitour.auth.config.AuthenticationPrincipal;
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

    @PostMapping
    public ResponseEntity<InformationResponse> createInformation(@Valid @RequestPart("request") InformationRegisterRequest informationRegisterRequest,
                                                                 @RequestPart("thumbNailImage") MultipartFile thumbnail,
                                                                 @RequestPart("contentImages") List<MultipartFile> contentImages,
                                                                 BindingResult bindingResult) {
        Utils.validationRequest(bindingResult);
        InformationResponse informationResponse = informationService.registerInformation(informationRegisterRequest, thumbnail, contentImages);


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(informationResponse);
    }

    @GetMapping("/{informationId}")
    public ResponseEntity<InformationDetailResponse> getDetailInformation(@PathVariable Long informationId) {
        InformationDetailResponse informationDetailResponse = informationService.getDetailInformation(informationId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(informationDetailResponse);
    }

    @PutMapping("/{informationId}")
    public ResponseEntity<InformationResponse> modifyInformation(@PathVariable Long informationId,
                                                                 @RequestPart(value = "thumbNailImage", required = false) MultipartFile thumbnail,
                                                                 @RequestPart(value = "contentImages", required = false) List<MultipartFile> contentImages,
                                                                 @Valid @RequestPart("request") InformationModifyRequest informationModifyRequest,
                                                                 BindingResult bindingResult) {
        Utils.validationRequest(bindingResult);

        InformationResponse informationResponse = informationService.modifyInformation(informationId, informationModifyRequest, thumbnail, contentImages);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(informationResponse);
    }

    @DeleteMapping("/{informationId}")
    public ResponseEntity<Void> deleteInformation(@PathVariable Long informationId) {
        informationService.deleteInformation(informationId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


    @GetMapping("/parent-category/{parentCategoryId}")
    public ResponseEntity<Page<InformationBriefResponse>> pageInformationByParentCategory(@RequestParam(defaultValue = "0") int page,
                                                                                          @PathVariable("parentCategoryId") Long categoryId) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<InformationBriefResponse> briefInformationPage = informationService.getBriefInformationPageByParentCategory(pageable, categoryId);

        return ResponseEntity.status(HttpStatus.OK).body(briefInformationPage);
    }

    @GetMapping("/users/parent-category/{parentCategoryId}")
    public ResponseEntity<Page<InformationBriefResponse>> pageInformationByParentCategory(@RequestParam(defaultValue = "0") int page,
                                                                                          @PathVariable("parentCategoryId") Long categoryId,
                                                                                          @AuthenticationPrincipal Long userId) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<InformationBriefResponse> briefInformationPage = informationService.getBriefInformationPageFromUserByParentCategory(pageable, categoryId, userId);

        return ResponseEntity.status(HttpStatus.OK).body(briefInformationPage);
    }

    @GetMapping("/child-category/{childCategoryId}")
    public ResponseEntity<Page<InformationBriefResponse>> pageInformationByChildCategory(@RequestParam(defaultValue = "0") int page,
                                                                                         @PathVariable("childCategoryId") Long categoryId) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<InformationBriefResponse> briefInformationPage = informationService.getBriefInformationPageByChildCategory(pageable, categoryId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(briefInformationPage);
    }

    @GetMapping("/users/child-category/{childCategoryId}")
    public ResponseEntity<Page<InformationBriefResponse>> pageInformationByChildCategory(@RequestParam(defaultValue = "0") int page,
                                                                                         @PathVariable("childCategoryId") Long categoryId,
                                                                                         @AuthenticationPrincipal Long userId) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<InformationBriefResponse> briefInformationPage = informationService.getBriefInformationPageFromUserByChildCategory(pageable, categoryId, userId);

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
}

