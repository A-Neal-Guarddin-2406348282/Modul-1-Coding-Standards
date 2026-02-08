package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;
    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    // Update (Edit) Tests
    @Test
    void testUpdateProduct_Positive_WhenProductExists() {
        Product original = new Product();
        original.setProductId("id-1");
        original.setProductName("Old name");
        original.setProductQuantity(10);
        productRepository.create(original);

        Product updated = new Product();
        updated.setProductId("id-1");
        updated.setProductName("New Name");
        updated.setProductQuantity(99);

        Product result = productRepository.updateProduct(updated);

        assertNotNull(result);
        assertEquals("id-1", result.getProductId());
        assertEquals("New Name", result.getProductName());
        assertEquals(99, result.getProductQuantity());

        Product fromRepo = productRepository.findProductById("id-1");
        assertNotNull(fromRepo);
        assertEquals("New Name", fromRepo.getProductName());
        assertEquals(99, fromRepo.getProductQuantity());
    }

    @Test
    void testUpdateProduct_Negative_WhenProductDoesNotExist() {
        Product updateAttempt = new Product();
        updateAttempt.setProductId("missing-id");
        updateAttempt.setProductName("Doesn't matter");
        updateAttempt.setProductQuantity(1);

        Product result = productRepository.updateProduct(updateAttempt);

        assertNull(result);
    }

    @Test
    void testUpdateProduct_Negative_WhenProductIdIsNull() {
        Product updateAttempt = new Product();
        updateAttempt.setProductId(null);
        updateAttempt.setProductName("Name");
        updateAttempt.setProductQuantity(1);

        Product result = productRepository.updateProduct(updateAttempt);

        assertNull(result);
    }

    @Test
    void testUpdateProduct_Negative_WhenProductIdIsBlank() {
        Product updateAttempt = new Product();
        updateAttempt.setProductId("    ");
        updateAttempt.setProductName("Name");
        updateAttempt.setProductQuantity(1);

        Product result = productRepository.updateProduct(updateAttempt);

        assertNull(result);
    }

    // Delete Tests
    @Test
    void testDeleteProduct_Positive_WhenProductExists() {
        Product product = new Product();
        product.setProductId("id-del");
        product.setProductName("To Delete");
        product.setProductQuantity(5);
        productRepository.create(product);

        boolean deleted = productRepository.deleteProduct("id-del");

        assertTrue(deleted);
        assertNull(productRepository.findProductById("id-del"));

        Iterator<Product> it = productRepository.findAll();
        assertFalse(it.hasNext());
    }

    @Test
    void testDeleteProduct_Negative_WhenProductDoesNotExist() {
        boolean deleted = productRepository.deleteProduct("missing-id");
        assertFalse(deleted);
    }

    @Test
    void testDeleteProduct_Negative_WhenProductIdIsNull() {
        Product product = new Product();
        product.setProductId("id-stay");
        product.setProductName("Stay");
        product.setProductQuantity(1);
        productRepository.create(product);

        boolean deleted = productRepository.deleteProduct(null);

        assertFalse(deleted);
        assertNotNull(productRepository.findProductById("id-stay"));
    }
}
