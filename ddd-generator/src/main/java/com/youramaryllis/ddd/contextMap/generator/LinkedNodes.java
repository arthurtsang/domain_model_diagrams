package com.youramaryllis.ddd.contextMap.generator;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
public class LinkedNodes {
    private String packageName1;
    private String packageName2;

    @Override
    public boolean equals(Object o) {
        LinkedNodes o1 = (LinkedNodes) o;
        return (Objects.equals(o1.packageName1, packageName1) && Objects.equals(packageName2, o1.packageName2)) ||
                (Objects.equals(o1.packageName2, packageName1) && Objects.equals(packageName2, o1.packageName1)) ;
    }

    @Override
    public int hashCode() {
        return Stream.of(packageName1, packageName2).sorted().collect(Collectors.toUnmodifiableList()).hashCode();
    }
}
