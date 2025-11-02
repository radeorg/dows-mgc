package org.dows.mgc.mind;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Schema(title = "脑图")
@Builder
@Data
public class GitMind {
    @Schema(title = "用户名")
    private String username;
    @Schema(title = "密码")
    private String password;
    @Schema(title = "取值路径[xpath]")
    private final List<MindXpath> mindXpaths = new ArrayList<>();

    public GitMind addMindXpath(MindXpath mindXpath) {
        mindXpaths.add(mindXpath);
        return this;
    }
}
