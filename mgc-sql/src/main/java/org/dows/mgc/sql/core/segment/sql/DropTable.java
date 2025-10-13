package org.dows.mgc.sql.core.segment.sql;

import org.dows.mgc.sql.core.enums.StatementType;
import org.dows.mgc.sql.core.segment.AbstractBase;
import org.dows.mgc.sql.exception.EntitySqlRuntimeException;
import org.dows.mgc.sql.utils.ExecuteSqlUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class DropTable<T> extends AbstractBase<T, DropTable<T>, Integer> {

    public DropTable(Class<T> entityClass, StatementType statementType) {
        super(entityClass, statementType);
    }

    @Override
    protected Integer executeSql(Connection connection) {
        try {
            return ExecuteSqlUtils.executeUpdate(connection, StatementType.DROP.getValue() + tableModel.getTableName(), new HashMap<>());
        } catch (SQLException e) {
            throw new EntitySqlRuntimeException(e);
        }
    }
}
