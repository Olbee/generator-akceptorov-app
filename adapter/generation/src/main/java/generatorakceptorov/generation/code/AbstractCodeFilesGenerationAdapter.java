package generatorakceptorov.generation.code;

public abstract class AbstractCodeFilesGenerationAdapter {

    protected String resolveStateName(int state) {
        return state == 0 ? "start" : "q" + state;
    }
}
