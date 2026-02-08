package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createProduct_newProductAppearsInProductList(ChromeDriver driver) {
        // Pura2 buat
        String productName = "Functional Test-" + UUID.randomUUID();
        String productQuantity = "12";

        // Aksi: Buat halaman baru
        driver.get(baseUrl + "/product/create");

        // Isi form, menyesuaikan id di CreateProduct.html
        driver.findElement(By.id("nameInput")).sendKeys(productName);

        WebElement qtyInput = driver.findElement(By.id("quantityInput"));
        qtyInput.clear();
        qtyInput.sendKeys(productQuantity);

        // Submit Create Product
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Tunggu sampai di redirect ke product list
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/product/list"),
                ExpectedConditions.titleContains("Product")
        ));

        // Di tes "Assert" product muncul di list table
        List<WebElement> rows = driver.findElements(By.cssSelector("tbody tr"));
        assertFalse(rows.isEmpty(), "Product list should contain at least one row after creation.");

        boolean found = false;
        for (WebElement row : rows) {
            List<WebElement> cols = row.findElements(By.cssSelector("td"));
            if (cols.size() >= 2) {
                String nameText = cols.get(0).getText().trim();
                String qtyText = cols.get(1).getText().trim();
                if (productName.equals(nameText) && productQuantity.equals(qtyText)) {
                    found = true;
                    break;
                }
            }
        }

        assertTrue(found, "Newly created product should be visible in the product list.");
    }
}
