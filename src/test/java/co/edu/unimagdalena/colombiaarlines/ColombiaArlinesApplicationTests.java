package co.edu.unimagdalena.colombiaarlines;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ColombiaArlinesApplicationTests {

    @Test
    void contextLoads() {
    }

}
