package com.scfsoft.system.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author guojingyu
 */
@Data
@ApiModel("系统公告类型内容")
public class NoticeType {

    String text;
    String color;

    public NoticeType(String text,String color) {
        this.text = text;
        this.color = color;
    }

}