package org.dows.mgc.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ChatRequest {
    @NotNull
    private String text;
}
