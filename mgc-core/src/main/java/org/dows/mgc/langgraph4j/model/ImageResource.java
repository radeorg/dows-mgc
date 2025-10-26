package org.dows.mgc.langgraph4j.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dows.mgc.langgraph4j.model.enums.ImageCategoryEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * 图片资源对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageResource implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 图片类别
     */
    private ImageCategoryEnum category;
    /**
     * 图片描述
     */
    private String description;
    /**
     * 图片地址
     */
    private String url;
}
