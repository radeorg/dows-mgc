package org.dows.mgc.git;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RepositoryResponse {
    private String name;
    private String httpUrl;
    private String gitUrl;
    private String ref;
    private String sha;
    private Long id;
}
