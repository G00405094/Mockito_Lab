package ie.atu.week8.projectexercise;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testSaveProductForApplyDiscount() {
        Product product = new Product(1L, "Laptop", "Expensive", 1500);
        when(productRepository.save(product)).thenReturn(product);
        Product expectedProduct = productService.saveProduct(product);
        assertEquals(1350, expectedProduct.getPrice());
    }

    @Test
    void testValidateProductFailure() {
        Product product = new Product(1L, "Laptop", "Expensive", -1500);
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> productService.saveProduct(product));
        assertEquals("Price cannot be negative", iae.getMessage());
    }

    @Test
    void testGetProductById() {
        Product product = new Product(1L, "Product 1", "Description", 100.0);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Product result = productService.getProductById(1L).orElse(null);
        assertNotNull(result);
        assertEquals("Product 1", result.getName());
    }
    @Test
    void testValidateProductForEmptyName() {
        Product product = new Product(1L, "", "hi", 890);
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> productService.saveProduct(product));
        assertEquals("Product name cannot be empty", iae.getMessage());
    }

}
