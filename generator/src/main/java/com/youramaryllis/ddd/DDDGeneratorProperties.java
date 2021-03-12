package com.youramaryllis.ddd;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties("ddd-generator")
public class DDDGeneratorProperties {
    private String packageName;
}
