package generatorakceptorov.conf;

import guru.nidi.graphviz.parse.Parser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

  @Bean
  public Parser graphvizParser() {
    return new Parser();
  }
}
