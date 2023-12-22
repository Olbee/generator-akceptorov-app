package generatorakceptorov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {GeneratorAkceptorovApplication.class})
public class GeneratorAkceptorovApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeneratorAkceptorovApplication.class);
    }
}