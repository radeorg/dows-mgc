package org.dows.mgc.service;

import org.dows.mgc.model.GenerationStatus;

public interface GenerationStatusService {
    void markRunning(Long appId, String sessionId);

    void markBuilt(Long appId, String sessionId, String message);

    void markFailed(Long appId, String sessionId, String message);

    void markStopped(Long appId, String sessionId, String message);

    GenerationStatus getStatus(Long appId);
}
