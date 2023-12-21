package generatorakceptorov.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.RequestMapping;

public abstract class BaseController {

  public static final String BASE_V1_URI = "/v1/regex-to-acceptor";

  public static final String PARAM_REGEX = "regex";

  @Hidden
  @RequestMapping(BASE_V1_URI)
  public String index() {
    return "main";
  }
}
