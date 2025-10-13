package org.dows.mgc.sql.core.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.dows.mgc.sql.core.enums.EditType;

@Data
@Accessors(chain = true)
public class EditModel {
    private Boolean editable = true;
    private EditType type = EditType.TEXT;
    private Boolean required = false;
    private Integer editOrder = 100;
    private String placeholder = "";
}
