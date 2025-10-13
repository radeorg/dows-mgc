package org.dows.mgc.sql.core.segment;

import org.dows.mgc.sql.core.enums.StatementType;

public abstract class AbstractDelete<T, Children extends AbstractDelete<T, Children, R>, R> extends AbstractWhere<T, Children, R> {

    public AbstractDelete(Class<T> entityClass, StatementType statementType) {
        super(entityClass, statementType);
    }
}
