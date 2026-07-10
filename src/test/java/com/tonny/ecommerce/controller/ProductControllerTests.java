package com.tonny.ecommerce.controller;

import com.tonny.ecommerce.DTO.PostProductRequestDTO;
import com.tonny.ecommerce.DTO.PostProductResponseDTO;
import com.tonny.ecommerce.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import tools.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private ProductService productService;

    private final ObjectMapper json = new ObjectMapper();

    @Test
    @DisplayName("Must create product successfully and return 201 with object created")
    void mustCreateProductSuccessfully() throws UnsupportedEncodingException {
        PostProductRequestDTO request = new PostProductRequestDTO("title", "description", 10.0);
        String jsonRequest = json.writeValueAsString(request);

        when(productService.createProduct(request)).thenReturn(fakeResponse(request));

        MvcTestResult testResult = mvc.post().uri("/product/create-product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .exchange();

        PostProductResponseDTO response = json.readValue(testResult.getResponse().getContentAsString(), PostProductResponseDTO.class);

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.title()).isEqualTo(request.title());
        assertThat(response.description()).isEqualTo(request.description());
        assertThat(response.price()).isEqualTo(request.price());
        assertThat(response.reviews()).isEqualTo(0.0);
        assertThat(response.reviewsCount()).isEqualTo(0);
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.updatedAt()).isNull();
    }

    private PostProductResponseDTO fakeResponse(PostProductRequestDTO request) {
        return new PostProductResponseDTO(
                UUID.randomUUID(),
                request.title(),
                request.description(),
                0.0,
                0,
                request.price(),
                LocalDateTime.now(),
                null
        );
    }
}
