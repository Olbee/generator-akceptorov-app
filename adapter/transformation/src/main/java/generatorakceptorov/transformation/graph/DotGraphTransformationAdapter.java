package generatorakceptorov.transformation.graph;

import generatorakceptorov.graph.port.outbound.DotGraphTransformationPort;
import generatorakceptorov.transformation.error.TransformationErrorCode;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.*;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import jakarta.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class DotGraphTransformationAdapter implements DotGraphTransformationPort {

  private final Parser graphvizParser;

  @Inject
  public DotGraphTransformationAdapter(Parser graphvizParser) {
    this.graphvizParser = graphvizParser;
  }

  @Override
  public byte[] convertToPNG(String dot) {
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // Ability to generate very large graphs (it takes a long time and the default timeout is 10
    // seconds)
    try (GraphvizCmdLineEngine engine =
        new GraphvizCmdLineEngine().timeout(100000, TimeUnit.SECONDS)) {
      Graphviz.useEngine(engine);
      MutableGraph graph = graphvizParser.read(dot);
      graph.edges().forEach(edge -> edge.with(Style.DOTTED, Color.RED));
      Graphviz.fromGraph(graph).render(Format.PNG).toOutputStream(baos);
    } catch (IOException | GraphvizException error) {
      throw TransformationErrorCode.DOT_TO_PNG_CONVERT_ERROR
          .createError(error.getCause().getMessage())
          .convertToException();
    }

    return baos.toByteArray();
  }
}
