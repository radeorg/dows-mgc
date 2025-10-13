package org.dows.mgc.sql.core.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.dows.mgc.sql.core.enums.GenerationType;

@Data
@Accessors(chain = true)
public class KeyModel {
    private Boolean isKey = false;
    private GenerationType type = GenerationType.UUID;
}
