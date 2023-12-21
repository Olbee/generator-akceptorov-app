package generatorakceptorov.code.service;

import generatorakceptorov.code.port.inbound.DownloadCodeFilesAsZipUseCase;
import generatorakceptorov.code.port.outbound.CodeGenerationPort;
import generatorakceptorov.domain.code.command.LanguagesToDownloadCodeCommand;
import generatorakceptorov.domain.code.data.CodeFilesZipData;
import generatorakceptorov.domain.code.data.ImmutableCodeFilesZipData;
import generatorakceptorov.code.port.outbound.CCodeGenerationPort;
import generatorakceptorov.code.port.outbound.JavaCodeGenerationPort;
import generatorakceptorov.code.port.outbound.PythonCodeGenerationPort;
import generatorakceptorov.domain.regex.ProgrammingLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static generatorakceptorov.domain.code.error.CodeErrorCode.CODE_FILES_TO_ZIP_GENERATE_ERROR;
import static generatorakceptorov.domain.regex.ProgrammingLanguage.*;

@Component
public class DownloadCodeFilesAsZipService implements DownloadCodeFilesAsZipUseCase {

    final private Map<ProgrammingLanguage, CodeGenerationPort> codeGenerationPorts;

    @Autowired
    public DownloadCodeFilesAsZipService(
            CCodeGenerationPort CCodeGenerationPort,
            JavaCodeGenerationPort javaCodeGenerationPort,
            PythonCodeGenerationPort pythonCodeGenerationPort) {
        this.codeGenerationPorts =
                Map.of(C, CCodeGenerationPort,
                        JAVA, javaCodeGenerationPort,
                        PYTHON, pythonCodeGenerationPort);
    }

    @Override
    public CodeFilesZipData execute(LanguagesToDownloadCodeCommand command) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            command.languagesToDownloadCode().forEach(lang -> {
                try {
                    addFilesToZip(zos, codeGenerationPorts.get(lang).generateFromMinDFA(command.minDFA()));
                } catch (IOException error) {
                    throwCodeFilesToZipGenerateErrorError(error.getMessage());
                }
            });
        } catch (IOException error) {
            throwCodeFilesToZipGenerateErrorError(error.getMessage());
        }

        return ImmutableCodeFilesZipData.builder()
                .fileName("generated_codes.zip")
                .fileContent(baos.toByteArray())
                .build();
    }

    private void addFilesToZip(ZipOutputStream zos, HashMap<String, String> codeFiles) throws IOException {
        for (HashMap.Entry<String, String> cCodeFile : codeFiles.entrySet())
            addFileToZip(zos, cCodeFile.getValue().getBytes(), cCodeFile.getKey());
    }

    private void addFileToZip(ZipOutputStream zos, byte[] content, String fileName) throws IOException {
        ZipEntry entry = new ZipEntry(fileName);
        entry.setSize(content.length);
        zos.putNextEntry(entry);
        zos.write(content);
        zos.closeEntry();
    }

    private void throwCodeFilesToZipGenerateErrorError(String errorMessage) {
        throw CODE_FILES_TO_ZIP_GENERATE_ERROR
                .createError(errorMessage)
                .convertToException();
    }
}