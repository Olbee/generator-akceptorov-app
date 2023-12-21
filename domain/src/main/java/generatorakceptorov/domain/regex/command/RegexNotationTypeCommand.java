package generatorakceptorov.domain.regex.command;

import generatorakceptorov.domain.regex.NotationType;
import org.immutables.value.Value;

@Value.Immutable
public interface RegexNotationTypeCommand {

    @Value.Parameter
    NotationType notationType();
}
