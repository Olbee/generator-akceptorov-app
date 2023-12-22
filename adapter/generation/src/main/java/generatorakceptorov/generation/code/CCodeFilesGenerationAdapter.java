package generatorakceptorov.generation.code;

import generatorakceptorov.code.port.outbound.CCodeGenerationPort;
import generatorakceptorov.domain.automaton.entity.MinDFAEntity;
import java.util.HashMap;
import org.springframework.stereotype.Component;

// TODO: rework
@Component
public class CCodeFilesGenerationAdapter extends AbstractCodeFilesGenerationAdapter
    implements CCodeGenerationPort {

  public static final String FOLDER_NAME = "CCodeFiles/";

  public static final String MAIN_FILE_NAME = FOLDER_NAME + "main.c";
  public static final String REGEX_ACCEPTOR_H_FILE_NAME = FOLDER_NAME + "regexAcceptor.h";
  public static final String REGEX_ACCEPTOR_FILE_NAME = FOLDER_NAME + "regexAcceptor.c";
  public static final String MAKEFILE_FILE_NAME = FOLDER_NAME + "Makefile";

  @Override
  public HashMap<String, String> generateFromMinDFA(MinDFAEntity minDFA) {
    final HashMap<String, String> generatedFiles = new HashMap<>();
    generatedFiles.put(MAIN_FILE_NAME, generateMainFileContent());
    generatedFiles.put(REGEX_ACCEPTOR_H_FILE_NAME, generateRegexAcceptorHFileContent(minDFA));
    generatedFiles.put(REGEX_ACCEPTOR_FILE_NAME, generateRegexAcceptorFileContent(minDFA));
    generatedFiles.put(MAKEFILE_FILE_NAME, generateMakefileFileContent());

    return generatedFiles;
  }

  private String generateMainFileContent() {
    return """
                #include <stdio.h>
                #include <string.h>

                #include "regexAcceptor.h"

                int main() {
                \tprintf("\\n");
                \tchar str[MAX_LEN] = "";
                \twhile (strcmp(str, "?") != 0) {
                \t\tprintf("Pre ukončenie programu zadajte symbol \\"?\\".\\n");
                \t\tprintf("Zadajte vstupny retazec: ");
                \t\tfgets(str, MAX_LEN, stdin);
                \t\tstr[strcspn(str, "\\n")] = '\\0';
                \t\tif (strcmp(str, "?") != 0) {
                \t\t\tif (strlen(str) + 1 >= MAX_LEN) {
                \t\t\t\tprintf("Retazec je prilis dlhy (maximalna dlzka je %d).\\n\\n", MAX_LEN);
                \t\t\t\tstr[0] = '\\0';
                \t\t\t\tint c;
                \t\t\t\twhile ((c = getchar()) != '\\n' && c != EOF) {}
                \t\t\t\tcontinue;
                \t\t\t} else {
                \t\t\t\tprintf("\\nVstupny retazec: %s\\n", str);
                \t\t\t\tprintf("Dlzka retazca: %li\\n", strlen(str));
                \t\t\t\tprintf("Stav: %s\\n\\n", isAccepted(str) == ACCEPT ? "accepted" : "rejected");
                \t\t\t}
                \t\t} else {
                \t\t\tbreak;
                \t\t}
                \t}
                \treturn 0;
                }
            """;
  }

  private String generateRegexAcceptorHFileContent(MinDFAEntity minDFA) {
    final StringBuilder sb = new StringBuilder();
    sb.append("#ifndef _REGEXACCEPTOR_H\n");
    sb.append("#define _REGEXACCEPTOR_H\n\n");
    sb.append("#define ACCEPT 1\n");
    sb.append("#define NON_ACCEPT -1\n");
    sb.append("#define MAX_LEN 64\n");
    sb.append("#define UNDEF -1\n\n");

    for (int state = 0; state < minDFA.stateCount(); state++)
      sb.append("void ").append(resolveStateName(state)).append("(char currentChar);\n");

    sb.append("int isAccepted(char *inStr);\n\n");
    sb.append("#endif");

    return sb.toString();
  }

  private String generateRegexAcceptorFileContent(MinDFAEntity minDFA) {
    final StringBuilder sb = new StringBuilder();
    sb.append("#include <string.h>\n\n");
    sb.append("#include \"regexAcceptor.h\"\n\n");
    sb.append("int currentState = 0;\n\n");

    for (int state = 0; state < minDFA.stateCount(); state++) {
      sb.append("void ").append(resolveStateName(state)).append("(char currentChar) {\n");

      int numOfTransitions = 0;
      for (int transition = 0; transition < minDFA.alphabet().size(); transition++) {
        final Integer nextState = minDFA.transitions()[state][transition];
        if (nextState != null) {
          char transitionValue = minDFA.alphabet().get(transition);
          String condition = numOfTransitions == 0 ? "if" : "else if";
          sb.append("\t")
              .append(condition)
              .append(" (currentChar == '")
              .append(transitionValue)
              .append("') {\n");
          sb.append("\t\tcurrentState = ").append(nextState).append(";\n");
          sb.append("\t}\n");
          numOfTransitions++;
        }
      }

      if (numOfTransitions == 0) {
        sb.append("\tcurrentState = UNDEF;\n");
      } else {
        sb.append("\telse {\n");
        sb.append("\t\tcurrentState = UNDEF;\n");
        sb.append("\t}\n");
      }
      sb.append("}\n\n");
    }

    sb.append("int isAccepted(char *inStr) {\n");
    sb.append("\tint len = strlen(inStr);\n");
    sb.append("\tcurrentState = 0;\n\n");
    sb.append("\tfor (int i = 0; i < len; i++) {\n");
    for (int state = 0; state < minDFA.stateCount(); state++) {
      if (state == 0) {
        sb.append("\t\tif (currentState == ").append(state).append(") {\n");
        sb.append("\t\t\tstart(inStr[i]);\n");
      } else {
        sb.append("\t\telse if (currentState == ").append(state).append(") {\n");
        sb.append("\t\t\tq").append(state).append("(inStr[i]);\n");
      }
      sb.append("\t\t}\n");
    }
    sb.append("\t}\n");
    sb.append("\treturn (currentState != UNDEF) ? ACCEPT : NON_ACCEPT;\n");
    sb.append("}\n");

    return sb.toString();
  }

  private String generateMakefileFileContent() {
    return """
                CC=gcc
                CFLAGS=-std=c11 -Wall -Werror
                LDLIBS=-lm
                OUTPUT=regexAcceptor

                all: $(OUTPUT)

                $(OUTPUT): regexAcceptor.o main.o
                \tcppcheck --enable=performance,unusedFunction --error-exitcode=1 *.c
                \tgcc $(CFLAGS) regexAcceptor.o main.o $(LDLIBS) -o $(OUTPUT)

                main.o: main.c
                \tgcc $(CFLAGS) -c main.c -o main.o
                regexAcceptor.o: regexAcceptor.c regexAcceptor.h
                \tgcc $(CFLAGS) -c regexAcceptor.c -o regexAcceptor.o
                clean:
                \trm -rf $(OUTPUT) *.o
            """;
  }
}
