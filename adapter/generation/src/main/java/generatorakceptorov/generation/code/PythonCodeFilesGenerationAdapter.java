package generatorakceptorov.generation.code;

import generatorakceptorov.code.port.outbound.PythonCodeGenerationPort;
import generatorakceptorov.domain.automaton.entity.MinDFAEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;

//TODO: rework
@Component
public class PythonCodeFilesGenerationAdapter extends AbstractCodeFilesGenerationAdapter implements PythonCodeGenerationPort {

    public static final String FOLDER_NAME = "PythonCodeFiles/";

    public static final String MAIN_FILE_NAME = FOLDER_NAME + "main.py";
    public static final String REGEX_ACCEPTOR_FILE_NAME = FOLDER_NAME + "regex_acceptor.py";

    @Override
    public HashMap<String, String> generateFromMinDFA(MinDFAEntity minDFA) {
        HashMap<String, String> generatedFiles = new HashMap<>();
        generatedFiles.put(MAIN_FILE_NAME, generateMainFileContent());
        generatedFiles.put(REGEX_ACCEPTOR_FILE_NAME, generateRegexAcceptorFileContent(minDFA));

        return generatedFiles;
    }

    private String generateMainFileContent() {
        return
            """
                from regex_acceptor import RegexAcceptor

                MAX_LEN = 64

                if __name__ == "__main__":
                \tstr_ = ""
                \twhile str_ != "?":
                \t\tprint(f"\\nPre ukončenie programu zadajte symbol \\\"?\\\".")
                \t\tstr_ = input("Zadajte vstupny retazec: ")
                \t\tif str_ != "?":
                \t\t\tif len(str_) > MAX_LEN:
                \t\t\t\tprint(f"Retazec je prilis dlhy (maximalna dlzka je {MAX_LEN}).")
                \t\t\t\tcontinue
                \t\t\tprint(f"\\nVstupny retazec: {str_}")
                \t\t\tprint(f"Dlzka retazca: {len(str_)}")

                \t\t\tregex_acceptor = RegexAcceptor()
                \t\t\tis_accepted = regex_acceptor.is_accepted(str_)
                \t\t\tprint(f"Stav: {'accepted' if is_accepted == regex_acceptor.ACCEPT else 'rejected'}")
            """;
    }

    private String generateRegexAcceptorFileContent(MinDFAEntity minDFA) {
        final StringBuilder sb = new StringBuilder();
        sb.append("class RegexAcceptor:\n");
        sb.append("\tACCEPT = 1\n");
        sb.append("\tNON_ACCEPT = -1\n");
        sb.append("\tMAX_LEN = 64\n");
        sb.append("\tUNDEF = -1\n\n");
        sb.append("\tdef __init__(self):\n");
        sb.append("\t\tself.current_state = 0\n\n");
        for (int state = 0; state < minDFA.stateCount(); state++) {
            sb.append("\tdef ").append(resolveStateName(state)).append("(self, current_char):\n");
            int numOfTransitions = 0;
            for (int transition = 0; transition < minDFA.alphabet().size(); transition++) {
                Integer nextState = minDFA.transitions()[state][transition];
                if (nextState != null) {
                    char transitionValue = minDFA.alphabet().get(transition);
                    String condition = numOfTransitions == 0 ? "if" : "elif";
                    sb.append("\t\t").append(condition).append(" current_char == '").append(transitionValue).append("':\n");
                    sb.append("\t\t\tself.current_state = ").append(nextState).append("\n");
                    numOfTransitions++;
                }
            }
            if (numOfTransitions == 0) {
                sb.append("\t\tself.current_state = self.UNDEF\n\n");

            } else {
                sb.append("\t\telse:\n");
                sb.append("\t\t\tself.current_state = self.UNDEF\n\n");
            }
        }

        final int[] acceptedStates = minDFA.acceptStates().stream()
                .mapToInt(Integer::intValue)
                .toArray();

        sb.append("\tdef is_accepted(self, in_str):\n");
        sb.append("\t\tlen_ = len(in_str)\n");
        sb.append("\t\tself.current_state = 0\n\n");
        sb.append("\t\tfor i in range(len_):\n");
        for (int state = 0; state < minDFA.stateCount(); state++) {
            if (state == 0) {
                sb.append("\t\t\tif self.current_state == ").append(state).append(":\n");
                sb.append("\t\t\t\tself.start(in_str[i])\n");
            } else {
                sb.append("\t\t\telif self.current_state == ").append(state).append(":\n");
                sb.append("\t\t\t\tself.q").append(state).append("(in_str[i])\n");
            }
        }
        if (acceptedStates.length == 0) {
            sb.append("\t\treturn self.ACCEPT\n");
        } else if (acceptedStates.length == 1) {
            sb.append("\t\treturn self.ACCEPT if self.current_state == ").append(acceptedStates[0]).append(" else self.NON_ACCEPT\n");
        } else {
            sb.append("\t\tif self.current_state in (");
            for (int stateCounter = 0; stateCounter < acceptedStates.length; stateCounter++) {
                sb.append(acceptedStates[stateCounter]);
                if (stateCounter + 1 != acceptedStates.length)
                    sb.append(", ");
            }
            sb.append("):\n");
            sb.append("\t\t\treturn self.ACCEPT\n");
            sb.append("\t\telse:\n");
            sb.append("\t\t\treturn self.NON_ACCEPT\n");
        }

        return sb.toString();
    }
}