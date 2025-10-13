package org.dows.mgc.sql.core.segment;

import java.sql.Connection;

public interface IBase<T, Children, R> {

    R execute(Connection connection);
}
