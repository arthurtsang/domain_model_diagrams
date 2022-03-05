package com.youramaryllis.ddd.domainModel.generator;

import junit.framework.TestCase;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;

public class DomainModelGeneratorTest extends TestCase {


    @SneakyThrows
    @Test
    public void testDomainModel() {
        String outputDirectory = "target/generated-test-output/example";
        String outputName = "exampleTest";
        DomainModelGenerator generator = new DomainModelGenerator(
                "com.youramaryllis.ddd.domainModel.generator.example",
                outputDirectory,
                outputName );
        File output = FileUtils.getFile(outputDirectory, outputName+".dot");
        assert output.exists();
        File expected = ResourceUtils.getFile("classpath:"+outputName+".dot");
        assert FileUtils.contentEqualsIgnoreEOL( output, expected, "UTF-8" );
    }
}