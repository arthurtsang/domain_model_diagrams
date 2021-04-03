package com.youramaryllis.ddd;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Paths;

@Data
@NoArgsConstructor
@ConfigurationProperties("ddd-generator")
public class DDDGeneratorProperties {
    private String packageName;
    private String outputDirectory = Paths.get("").toAbsolutePath().toString();
}
