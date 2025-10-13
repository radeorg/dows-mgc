package org.dows.mgc.sql.core.segment;

import org.dows.mgc.sql.core.enums.StatementType;

public abstract class AbstractPage<T, Children extends AbstractPage<T, Children, R>, R> extends AbstractWhere<T, Children, R> implements IPage<Children> {

    public AbstractPage(Class<T> entityClass, StatementType statementType) {
        super(entityClass, statementType);
    }

    @Override
    public Children page(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
        return typedThis;
    }
}
