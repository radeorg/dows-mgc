package org.dows.mgc.sql.core.segment;

import org.dows.mgc.sql.core.functional_interface.SFunction;


public interface ISet<T, Children> {

    Children set(SFunction<T, ?> column, Object value);
}
