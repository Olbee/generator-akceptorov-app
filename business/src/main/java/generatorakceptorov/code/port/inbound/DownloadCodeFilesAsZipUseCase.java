package generatorakceptorov.code.port.inbound;

import generatorakceptorov.domain.code.command.LanguagesToDownloadCodeCommand;
import generatorakceptorov.domain.code.data.CodeFilesZipData;

public interface DownloadCodeFilesAsZipUseCase {

    CodeFilesZipData execute(LanguagesToDownloadCodeCommand command);
}
