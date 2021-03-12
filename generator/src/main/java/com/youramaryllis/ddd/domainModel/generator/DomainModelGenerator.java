package com.youramaryllis.ddd.domainModel.generator;

import com.youramaryllis.ddd.contextMap.annotations.BoundedContext;
import com.youramaryllis.ddd.domainModel.annotations.AggregateRoot;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Engine;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizCmdLineEngine;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.Node;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.io.File;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.Arrays;

import static guru.nidi.graphviz.attribute.Attributes.attr;
import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.node;

@Slf4j
public class DomainModelGenerator {
    @SneakyThrows
    public DomainModelGenerator(String packageName) {
        log.info("domain model generator " + packageName);
        /*
          setup graphviz
         */
        GraphvizCmdLineEngine cmdLineEngine = new GraphvizCmdLineEngine();
        cmdLineEngine.setDotOutputFile(Paths.get("").toAbsolutePath().toString(), "domain_model");
        Graphviz.useEngine(cmdLineEngine);
        MutableGraph domainModel = buildMutableGraph("Domain Model");
        /*
          setup reflection
         */
        Reflections myPackages = new Reflections(packageName);
        myPackages.getTypesAnnotatedWith(BoundedContext.class)
                .forEach(bc -> {
                    domainModel.add(buildBoundedContext(bc));
                });
        /*
          generate graph
         */
        Graphviz.fromGraph(domainModel).engine(Engine.FDP).render(Format.SVG).toFile(new File("domain_model.svg"));
    }

    private MutableGraph buildMutableGraph(String name) {
        return mutGraph(name).setDirected(true)
                .graphAttrs().add(attr("size", "5.5"))
                .nodeAttrs().add(Shape.RECORD, Style.FILLED, attr("fillcolor", "gray95"), Font.config("Bitstream Vera Sans", 12))
                .linkAttrs().add(attr("dir", "back"), attr("fontsize", "3"), attr("fontname", "sans-serif"), attr("labeldistance", "0"));
    }

    private MutableGraph buildBoundedContext(Class<?> boundedContextPackage) {
        String bcName = boundedContextPackage.getAnnotation(BoundedContext.class).value();
        MutableGraph bc = mutGraph(bcName).setCluster(true).graphAttrs().add(attr("label", bcName), attr("style", "rounded"));
        String packageName = boundedContextPackage.getPackageName();
        Reflections bcPackages = new Reflections(packageName);
        bcPackages.getStore().put(SubTypesScanner.class, "dummy", "dummy");
        bcPackages.getTypesAnnotatedWith(AggregateRoot.class)
                .forEach(ar -> {
                    log.info("adding node " + ar.getSimpleName());
                    addNode(bc, boundedContextPackage.getPackageName(), ar);
                });
        return bc;
    }

    private void addNode(MutableGraph bc, String bcPackageName, Class<?> aClass) {
        if( !aClass.getCanonicalName().startsWith(bcPackageName )) return;
        Node arNode = node(aClass.getCanonicalName()).with(getClassLabel(aClass));
        bc.add(arNode);
        Arrays.stream(aClass.getDeclaredFields()).forEach(field -> {
            Type fieldType = field.getGenericType();
            if (fieldType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) fieldType;
                fieldType = parameterizedType.getActualTypeArguments()[0];
            }
            if( fieldType.getTypeName().startsWith(bcPackageName) )
                addNode(bc, bcPackageName, (Class<?>) fieldType);
        });

    }

    private Label getClassLabel(Class<?> ar) {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(ar.getSimpleName()).append("|");
        Arrays.stream(ar.getDeclaredFields()).forEach(field -> {
            sb.append((field.getModifiers() == Modifier.PUBLIC) ? "+ " : "- ")
                    .append(field.getName()).append("\\n");
        });
        sb.append("|");
        Arrays.stream(ar.getDeclaredMethods()).forEach(method -> {
            sb.append((method.getModifiers() == Modifier.PUBLIC) ? "+ " : "- ")
                    .append(method.getName()).append("\\n");
        });
        sb.append("}");
        return Label.of(sb.toString());
    }

    /*        Graph g = graph("example").directed()
                .graphAttr().with("size", "5,5")
                .nodeAttr().with(Shape.RECORD, Style.FILLED, attr("fillcolor", "gray95"), Font.config("Bitstream Vera Sans", 8))
                .linkAttr().with(attr("dir", "back"), attr("arrowtail", "empty"))
                .with(
                        node("2").wit(Label.of("{AbstractSuffixTree1239041349059|+ text\\n+ root|...}")),
                        node("3").with(Label.of("{SimpleSuffixTree|...| + constructTree()\\l...}")),
                        node("4").with(Label.of("{CompactSuffixTree|...| + compactNodes()\\l...}")),
                        node("5").with(Label.of("{SuffixTreeNode|...|+ addSuffix(...)\\l...}")),
                        node("6").with(Label.of("{SuffixTreeEdge|...|+ compactLabel(...)\\l...}")),
                        node("2").link(node("3")),
                        node("2").link(node("4")),
                        node("5").link(
                                to(node("5")).with(attr("constraint","false"), attr("arrowtail","odiamond"))
                        ),
                        node("4").link(
                                to(node("3")).with(attr("constraint","false"), attr("arrowtail","odiamond"))
                        ),
                        node( "2").link(
                                to(node("5")).with(attr("constraint","false"), attr("arrowtail","odiamond"))
                        ),
                        node("5").link(
                                to(node("6")).with( attr("arrowtail","odiamond"))
                        )
                );
        Graphviz.fromGraph(g).render(Format.PNG).toFile(new File("ex1.png"));*/
}
