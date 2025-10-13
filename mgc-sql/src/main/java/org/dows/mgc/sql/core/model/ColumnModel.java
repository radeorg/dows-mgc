package org.dows.mgc.sql.core.model;

import lombok.Data;
import org.dows.mgc.sql.core.enums.ColumnType;
import org.dows.mgc.sql.core.enums.GenerationType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.dows.mgc.sql.core.enums.ColumnType.NUMBER;
import static org.dows.mgc.sql.core.enums.ColumnType.VARCHAR;

@Data
public class ColumnModel {
    ColumnType type = VARCHAR;
    GenerationType generationType;
    private String field;
    private String column;
    private String label;
    private String placeholder = "";
    private int length = 200;
    private int precision = 18;
    private int scale = 2;
    private List<ItemModel> items = new ArrayList<>();
    private ViewModel view = new ViewModel();
    private EditModel edit = new EditModel();
    private SearchModel search = new SearchModel();
    private KeyModel key = new KeyModel();
    private Field f;

    public String definition() {
        String str;
        if (type == VARCHAR) {
            str = String.format("%s %s(%d)", column, type.getValue(), length);
        } else if (type == NUMBER) {
            str = String.format("%s %s(%d,%d)", column, type.getValue(), precision, scale);
        } else {
            str = String.format("%s %s", column, type.getValue());
        }

        return key.getIsKey() ? str + " PRIMARY KEY" : str;
    }

}
