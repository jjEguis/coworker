package co.edu.unimagdalena.colombiaarlines;

import org.springframework.boot.SpringApplication;

public class TestColombiaArlinesApplication {

    public static void main(String[] args) {
        SpringApplication.from(ColombiaArlinesApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
