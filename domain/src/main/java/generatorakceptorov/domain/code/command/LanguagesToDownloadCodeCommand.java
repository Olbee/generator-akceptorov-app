package generatorakceptorov.domain.code.command;

import generatorakceptorov.domain.automaton.entity.MinDFAEntity;
import generatorakceptorov.domain.regex.ProgrammingLanguage;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
public interface LanguagesToDownloadCodeCommand {

  MinDFAEntity minDFA();

  List<ProgrammingLanguage> languagesToDownloadCode();
}
