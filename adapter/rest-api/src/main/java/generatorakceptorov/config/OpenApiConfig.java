package generatorakceptorov.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Generator Akceptorov REST API")
                .version("0.0.1")
                .license(
                    new License()
                        .name("Apache 2.0")
                        .url("https://opensource.org/licenses/Apache-2.0")))
        .components(new Components());
  }
}
