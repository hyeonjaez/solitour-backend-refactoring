package solitour_backend.solitour.information.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import solitour_backend.solitour.error.Utils;
import solitour_backend.solitour.information.dto.request.InformationRegisterRequest;
import solitour_backend.solitour.information.dto.response.InformationResponse;
import solitour_backend.solitour.information.service.InformationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/informations")
public class InformationController {
    private final InformationService informationService;

    @PostMapping
    public ResponseEntity<InformationResponse> createInformation(@Valid @RequestPart("request") InformationRegisterRequest informationRegisterRequest,
                                                                 BindingResult bindingResult) {
        Utils.validationRequest(bindingResult);
        InformationResponse informationResponse = informationService.registerInformation(informationRegisterRequest);


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(informationResponse);
    }
}
