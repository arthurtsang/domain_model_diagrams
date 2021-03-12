package com.youramaryllis.ddd.domainModel.generator;

import com.youramaryllis.ddd.domainModel.annotations.Entity;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.Set;

@Slf4j
public class DomainModelGenerator {
    public DomainModelGenerator(String packageName) {
        log.info("domain model generator " + packageName);
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);
        //entities.forEach(entity -> log.info(entity.getCanonicalName()));
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
