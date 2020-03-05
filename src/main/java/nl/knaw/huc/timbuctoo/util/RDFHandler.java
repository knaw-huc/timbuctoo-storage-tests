package nl.knaw.huc.timbuctoo.util;

import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;

import java.util.function.Supplier;

import static nl.knaw.huc.timbuctoo.util.RdfConstants.LANGSTRING;

public abstract class RDFHandler extends AbstractRDFHandler {
    private static final int ADD = '+';
    private final String defaultGraph;
    private Supplier<Integer> actionSupplier;

    public RDFHandler(String defaultGraph) {
        this.defaultGraph = defaultGraph;
    }

    public void registerActionSupplier(Supplier<Integer> actionSupplier) {
        this.actionSupplier = actionSupplier;
    }

    @Override
    public void handleStatement(Statement st) throws RDFHandlerException {
        try {
            String graph = st.getContext() == null ? defaultGraph : st.getContext().stringValue();
            onQuad(
                    isAssertion(),
                    handleNode(st.getSubject()),
                    st.getPredicate().stringValue(),
                    handleNode(st.getObject()),
                    (st.getObject() instanceof Literal) ? ((Literal) st.getObject()).getDatatype().toString() : null,
                    (st.getObject() instanceof Literal) ? ((Literal) st.getObject()).getLanguage().orElse(null) : null,
                    graph
            );
        } catch (Exception e) {
            throw new RDFHandlerException(e);
        }
    }

    private boolean isAssertion() {
        return actionSupplier == null || actionSupplier.get() == ADD;
    }

    private String handleNode(Value resource) {
        if (resource instanceof BNode) {
            String nodeName = resource.toString();
            String nodeId = nodeName.substring(nodeName.indexOf(":") + 1, nodeName.length());
            return "BlankNode:" + defaultGraph + "/" + nodeId;
        } else {
            return resource.stringValue();
        }
    }

    private void onQuad(boolean isAssertion, String subject, String predicate, String object,
                        String dataType, String language, String graph) throws Exception {
        if (isAssertion) {
            if (dataType == null || dataType.isEmpty()) {
                this.addRelation(subject, predicate, object, graph);
            } else {
                if (language != null && !language.isEmpty() && dataType.equals(LANGSTRING)) {
                    this.addLanguageTaggedString(subject, predicate, object, language, graph);
                } else {
                    this.addValue(subject, predicate, object, dataType, graph);
                }
            }
        } else {
            if (dataType == null || dataType.isEmpty()) {
                this.delRelation(subject, predicate, object, graph);
            } else {
                if (language != null && !language.isEmpty() && dataType.equals(LANGSTRING)) {
                    this.delLanguageTaggedString(subject, predicate, object, language, graph);
                } else {
                    this.delValue(subject, predicate, object, dataType, graph);
                }
            }
        }
    }

    private void addRelation(String subject, String predicate, String object, String graph) throws Exception {
        putQuad(subject, predicate, Direction.OUT, object, null, null);
        putQuad(object, predicate, Direction.IN, subject, null, null);
    }

    private void addValue(String subject, String predicate, String value, String dataType, String graph)
            throws Exception {
        putQuad(subject, predicate, Direction.OUT, value, dataType, null);
    }

    private void addLanguageTaggedString(String subject, String predicate, String value, String language,
                                         String graph) throws Exception {
        putQuad(subject, predicate, Direction.OUT, value, LANGSTRING, language);
    }

    private void delRelation(String subject, String predicate, String object, String graph)
            throws Exception {
        deleteQuad(subject, predicate, Direction.OUT, object, null, null);
        deleteQuad(object, predicate, Direction.IN, subject, null, null);
    }

    private void delValue(String subject, String predicate, String value, String dataType, String graph)
            throws Exception {
        deleteQuad(subject, predicate, Direction.OUT, value, dataType, null);
    }

    private void delLanguageTaggedString(String subject, String predicate, String value, String language,
                                         String graph) throws Exception {
        deleteQuad(subject, predicate, Direction.OUT, value, LANGSTRING, language);
    }

    protected abstract void putQuad(String subject, String predicate, Direction direction, String object, String valueType,
                                    String language) throws Exception;

    protected abstract void deleteQuad(String subject, String predicate, Direction direction, String object, String valueType,
                                       String language) throws Exception;
}
