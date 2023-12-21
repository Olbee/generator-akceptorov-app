package generatorakceptorov.domain.code.command;

import generatorakceptorov.domain.automaton.entity.MinDFAEntity;
import generatorakceptorov.domain.regex.ProgrammingLanguage;
import org.immutables.value.Value;

import java.util.List;
import java.util.Map;

@Value.Immutable
public interface LanguagesToDownloadCodeCommand {

    MinDFAEntity minDFA();

    List<ProgrammingLanguage> languagesToDownloadCode();
}
