package org.dows.mgc.sql.core.segment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Set {
    private String column;
    private Object value;
}
