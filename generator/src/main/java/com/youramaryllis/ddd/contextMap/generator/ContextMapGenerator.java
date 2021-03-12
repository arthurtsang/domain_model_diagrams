package com.youramaryllis.ddd.contextMap.generator;

import com.youramaryllis.ddd.contextMap.annotations.*;
import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Engine;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizCmdLineEngine;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.Node;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

import static guru.nidi.graphviz.attribute.Attributes.attr;
import static guru.nidi.graphviz.model.Factory.*;

@Slf4j
public class ContextMapGenerator {
    @SneakyThrows
    public ContextMapGenerator(String packageName) {
        log.info("context map generator " + packageName);
        HashMap<LinkedNodes, RelationshipLabel> upDownStreamMap = new HashMap();
        GraphvizCmdLineEngine cmdLineEngine = new GraphvizCmdLineEngine();
        cmdLineEngine.setDotOutputFile(Paths.get("").toAbsolutePath().toString(), "context_map");
        Graphviz.useEngine(cmdLineEngine);
        Reflections myPackages = new Reflections(packageName);
        MutableGraph contextMap = mutGraph("Context Map")
                .graphAttrs().add(attr("size", "5.5"))
                .nodeAttrs().add(Shape.ELLIPSE, Style.FILLED, attr("fillcolor", "gray95"), Font.config("Bitstream Vera Sans", 12))
                .linkAttrs().add(attr("dir", "none"), attr("fontsize", "3"), attr("fontname", "sans-serif"), attr("labeldistance", "0"));
        myPackages.getTypesAnnotatedWith(BoundedContext.class)
                .forEach(bc -> {
                    Node bcNode = node(bc.getPackageName())
                            .with(Label.of(bc.getAnnotation(BoundedContext.class).value()));
                    contextMap.add(bcNode);
                    updateUpDownStreamMap(upDownStreamMap, bc);
                });

        addLinkToContextMap(upDownStreamMap, contextMap);
        Graphviz.fromGraph(contextMap).engine(Engine.FDP).render(Format.SVG).toFile(new File("context_map.svg"));
    }

    private Label getHtmlLabel(String upDown, String rel) {
        String html = String.format("<table cellspacing=\"0\" cellborder=\"%s\" border=\"0\"><tr><td bgcolor=\"white\" sides=\"r\">%s</td><td sides=\"trbl\" bgcolor=\"white\">%s</td></tr></table>", ("".equals(rel)) ? "0" : "1", upDown, rel);
        return Label.html(html);
    }

    private void updateUpDownStreamMap(HashMap<LinkedNodes, RelationshipLabel> upDownStreamMap, Class<?> bc) {
        Arrays.stream(bc.getAnnotations())
                .filter(annotation -> annotation.annotationType().getAnnotation(Relationship.class) != null)
                .forEach(annotation -> {
                    switch (annotation.annotationType().getSimpleName()) {
                        case "AntiCorruptionLayer":
                            AntiCorruptionLayer acl = (AntiCorruptionLayer) annotation;
                            LinkedNodes key = new LinkedNodes(acl.value(), bc.getPackageName());
                            RelationshipLabel value = upDownStreamMap.getOrDefault(key, new RelationshipLabel());
                            if (value.getDownRelationshipLabel() != null || value.getSharedRelationshipLabel() != null) {
                                throw new RuntimeException(key.getPackageName1() + " -- " + key.getPackageName2() + " has conflicting relationships: " + value.toString());
                            }
                            value.setDownRelationshipLabel("ACL");
                            upDownStreamMap.put(key, value);
                            break;
                        case "OpenHostService":
                            OpenHostService ohs = (OpenHostService) annotation;
                            key = new LinkedNodes(ohs.value(), bc.getPackageName());
                            value = upDownStreamMap.getOrDefault(key, new RelationshipLabel());
                            if (value.getUpRelationshipLabel() != null || value.getSharedRelationshipLabel() != null) {
                                throw new RuntimeException(key.getPackageName1() + " -- " + key.getPackageName2() + " has conflicting relationships: " + value.toString());
                            }
                            value.setUpRelationshipLabel("OHS");
                            upDownStreamMap.put(key, value);
                            break;
                        case "SharedKernel":
                            SharedKernel sk = (SharedKernel) annotation;
                            key = new LinkedNodes(sk.value(), bc.getPackageName());
                            value = upDownStreamMap.getOrDefault(key, new RelationshipLabel());
                            if (value.getUpRelationshipLabel() != null && value.getDownRelationshipLabel() != null) {
                                throw new RuntimeException(key.getPackageName1() + " -- " + key.getPackageName2() + " has conflicting relationships: " + value.toString());
                            }
                            value.setSharedRelationshipLabel("Shared Kernel");
                            upDownStreamMap.put(key, value);
                            break;
                        case "Conformist":
                            Conformist cf = (Conformist) annotation;
                            key = new LinkedNodes(cf.value(), bc.getPackageName());
                            value = upDownStreamMap.getOrDefault(key, new RelationshipLabel());
                            if (value.getUpRelationshipLabel() != null && value.getSharedRelationshipLabel() != null) {
                                throw new RuntimeException(key.getPackageName1() + " -- " + key.getPackageName2() + " has conflicting relationships: " + value.toString());
                            }
                            value.setDownRelationshipLabel("CF");
                            upDownStreamMap.put(key, value);
                            break;
                        case "CustomerSupplier":
                            CustomerSupplier cs = (CustomerSupplier) annotation;
                            key = (cs.upStream()) ? new LinkedNodes(cs.value(), bc.getPackageName()) : new LinkedNodes(bc.getPackageName(), cs.value());
                            value = upDownStreamMap.getOrDefault(key, new RelationshipLabel());
                            if (value.getSharedRelationshipLabel() != null) {
                                throw new RuntimeException(key.getPackageName1() + " -- " + key.getPackageName2() + " has conflicting relationships: " + value.toString());
                            }
                            if (cs.upStream())
                                value.setUpRelationshipLabel("");
                            else
                                value.setDownRelationshipLabel("");
                            upDownStreamMap.put(key, value);
                            break;
                        default:
                            throw new RuntimeException("unknown relationship");
                    }
                });
    }

    private void addLinkToContextMap(HashMap<LinkedNodes, RelationshipLabel> upDownStreamMap, MutableGraph contextMap) {
        upDownStreamMap.forEach((key, value) -> {
            Node upNode = node(key.getPackageName1());
            Node downNode = node(key.getPackageName2());
            List<Attributes<? extends ForLink>> attributes = new ArrayList<>();
            if (Objects.nonNull(value.getDownRelationshipLabel())) {
                attributes.add(attr("headlabel", getHtmlLabel("D", value.getDownRelationshipLabel())));
            }
            if (Objects.nonNull(value.getUpRelationshipLabel())) {
                attributes.add(attr("taillabel", getHtmlLabel("U", value.getUpRelationshipLabel())));
            }
            if (Objects.nonNull(value.getSharedRelationshipLabel())) {
                attributes.add(Label.of(value.getSharedRelationshipLabel()));
            }
            contextMap.add(upNode.link(to(downNode).with(attributes)));
        });
    }
}
