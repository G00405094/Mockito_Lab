package ie.atu.week8.projectexercise;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(ProductController.class)
 class productControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;


    @Test
    void testGetProductById() throws Exception {
        Product product = new Product(1l, "Pot", "10Litres", 30);
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/1"))
            .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pot"));
    }

    @Test
    void testCreateProduct() throws Exception {
        Product product = new Product( null,"Pot","10 litres", 30);
        when(productService.saveProduct(any(Product.class))).thenReturn(product);

        ObjectMapper mapper = new ObjectMapper();
        String jsonDetails = mapper.writeValueAsString(product);

        mockMvc.perform(post("/products").contentType("application/json").content(jsonDetails))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Pot"));

    }

    @Test
    void testUpdateProduct() throws Exception {
        Product existingProduct = new Product(1L, "Pot", "10 litres", 30);
        Product updatedProduct = new Product(1L, "Updated Pot", "20 litres", 35);


        when(productService.getProductById(1L)).thenReturn(Optional.of(existingProduct));

        when(productService.saveProduct(any(Product.class))).thenReturn(updatedProduct);


        ObjectMapper mapper = new ObjectMapper();
        String jsonDetails = mapper.writeValueAsString(updatedProduct);


        mockMvc.perform(put("/products/1")
                        .contentType("application/json")
                        .content(jsonDetails))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Pot"))
                .andExpect(jsonPath("$.description").value("20 litres"))
                .andExpect(jsonPath("$.price").value(35));
    }

    @Test
    void testDeleteProduct() throws Exception {
        Product product = new Product(1L, "Pot", "10 litres", 30);


        when(productService.getProductById(1L)).thenReturn(Optional.of(product));


        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());
    }

}