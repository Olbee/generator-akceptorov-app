package generatorakceptorov.domain.regex.entity;

import generatorakceptorov.domain.regex.NotationType;
import org.immutables.value.Value;

@Value.Immutable
public interface RegexEntity {

    @Value.Parameter
    String regex();

    @Value.Parameter
    NotationType notationType();
}

