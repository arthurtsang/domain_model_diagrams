package com.youramaryllis.ddd;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.nio.file.Paths;

@Data
@NoArgsConstructor
@ConfigurationProperties("ddd-generator")
@Validated
public class DDDGeneratorProperties {
    @NotNull
    private String packageName;
    private String outputDirectory = Paths.get("").toAbsolutePath().toString();
}
