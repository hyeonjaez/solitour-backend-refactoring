package solitour_backend.solitour.zone_category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import solitour_backend.solitour.zone_category.dto.request.ZoneCategoryModifyRequest;
import solitour_backend.solitour.zone_category.dto.request.ZoneCategoryRegisterRequest;
import solitour_backend.solitour.zone_category.dto.response.ZoneCategoryResponse;
import solitour_backend.solitour.zone_category.service.ZoneCategoryService;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ZoneCategoryController.class)
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class ZoneCategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ZoneCategoryService zoneCategoryService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(modifyUris(), prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }


    @Test
    @DisplayName("ZoneCategory read 테스트")
    void getZoneCategoryTest() throws Exception {
        ZoneCategoryResponse zoneCategoryResponse = new ZoneCategoryResponse(2, "서울");

        when(zoneCategoryService.getZoneCategoryById(any(Integer.class))).thenReturn(zoneCategoryResponse);

        mockMvc.perform(get("/api/zoneCategories/{id}", zoneCategoryResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(zoneCategoryResponse.getId()))
                .andExpect(jsonPath("$.name").value(zoneCategoryResponse.getName()))
                .andDo(document("ZoneCategory-get-byId",
                        pathParameters(parameterWithName("id").description("조회할 ZoneCategory ID")),
                        responseFields(
                                fieldWithPath("id").description("ZoneCategory 아이디"),
                                fieldWithPath("name").description("ZoneCategory 이름")
                        )));

        verify(zoneCategoryService, times(1)).getZoneCategoryById(any(Integer.class));
    }

    @Test
    @DisplayName("ZoneCategory register 테스트")
    void registerZoneCategoryTest() throws Exception {
        ZoneCategoryResponse zoneCategoryResponse = new ZoneCategoryResponse(2, "서울");
        ZoneCategoryRegisterRequest zoneCategoryRegisterRequest = new ZoneCategoryRegisterRequest();

        ReflectionTestUtils.setField(zoneCategoryRegisterRequest, "id", 2);
        ReflectionTestUtils.setField(zoneCategoryRegisterRequest, "name", "서울");

        when(zoneCategoryService.registerZoneCategory(any(ZoneCategoryRegisterRequest.class))).thenReturn(zoneCategoryResponse);

        mockMvc.perform(post("/api/zoneCategories")
                        .content(new ObjectMapper().writeValueAsString(zoneCategoryRegisterRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(zoneCategoryResponse.getId()))
                .andExpect(jsonPath("$.name").value(zoneCategoryResponse.getName()))
                .andDo(document("ZoneCategory-create",
                        requestFields(
                                fieldWithPath("id").description("ZoneCategory 아이디"),
                                fieldWithPath("name").description("ZoneCategory 이름")
                        ),
                        responseFields(
                                fieldWithPath("id").description("ZoneCategory 아이디"),
                                fieldWithPath("name").description("ZoneCategory 이름")
                        )));

        verify(zoneCategoryService, times(1)).registerZoneCategory(any(ZoneCategoryRegisterRequest.class));
    }

    @Test
    @DisplayName("post 요청으로 들어온 데이터의 id 값의 유효성을 지키지 않은 경우 - NotNull")
    void registerZoneCategoryValidationExceptionByIdNotNullTest() throws Exception {
        ZoneCategoryRegisterRequest zoneCategoryRegisterRequest = new ZoneCategoryRegisterRequest();

        ReflectionTestUtils.setField(zoneCategoryRegisterRequest, "id", null);
        ReflectionTestUtils.setField(zoneCategoryRegisterRequest, "name", "서울");

        mockMvc.perform(post("/api/zoneCategories")
                        .content(new ObjectMapper().writeValueAsString(zoneCategoryRegisterRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("ZoneCategory-create-error-id-NotNull"));

        verify(zoneCategoryService, never()).registerZoneCategory(any(ZoneCategoryRegisterRequest.class));
    }

    @Test
    @DisplayName("post 요청으로 들어온 데이터의 id 값의 유효성을 지키지 않은 경우 - min 1")
    void registerZoneCategoryValidationExceptionByIdMinTest() throws Exception {
        ZoneCategoryRegisterRequest zoneCategoryRegisterRequest = new ZoneCategoryRegisterRequest();

        ReflectionTestUtils.setField(zoneCategoryRegisterRequest, "id", 0);
        ReflectionTestUtils.setField(zoneCategoryRegisterRequest, "name", "서울");

        mockMvc.perform(post("/api/zoneCategories")
                        .content(new ObjectMapper().writeValueAsString(zoneCategoryRegisterRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("ZoneCategory-create-error-id-NotNull"));

        verify(zoneCategoryService, never()).registerZoneCategory(any(ZoneCategoryRegisterRequest.class));
    }

    @Test
    @DisplayName("post 요청으로 들어온 데이터의 name 값의 유효성을 지키지 않은 경우 - NotBlank")
    void registerZoneCategoryValidationExceptionByNameNotBlankTest() throws Exception {
        ZoneCategoryRegisterRequest zoneCategoryRegisterRequest = new ZoneCategoryRegisterRequest();

        ReflectionTestUtils.setField(zoneCategoryRegisterRequest, "id", 1);
        ReflectionTestUtils.setField(zoneCategoryRegisterRequest, "name", "");
        mockMvc.perform(post("/api/zoneCategories")
                        .content(new ObjectMapper().writeValueAsString(zoneCategoryRegisterRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("ZoneCategory-create-error-name-NotBlank"));

        verify(zoneCategoryService, never()).registerZoneCategory(any(ZoneCategoryRegisterRequest.class));
    }

    @Test
    @DisplayName("post 요청으로 들어온 데이터의 name 값의 유효성을 지키지 않은 경우 - NotNull")
    void registerZoneCategoryValidationExceptionByNameNotNullTest() throws Exception {
        ZoneCategoryRegisterRequest zoneCategoryRegisterRequest = new ZoneCategoryRegisterRequest();

        ReflectionTestUtils.setField(zoneCategoryRegisterRequest, "id", 1);
        ReflectionTestUtils.setField(zoneCategoryRegisterRequest, "name", null);
        mockMvc.perform(post("/api/zoneCategories")
                        .content(new ObjectMapper().writeValueAsString(zoneCategoryRegisterRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("ZoneCategory-create-error-name-NotBlank"));

        verify(zoneCategoryService, never()).registerZoneCategory(any(ZoneCategoryRegisterRequest.class));
    }

    @Test
    @DisplayName("post 요청으로 들어온 데이터의 name 값의 유효성을 지키지 않은 경우 - max size 20")
    void registerZoneCategoryValidationExceptionByNameMaxSizeTest() throws Exception {
        ZoneCategoryRegisterRequest zoneCategoryRegisterRequest = new ZoneCategoryRegisterRequest();

        ReflectionTestUtils.setField(zoneCategoryRegisterRequest, "id", 1);
        ReflectionTestUtils.setField(zoneCategoryRegisterRequest, "name", "qwerasdfzxcvtyuighjkb");

        mockMvc.perform(post("/api/zoneCategories")
                        .content(new ObjectMapper().writeValueAsString(zoneCategoryRegisterRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("ZoneCategory-create-error-name-max-size-20"));

        verify(zoneCategoryService, never()).registerZoneCategory(any(ZoneCategoryRegisterRequest.class));
    }

    @Test
    @DisplayName("ZoneCategory update 테스트")
    void updateZoneCategoryTest() throws Exception {
        ZoneCategoryResponse zoneCategoryResponse = new ZoneCategoryResponse(2, "서울");

        ZoneCategoryModifyRequest zoneCategoryModifyRequest = new ZoneCategoryModifyRequest();
        ReflectionTestUtils.setField(zoneCategoryModifyRequest, "name", "서울");

        when(zoneCategoryService.modifyZoneCategory(any(Integer.class), any(ZoneCategoryModifyRequest.class))).thenReturn(zoneCategoryResponse);

        mockMvc.perform(put("/api/zoneCategories/{id}", 2)
                        .content(new ObjectMapper().writeValueAsString(zoneCategoryModifyRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(zoneCategoryResponse.getId()))
                .andExpect(jsonPath("$.name").value(zoneCategoryResponse.getName()))
                .andDo(document("ZoneCategory-update-success",
                        requestFields(
                                fieldWithPath("name").description("ZoneCategory 이름")
                        ),
                        responseFields(
                                fieldWithPath("id").description("ZoneCategory 아이디"),
                                fieldWithPath("name").description("ZoneCategory 이름")
                        )));

        verify(zoneCategoryService, times(1)).modifyZoneCategory(any(Integer.class), any(ZoneCategoryModifyRequest.class));
    }

    @Test
    @DisplayName("put 요청으로 들어온 데이터의 name 값의 유효성을 지키지 않은 경우 - NotBlank")
    void updateZoneCategoryValidationExceptionByNameNotBlankTest() throws Exception {
        ZoneCategoryModifyRequest zoneCategoryModifyRequest = new ZoneCategoryModifyRequest();

        ReflectionTestUtils.setField(zoneCategoryModifyRequest, "name", "");

        mockMvc.perform(put("/api/zoneCategories/{id}", 2)
                        .content(new ObjectMapper().writeValueAsString(zoneCategoryModifyRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("ZoneCategory-create-error-name-NotBlank"));

        verify(zoneCategoryService, never()).modifyZoneCategory(any(Integer.class), any(ZoneCategoryModifyRequest.class));
    }

    @Test
    @DisplayName("put 요청으로 들어온 데이터의 name 값의 유효성을 지키지 않은 경우 - NotNull")
    void updateZoneCategoryValidationExceptionByNameNotNullTest() throws Exception {
        ZoneCategoryModifyRequest zoneCategoryModifyRequest = new ZoneCategoryModifyRequest();

        ReflectionTestUtils.setField(zoneCategoryModifyRequest, "name", "");

        mockMvc.perform(put("/api/zoneCategories/{id}", 2)
                        .content(new ObjectMapper().writeValueAsString(zoneCategoryModifyRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("ZoneCategory-create-error-name-NotBlank"));

        verify(zoneCategoryService, never()).modifyZoneCategory(any(Integer.class), any(ZoneCategoryModifyRequest.class));
    }

    @Test
    @DisplayName("put 요청으로 들어온 데이터의 name 값의 유효성을 지키지 않은 경우 - max size 20")
    void updateZoneCategoryValidationExceptionByNameMaxSizeTest() throws Exception {
        ZoneCategoryModifyRequest zoneCategoryModifyRequest = new ZoneCategoryModifyRequest();

        ReflectionTestUtils.setField(zoneCategoryModifyRequest, "name", "qwerasdfzxcvtyuighjkb");

        mockMvc.perform(put("/api/zoneCategories/{id}", 2)
                        .content(new ObjectMapper().writeValueAsString(zoneCategoryModifyRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("ZoneCategory-create-error-name-NotBlank"));

        verify(zoneCategoryService, never()).modifyZoneCategory(any(Integer.class), any(ZoneCategoryModifyRequest.class));
    }

    @Test
    @DisplayName("ZoneCategory delete 테스트")
    void deleteZoneCategoryTest() throws Exception {

        mockMvc.perform(delete("/api/zoneCategories/{id}", 2))
                .andExpect(status().isNoContent())
                .andDo(document("ZoneCategory-delete-success",
                        pathParameters(
                                parameterWithName("id").description("ZoneCategory 아이디")
                        )));

        verify(zoneCategoryService, times(1)).deleteZoneCategory(any(Integer.class));
    }


}