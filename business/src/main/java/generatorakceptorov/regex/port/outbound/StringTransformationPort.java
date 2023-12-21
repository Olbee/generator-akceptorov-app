package generatorakceptorov.regex.port.outbound;

import generatorakceptorov.domain.regex.NotationType;
import generatorakceptorov.domain.regex.entity.RegexEntity;

public interface StringTransformationPort {

    RegexEntity transformToRegex(String regex, NotationType notationType);
}
