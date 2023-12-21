package generatorakceptorov.domain.regex.data;

import org.immutables.value.Value;

@Value.Immutable
public interface RegexData {

    @Value.Parameter
    String regex();
}