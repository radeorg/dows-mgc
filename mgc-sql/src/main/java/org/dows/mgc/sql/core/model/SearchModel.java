package org.dows.mgc.sql.core.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.dows.mgc.sql.core.enums.EditType;
import org.dows.mgc.sql.core.enums.StatementCondition;

@Data
@Accessors(chain = true)
public class SearchModel {
    private Boolean searchable = false;
    private EditType type = EditType.TEXT;
    private StatementCondition condition = StatementCondition.EQ;
    private Integer searchOrder = 100;
}
