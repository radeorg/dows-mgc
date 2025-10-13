package org.dows.mgc.sql.core.model;

import lombok.Data;
import org.dows.mgc.sql.core.functional_interface.SFunction;
import org.dows.mgc.sql.utils.LambdaUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class TableModel {

    private String tableName;
    private List<ColumnModel> columns = new ArrayList<>();
    private Boolean init = false;

    public <T> String getColumnByField(SFunction<T, ?> fn) {
        String field = LambdaUtils.getFieldName(fn);
        // @formatter:off
        return columns.stream()
                .filter(columnModel -> columnModel.getField().equals(field))
                .findAny()
                .orElseThrow(() -> new  IllegalArgumentException("Column model not found for field: " + field))
                .getColumn();
        // @formatter:on
    }

    public ColumnModel getColumnModelByColumn(String column) {
        // @formatter:off
        return columns.stream()
                .filter(columnModel -> columnModel.getColumn().equalsIgnoreCase(column))
                .findAny()
                .orElseThrow(() -> new  IllegalArgumentException("Column model not found for column: " + column));
        // @formatter:on
    }
}
