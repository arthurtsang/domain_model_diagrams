package com.youramaryllis.ddd.contextMap.generator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RelationshipLabel {
    private String downRelationshipLabel = null;
    private String upRelationshipLabel = null;
    private String sharedRelationshipLabel = null;
    @Override
    public String toString() {
        return "upstream:" + upRelationshipLabel + " " +
                "downstream:" + downRelationshipLabel + " " +
                "shared:" + sharedRelationshipLabel;
    }
}
