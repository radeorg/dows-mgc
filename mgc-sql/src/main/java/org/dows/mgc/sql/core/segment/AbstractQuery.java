package org.dows.mgc.sql.core.segment;

import org.dows.mgc.sql.core.enums.StatementType;
import org.dows.mgc.sql.core.functional_interface.SFunction;

public abstract class AbstractQuery<T, Children extends AbstractQuery<T, Children, R>, R> extends AbstractPage<T, Children, R> implements ISelect<T, Children> {

    public AbstractQuery(Class<T> entityClass, StatementType statementType) {
        super(entityClass, statementType);
    }

    @Override
    public Children select(SFunction<T, ?>... columns) {
        for (SFunction<T, ?> column : columns) {
            this.columns.add(tableModel.getColumnByField(column));
        }
        return typedThis;
    }

}
