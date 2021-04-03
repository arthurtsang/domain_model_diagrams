package com.youramaryllis.ddd.contextMap.generator;

import junit.framework.TestCase;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;

public class ContextMapGeneratorTest extends TestCase {

    @SneakyThrows
    public void testBase( String testName ) {
        String outputDirectory = "target/generated-test-output/"+testName;
        String outputName = testName+"Test";
        new ContextMapGenerator(
                "com.youramaryllis.ddd.contextMap.generator."+testName,
                outputDirectory,
                outputName
        );
        File output = FileUtils.getFile(outputDirectory, outputName+".dot");
        assert output.exists();
        File expected = ResourceUtils.getFile("classpath:"+outputName+".dot");
        assert FileUtils.contentEqualsIgnoreEOL( output, expected, "UTF-8" );
    }

    @SneakyThrows
    @Test
    public void testSharedKernel() {
        testBase("sharedKernel");
    }

    @Test
    public void testACL() {
        testBase("acl");
    }
}