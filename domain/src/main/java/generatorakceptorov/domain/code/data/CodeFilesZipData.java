package generatorakceptorov.domain.code.data;

import org.immutables.value.Value;

@Value.Immutable
public interface CodeFilesZipData {

  String fileName();

  byte[] fileContent();
}
