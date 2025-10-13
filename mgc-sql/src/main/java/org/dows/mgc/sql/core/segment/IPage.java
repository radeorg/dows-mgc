package org.dows.mgc.sql.core.segment;

public interface IPage<Children> {

    Children page(int page, int pageSize);
}
