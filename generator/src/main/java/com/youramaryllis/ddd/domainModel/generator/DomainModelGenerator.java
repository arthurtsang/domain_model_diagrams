package com.youramaryllis.ddd.domainModel.generator;

import com.youramaryllis.ddd.contextMap.annotations.BoundedContext;
import com.youramaryllis.ddd.domainModel.annotations.AggregateRoot;
import com.youramaryllis.ddd.domainModel.annotations.CrossBoundaryReference;
import com.youramaryllis.ddd.domainModel.annotations.Event;
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
import org.javatuples.Pair;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.io.File;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static guru.nidi.graphviz.attribute.Attributes.attr;
import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.node;

@Slf4j
public class DomainModelGenerator {
    MutableGraph domainModel = null;

    @SneakyThrows
    public DomainModelGenerator(String packageName) {
        log.info("domain model generator " + packageName);
        /*
          setup graphviz
         */
        GraphvizCmdLineEngine cmdLineEngine = new GraphvizCmdLineEngine();
        cmdLineEngine.setDotOutputFile(Paths.get("").toAbsolutePath().toString(), "domain_model");
        Graphviz.useEngine(cmdLineEngine);
        domainModel = buildMutableGraph("Domain Model");
        /*
          setup reflection
         */
        Reflections myPackages = new Reflections(packageName);
        myPackages.getStore().put(SubTypesScanner.class, "dummy", "dummy");
        myPackages.getTypesAnnotatedWith(BoundedContext.class)
                .forEach(bc -> domainModel.add(buildBoundedContext(bc)));
        /*
          generate graph
         */
        Graphviz.fromGraph(domainModel).engine(Engine.DOT).render(Format.SVG).toFile(new File("domain_model.svg"));
    }

    private MutableGraph buildMutableGraph(String name) {
        return mutGraph(name).setDirected(true)
                .graphAttrs().add(attr("size", "5.5"), attr("compound", "true"), attr("K",1))
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
                .forEach(ar -> addNode(bc, boundedContextPackage.getPackageName(), ar, null));
        return bc;
    }

    private void addNode(MutableGraph bc, String bcPackageName, Class<?> aClass, Class<?> parentClass) {
        if (!aClass.getCanonicalName().startsWith(bcPackageName)) return;
        Node arNode = node(aClass.getCanonicalName()).with(getClassLabel(aClass));
        bc.add(arNode);
        if (parentClass != null) {
            Node parentNode = node(parentClass.getCanonicalName());
            bc.add(parentNode.link(arNode));
        }
        Arrays.stream(aClass.getDeclaredMethods())
                .filter(method -> method.getAnnotation(CrossBoundaryReference.class) != null )
                .map( method -> {
                    Class xrefClass = method.getDeclaredAnnotation(CrossBoundaryReference.class).value()[0];
                    Event event = method.getDeclaredAnnotation(Event.class);
                    String eventText = null;
                    if( event != null ) eventText = event.value();
                    return Pair.with( xrefClass, eventText );
                })
                .peek(p -> checkCrossBoundaryReference(p.getValue0()))
                .forEach(p -> {
                    if( p.getValue1() != null ) {
                        Node eventNode = node(p.getValue0().getCanonicalName()+"-"+p.getValue1().replace(' ', '-')).with(attr("label", p.getValue1()));
                        domainModel.add( eventNode );
                        domainModel.add( arNode.link(eventNode) );
                        domainModel.add( eventNode.link(node(p.getValue0().getCanonicalName())));
                    } else {
                        bc.add( arNode.link(node(p.getValue0().getCanonicalName())));
                    }
                });
        Arrays.stream(aClass.getDeclaredFields()).forEach(field -> {
            Type fieldType = field.getGenericType();
            if (fieldType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) fieldType;
                fieldType = parameterizedType.getActualTypeArguments()[0];
            }
            addNode(bc, bcPackageName, (Class<?>) fieldType, aClass);
        });

    }

    private void checkCrossBoundaryReference(Class<?> aClass) {
        if( aClass.getAnnotation(AggregateRoot.class) == null )
            throw new RuntimeException("cannot access non aggregated root class " + aClass.getCanonicalName() );
    }

    private Label getClassLabel(Class<?> ar) {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(ar.getSimpleName()).append("|");
        Arrays.stream(ar.getDeclaredFields()).forEach(field -> sb.append((field.getModifiers() == Modifier.PUBLIC) ? "+ " : "- ")
                .append(field.getName()).append("\\n"));
        sb.append("|");
        Arrays.stream(ar.getDeclaredMethods()).forEach(method -> sb.append((method.getModifiers() == Modifier.PUBLIC) ? "+ " : "- ")
                .append(method.getName()).append("\\n"));
        sb.append("}");
        return Label.of(sb.toString());
    }

}
