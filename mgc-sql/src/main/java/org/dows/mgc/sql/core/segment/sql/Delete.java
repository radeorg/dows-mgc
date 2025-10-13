package org.dows.mgc.sql.core.segment.sql;

import org.dows.mgc.sql.core.enums.StatementType;
import org.dows.mgc.sql.core.segment.AbstractDelete;
import org.dows.mgc.sql.exception.EntitySqlRuntimeException;
import org.dows.mgc.sql.utils.ExecuteSqlUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class Delete<T> extends AbstractDelete<T, Delete<T>, Integer> {

    public Delete(Class<T> entityClass, StatementType statementType) {
        super(entityClass, statementType);
    }

    @Override
    protected Integer executeSql(Connection connection) {
        try {
            return ExecuteSqlUtils.executeUpdate(connection, sb.toString(), params);
        } catch (SQLException e) {
            throw new EntitySqlRuntimeException(e);
        }
    }
}
