package org.dows.mgc.sql.core.segment;

import org.dows.mgc.sql.core.enums.StatementType;

public abstract class AbstractObject<T, Children extends AbstractObject<T, Children, R>, R> extends AbstractBase<T, Children, R> {

    protected T obj;

    public AbstractObject(T obj, StatementType statementType) {
        super((Class<T>) obj.getClass(), statementType);
        this.obj = obj;
    }

}
