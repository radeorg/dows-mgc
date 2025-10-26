package org.dows.mgc.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import org.dows.mgc.app.AppAdaptor;
import org.dows.mgc.entity.AppEntity;
import org.dows.mgc.entity.UserEntity;
import org.dows.mgc.model.dto.app.AppAddRequest;
import org.dows.mgc.model.dto.app.AppQueryRequest;
import org.dows.mgc.model.vo.AppVO;

import java.util.List;

/**
 * 应用 服务层。
 *
 */
public interface AppService extends IService<AppEntity>, AppAdaptor {

    AppVO getAppVO(AppEntity app);

    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    List<AppVO> getAppVOList(List<AppEntity> appList);




}
