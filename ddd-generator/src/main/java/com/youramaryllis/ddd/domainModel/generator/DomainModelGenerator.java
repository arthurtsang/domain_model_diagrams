package com.youramaryllis.ddd.domainModel.generator;

import com.youramaryllis.ddd.contextMap.annotations.BoundedContext;
import com.youramaryllis.ddd.domainModel.annotations.*;
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
import org.apache.logging.log4j.util.Strings;
import org.javatuples.Triplet;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

import static guru.nidi.graphviz.attribute.Attributes.attr;
import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.node;

@Slf4j
public class DomainModelGenerator {
    MutableGraph domainModel;

    @SneakyThrows
    public DomainModelGenerator(String packageName, String outputDirectory, String outputName ) {
        log.info("domain model generator " + packageName);
        new File(outputDirectory).mkdirs();
        /*
          setup graphviz
         */
        GraphvizCmdLineEngine cmdLineEngine = new GraphvizCmdLineEngine();
        cmdLineEngine.setDotOutputFile( outputDirectory, outputName );
        Graphviz.useEngine(cmdLineEngine);
        domainModel = buildMutableGraph("Domain Model");
        /*
          setup reflection
         */
        Reflections myPackages = new Reflections(packageName);
        myPackages.getStore().put(SubTypesScanner.class, "dummy", "dummy");
        /*
          build domain model
         */
        myPackages.getTypesAnnotatedWith(BoundedContext.class)
                .forEach(bc -> domainModel.add(buildBoundedContext(bc)));
        /*
          generate graph
         */
        Graphviz.fromGraph(domainModel).engine(Engine.DOT).render(Format.SVG).toFile( Paths.get(outputDirectory, outputName+".svg").toFile() );
    }

    private MutableGraph buildMutableGraph(String name) {
        return mutGraph(name).setDirected(true)
                .graphAttrs().add(attr("size", "5.5"), attr("compound", "true"), attr("K",1))
                .nodeAttrs().add(Shape.RECTANGLE, Style.FILLED, attr("fillcolor", "gray95"), Font.config("Bitstream Vera Sans", 12), attr("margin", "0"))
                .linkAttrs().add(attr("dir", "none"), attr("fontsize", "3"), attr("fontname", "sans-serif"), attr("labeldistance", "0"));
    }

    private MutableGraph buildBoundedContext(Class<?> boundedContextPackage) {
        String bcName = boundedContextPackage.getAnnotation(BoundedContext.class).value();
        Optional<ResourceBundle> ubiquitousLanguage = getUbiquitousLanguage(bcName);
        MutableGraph bc = mutGraph(bcName).setCluster(true).graphAttrs().add(attr("label", bcName), attr("style", "rounded"));
        if( ubiquitousLanguage.isPresent() && ubiquitousLanguage.get().containsKey(bcName) ) bc.graphAttrs().add(attr("tooltip", ubiquitousLanguage.get().getString(bcName)));
        String packageName = boundedContextPackage.getPackageName();
        Reflections bcPackages = new Reflections(packageName);
        bcPackages.getStore().put(SubTypesScanner.class, "dummy", "dummy");
        bcPackages.getTypesAnnotatedWith(AggregateRoot.class)
                .forEach(ar -> addNode(bc, boundedContextPackage.getPackageName(), ar, null, ubiquitousLanguage));
        return bc;
    }

    Optional<ResourceBundle> getUbiquitousLanguage(String bcName) {
        ResourceBundle ubiqutiousLanguage = null;
        try {
            ubiqutiousLanguage = ResourceBundle.getBundle(bcName);
        }catch (MissingResourceException e) {}
        return Optional.ofNullable(ubiqutiousLanguage);
    }

    private void addNode(MutableGraph bc, String bcPackageName, Class<?> aClass, Class<?> parentClass, Optional<ResourceBundle> ubiquitousLanguage) {
        if (!aClass.getCanonicalName().startsWith(bcPackageName)) return;
        Node arNode = node(aClass.getCanonicalName()).with(getClassLabel(aClass)).with(attr("tooltip", getClassToolTip(aClass, ubiquitousLanguage)));
        bc.add(arNode);
        if (parentClass != null) {
            Node parentNode = node(parentClass.getCanonicalName());
            bc.add(parentNode.link(arNode));
        }
        /*
            add links if method is annotated with CrossBoundaryReference
         */
        Arrays.stream(aClass.getDeclaredMethods())
                .filter(method -> method.getAnnotation(Events.class) != null )
                .map( method -> {
                    Event[] events = method.getDeclaredAnnotation(Events.class).value();
                    return Arrays.stream(events).map(event -> Triplet.with( event.target(), event.value(), event.persona() ) );
                })
                .flatMap(Function.identity())
                .peek(p -> checkEventTarget(p.getValue0()))
                .forEach(p -> {
                    if(!p.getValue1().equals(Strings.EMPTY)) {
                        Node eventNode = node(p.getValue0().getCanonicalName()+"-"+p.getValue1().replace(' ', '-')).with(getEventLabel(p)).with(attr("tooltip", p.getValue1()));
                        domainModel.add( eventNode );
                        domainModel.add( arNode.link(eventNode) );
                        domainModel.add( eventNode.link(node(p.getValue0().getCanonicalName())));
                    } else {
                        bc.add( arNode.link(node(p.getValue0().getCanonicalName())));
                    }
                });
        /*
            for each of the field, traverse down to add nodes and links
         */
        Arrays.stream(aClass.getDeclaredFields()).forEach(field -> {
            Type fieldType = field.getGenericType();
            if (fieldType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) fieldType;
                fieldType = parameterizedType.getActualTypeArguments()[0];
            }
            addNode(bc, bcPackageName, (Class<?>) fieldType, aClass, ubiquitousLanguage);
        });
    }

    /**
     * Any method referencing outside domains must only reference to it's root aggregate
     * @param aClass
     */
    private void checkEventTarget(Class<?> aClass) {
        if( aClass.getAnnotation(AggregateRoot.class) == null )
            throw new RuntimeException("cannot access non aggregated root class " + aClass.getCanonicalName() );
    }

    /**
     * build the label for an event
     * @param p Triplet with cross reference classname, event text and persona
     * @return
     */
    private Label getEventLabel(Triplet<Class, String, String> p) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table border=\"0\" cellborder=\"1\" cellspacing=\"1\" bgcolor=\"" + Colors.EVENT + "\">" );
        sb.append("<tr><td cellpadding=\"10\"><b>");
        sb.append( p.getValue1() );
        sb.append("</b></td></tr>");
        if( !Strings.EMPTY.equals(p.getValue2())) {
            sb.append("<tr><td><font point-size=\"10\">");
            sb.append(p.getValue2());
            sb.append("</font></td></tr>");
        }
        sb.append("</table>");
        return Label.html(sb.toString());
    }

    /**
     * construct an HTML table for the class
     * @param ar Class object
     * @return
     */
    private Label getClassLabel(Class<?> ar) {
        String color = ( ar.getAnnotation(External.class) != null ) ? Colors.EXTERNAL :
                ( ar.getAnnotation(AggregateRoot.class) != null ) ? Colors.AGGREGATE_ROOT: Colors.AGGREGATE;
        StringBuilder sb = new StringBuilder();
        sb.append("<table border=\"0\" cellborder=\"1\" cellspacing=\"1\" bgcolor=\"" + color + "\">" );
        sb.append("<tr><td cellpadding=\"10\"><b><font point-size=\"16\">");
        sb.append(ar.getSimpleName());
        sb.append("</font></b></td></tr>");
        if( ar.getDeclaredFields().length > 0 ) {
            sb.append("<tr><td>");
            Arrays.stream(ar.getDeclaredFields()).forEach(field -> sb.append((field.getModifiers() == Modifier.PUBLIC) ? "+ " : "- ")
                    .append(field.getName()).append("<br/>"));
            sb.append("</td></tr>");
        }
        if( ar.getDeclaredMethods().length > 0 )
        {
            sb.append("<tr><td>");
            Arrays.stream(ar.getDeclaredMethods()).forEach(method -> sb.append((method.getModifiers() == Modifier.PUBLIC) ? "+ " : "- ")
                    .append(method.getName()).append("<br/>"));
            sb.append("</td></tr>");
        }
        sb.append("</table>");
        return Label.html(sb.toString());
    }

    private String getClassToolTip(Class<?> ar, Optional<ResourceBundle> ul) {
        StringBuilder sb = new StringBuilder();
        if( ul.isPresent() ) {
            ResourceBundle ul1 = ul.get();
            Arrays.stream(ar.getDeclaredFields())
                    .map(Field::getName)
                    .filter(ul1::containsKey)
                    .forEach(field -> sb.append(splitCamelCase(field)).append(": ").append(ul1.getString(field)).append("\n"));
        }
        if( sb.length() == 0 ) sb.append(ar.getSimpleName());
        return sb.toString();
    }

    private String splitCamelCase(String camel) {
        String text = String.join(" ", camel.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"));
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
