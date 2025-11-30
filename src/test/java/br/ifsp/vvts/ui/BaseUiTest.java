package br.ifsp.vvts.ui;

import br.ifsp.vvts.security.user.JpaUserRepository;
import br.ifsp.vvts.security.user.Role;
import br.ifsp.vvts.security.user.User;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Duration;
import java.util.Locale;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseUiTest {

    protected WebDriver driver;
    protected Faker faker;

    protected static final String BASE_URL = "http://localhost:4200";
    protected static final Duration IMPLICIT_WAIT = Duration.ofSeconds(10);

    @LocalServerPort
    protected int port;

    @Autowired
    protected JpaUserRepository userRepository;

    @BeforeAll
    void setupFaker() {
        faker = new Faker(Locale.of("pt", "BR"));
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        userRepository.deleteAll();
    }

    protected User createTestUser(String email, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User(
                UUID.randomUUID(),
                faker.name().firstName(),
                faker.name().lastName(),
                email,
                encoder.encode(password),
                Role.USER
        );
        return userRepository.save(user);
    }

    protected String generateValidEmail() {
        return faker.internet().emailAddress();
    }

    protected String generateValidPassword() {
        return faker.internet().password(8, 16, true, true);
    }

    protected String generateInvalidEmail() {
        return faker.lorem().word();
    }

    protected String generateFirstName() {
        return faker.name().firstName();
    }

    protected String generateLastName() {
        return faker.name().lastName();
    }
}

