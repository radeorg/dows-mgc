package org.dows.mgc.sql.core.segment;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.dows.mgc.sql.core.enums.StatementCondition;

@Data
@AllArgsConstructor
public class Condition {
    private String column;
    private StatementCondition statementCondition;
    private Object value;
}
