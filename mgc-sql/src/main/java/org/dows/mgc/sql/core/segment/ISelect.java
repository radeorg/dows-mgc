package org.dows.mgc.sql.core.segment;

import org.dows.mgc.sql.core.functional_interface.SFunction;

public interface ISelect<T, Children> {

    Children select(SFunction<T, ?>... columns);
}
