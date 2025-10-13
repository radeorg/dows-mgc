package org.dows.mgc.sql.core.segment;

import org.dows.mgc.sql.core.enums.StatementType;
import org.dows.mgc.sql.core.functional_interface.SFunction;

public abstract class AbstractInsert<T, Children extends AbstractInsert<T, Children, R>, R> extends AbstractBase<T, Children, R> implements ISet<T, Children> {

    public AbstractInsert(Class<T> entityClass, StatementType statementType) {
        super(entityClass, statementType);
    }

    @Override
    public Children set(SFunction<T, ?> column, Object value) {
        sets.add(new Set(tableModel.getColumnByField(column), value));
        return typedThis;
    }

}
