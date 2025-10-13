package org.dows.mgc.sql.core.segment.entity;

import org.dows.mgc.sql.core.enums.StatementType;
import org.dows.mgc.sql.core.model.ColumnModel;
import org.dows.mgc.sql.core.segment.AbstractObject;
import org.dows.mgc.sql.core.segment.Condition;
import org.dows.mgc.sql.exception.EntitySqlRuntimeException;
import org.dows.mgc.sql.utils.DatabaseUtils;
import org.dows.mgc.sql.utils.ExecuteSqlUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public class GetById<T> extends AbstractObject<T, GetById<T>, T> {

    public GetById(T obj, StatementType statementType) {
        super(obj, statementType);
    }

    @Override
    protected T executeSql(Connection connection) {
        try {
            for (ColumnModel columnModel : tableModel.getColumns()) {
                if (columnModel.getKey().getIsKey()) {
                    Field field = columnModel.getF();
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (value != null)
                        conditions.add(new Condition(columnModel.getColumn(), columnModel.getSearch().getCondition(), DatabaseUtils.transValueObj2Db(value)));
                    else throw new EntitySqlRuntimeException("Primary key cannot be null");
                }
            }

            selectSql();

            List<T> result = ExecuteSqlUtils.executeQuery(connection, sb.toString(), params, entityClass);

            if (result.size() != 1)
                throw new EntitySqlRuntimeException("Record does not exist or multiple records found");

            return result.get(0);
        } catch (SQLException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException | IllegalAccessException | ParseException e) {
            throw new EntitySqlRuntimeException(e);
        }
    }
}
