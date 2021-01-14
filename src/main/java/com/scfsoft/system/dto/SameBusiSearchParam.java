package com.scfsoft.system.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class SameBusiSearchParam implements Serializable {

    private String curDate;

    private String type;
}
