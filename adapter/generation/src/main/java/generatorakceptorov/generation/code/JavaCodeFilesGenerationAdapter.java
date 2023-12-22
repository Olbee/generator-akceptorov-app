package generatorakceptorov.generation.code;

import generatorakceptorov.code.port.outbound.JavaCodeGenerationPort;
import generatorakceptorov.domain.automaton.entity.MinDFAEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;

//TODO: rework
@Component
public class JavaCodeFilesGenerationAdapter extends AbstractCodeFilesGenerationAdapter implements JavaCodeGenerationPort {

    public static final String FOLDER_NAME = "JavaCodeFiles/";
    public static final String PACKAGE_NAME = FOLDER_NAME + "src/main/java/akceptor/";

    public static final String MAIN_FILE_NAME = PACKAGE_NAME + "Main.java";
    public static final String REGEX_ACCEPTOR_FILE_NAME = PACKAGE_NAME + "RegexAcceptor.java";

    @Override
    public HashMap<String, String> generateFromMinDFA(MinDFAEntity minDFA) {
        final HashMap<String, String> generatedFiles = new HashMap<>();
        generatedFiles.put(MAIN_FILE_NAME, generateMainFileContent());
        generatedFiles.put(REGEX_ACCEPTOR_FILE_NAME, generateRegexAcceptorFileContent(minDFA));

        return generatedFiles;
    }

    private String generateMainFileContent() {
        final StringBuilder sb = new StringBuilder();
        resolveCurrentPackage(sb);
        sb.append("import java.util.Scanner;\n\n");
        sb.append("import static ").append(PACKAGE_NAME).append(".RegexAcceptor.ACCEPT;\n");
        sb.append("import static ").append(PACKAGE_NAME).append(".RegexAcceptor.MAX_LEN;\n\n");
        sb.append("public class Main {\n");
        sb.append("\tpublic static void main(String[] args) {\n\n");
        sb.append("\t\tSystem.out.print(\"\\n\");\n");
        sb.append("\t\tScanner scanner = new Scanner(System.in);\n");
        sb.append("\t\tString str = \"\";\n");
        sb.append("\t\twhile (!str.equals(\"?\")) {\n");
        sb.append("\t\t\tSystem.out.print(\"\\nPre ukončenie programu zadajte symbol \\\"?\\\".\");\n");
        sb.append("\t\t\tSystem.out.print(\"\\nZadajte vstupny retazec: \");\n");
        sb.append("\t\t\tstr = scanner.nextLine();\n");
        sb.append("\t\t\tif (!str.equals(\"?\")) {\n");
        sb.append("\t\t\t\tif (str.length() > MAX_LEN) {\n");
        sb.append("\t\t\t\t\tSystem.out.println(\"Retazec je prilis dlhy (maximalna dlzka je 64).\");\n");
        sb.append("\t\t\t\t\tcontinue;\n");
        sb.append("\t\t\t\t}\n");
        sb.append("\t\t\t\tSystem.out.printf(\"\\nVstupny retazec: %s\\n\", str);\n");
        sb.append("\t\t\t\tSystem.out.printf(\"Dlzka retazca: %d\", str.length());\n\n");
        sb.append("\t\t\t\tRegexAcceptor regexAcceptor = new RegexAcceptor();\n");
        sb.append("\t\t\t\tSystem.out.printf(\"\\nStav: %s\\n\", regexAcceptor.isAccepted(str) == ACCEPT ? \"accepted\" : \"rejected\");\n");
        sb.append("\t\t\t}\n");
        sb.append("\t\t}\n");
        sb.append("\t}\n");
        sb.append("}");

        return sb.toString();
    }

    private String generateRegexAcceptorFileContent(MinDFAEntity minDFA) {
        final StringBuilder sb = new StringBuilder();
        resolveCurrentPackage(sb);
        sb.append("public class RegexAcceptor {\n\n");
        sb.append("\tpublic static final int ACCEPT = 1;\n");
        sb.append("\tpublic static final int NON_ACCEPT = -1;\n");
        sb.append("\tpublic static final int MAX_LEN = 64;\n");
        sb.append("\tpublic static final int UNDEF = -1;\n\n");
        sb.append("\tprivate int currentState = 0;\n\n");
        for (int state = 0; state < minDFA.stateCount(); state++) {
            sb.append("\tprivate void ").append(resolveStateName(state)).append("(char currentChar) {\n");
            int numOfTransitions = 0;
            for (int transition = 0; transition < minDFA.alphabet().size(); transition++) {
                Integer nextState = minDFA.transitions()[state][transition];
                if (nextState != null) {
                    char transitionValue = minDFA.alphabet().get(transition);
                    String condition = numOfTransitions == 0 ? "if" : "else if";
                    sb.append("\t\t").append(condition).append(" (currentChar == '").append(transitionValue).append("') {\n");
                    sb.append("\t\t\tcurrentState = ").append(nextState).append(";\n");
                    sb.append("\t\t}\n");
                    numOfTransitions++;
                }
            }
            if (numOfTransitions == 0) {
                sb.append("\t\tcurrentState = UNDEF;\n");
            } else {
                sb.append("\t\telse {\n");
                sb.append("\t\t\tcurrentState = UNDEF;\n");
                sb.append("\t\t}\n");
            }
            sb.append("\t}\n\n");
        }

        final int[] acceptedStates = minDFA.acceptStates().stream()
                .mapToInt(Integer::intValue)
                .toArray();

        sb.append("\tpublic int isAccepted(String inStr) {\n");
        sb.append("\t\tint len = inStr.length();\n");
        sb.append("\t\tcurrentState = 0;\n\n");
        sb.append("\t\tfor (int i = 0; i < len; i++) {\n");
        for (int state = 0; state < minDFA.stateCount(); state++) {
            if (state == 0) {
                sb.append("\t\t\tif (currentState == ").append(state).append(") {\n");
                sb.append("\t\t\t\tstart(inStr.charAt(i));\n");
            } else {
                sb.append("\t\t\telse if (currentState == ").append(state).append(") {\n");
                sb.append("\t\t\t\tq").append(state).append("(inStr.charAt(i));\n");
            }
            sb.append("\t\t\t}\n");
        }
        sb.append("\t\t}\n");
        if (acceptedStates.length == 0) {
            sb.append("\t\treturn ACCEPT;\n");
        } else if (acceptedStates.length == 1) {
            sb.append("\t\treturn (currentState == ").append(acceptedStates[0]).append(") ? ACCEPT : NON_ACCEPT;\n");
        } else {
            sb.append("\t\treturn (currentState == ").append(acceptedStates[0]).append("\n");
            for (int state = 1; state < acceptedStates.length; state++) {
                String lastState =
                        state + 1 == acceptedStates.length
                                ? ") ? ACCEPT : NON_ACCEPT;"
                                : "";
                sb.append("\t\t\t|| currentState == ").append(acceptedStates[state]).append(lastState).append("\n");
            }
        }
        sb.append("\t}\n");
        sb.append("}\n");

        return sb.toString();
    }

    private void resolveCurrentPackage(StringBuilder sb) {
        sb.append("package ").append(PACKAGE_NAME).append(";\n\n");
    }
}