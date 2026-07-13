package com.tonny.ecommerce.controller;

import com.tonny.ecommerce.DTO.PatchProductRequestDTO;
import com.tonny.ecommerce.DTO.PostProductRequestDTO;
import com.tonny.ecommerce.DTO.ProductDTO;
import com.tonny.ecommerce.entity.Product;
import com.tonny.ecommerce.service.ProductService;
import com.tonny.ecommerce.utils.ProductTestsUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.util.List;

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

        when(productService.createProduct(request)).thenReturn(ProductTestsUtils.fakeResponse(request));

        MvcTestResult testResult = mvc.post().uri("/product/create-product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .exchange();

        ProductDTO response = json.readValue(testResult.getResponse().getContentAsString(), ProductDTO.class);

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.title()).isEqualTo(request.title());
        assertThat(response.description()).isEqualTo(request.description());
        assertThat(response.price()).isEqualTo(request.price());
        assertThat(response.reviews()).isEqualTo(0.0);
        assertThat(response.reviewsCount()).isEqualTo(0);
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.updatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Must return all products successfully")
    void mustReturnAllProductsSuccessfully() throws UnsupportedEncodingException {
        when(productService.getAllProducts()).thenReturn(ProductTestsUtils.fakeResponseList());

        MvcTestResult testResult = mvc.get().uri("/product/get-all-products")
                .exchange();

        List<ProductDTO> response = json.readValue(testResult.getResponse().getContentAsString(), new TypeReference<List<ProductDTO>>() {});

        assertThat(response).isNotNull();
        assertThat(response).hasSize(3);
    }

    @Test
    @DisplayName("Must patch product with new title")
    void mustPatchProductWithNewTitle() throws UnsupportedEncodingException {
        Product product = ProductTestsUtils.fakeEntity();
        String jsonRequest = json.writeValueAsString(new PatchProductRequestDTO("new title", null, null));
        ProductDTO edited = ProductTestsUtils.fakeResponse(new PostProductRequestDTO("new title", "description", 10.0));

        when(productService.patchProduct(product.getId(), new PatchProductRequestDTO("new title", null, null))).thenReturn(edited);

        MvcTestResult testResult = mvc.patch().uri("/product/patch-product/{id}", product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .exchange();

        ProductDTO response = json.readValue(testResult.getResponse().getContentAsString(), ProductDTO.class);

        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo(edited.title());
    }
}
