package generatorakceptorov.graph.port.outbound;

public interface DotGraphTransformationPort {

  byte[] convertToPNG(String dot);
}
