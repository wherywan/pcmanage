package com.scfsoft.system.enums;

import lombok.Getter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum OrgCode {

    /*江苏*/
    JS("江苏", "320000001"),
    /*南京*/
    NJ("南京","320590000"),
    /*无锡*/
    WX("无锡", "320610000"),
    /*徐州*/
    XZ("徐州", "320710000"),
    /*常州*/
    CZ("常州", "320620000"),
    /*南通*/
    NT("南通", "320640000"),
    /*连云港*/
    LYG("连云港", "320650000"),
    /*淮安*/
    HA("淮安", "320720000"),
    /*盐城*/
    YC("盐城", "320730000"),
    /*扬州*/
    YZ("扬州", "320740000"),
    /*镇江*/
    ZJ("镇江", "320750000"),
    /*泰州*/
    TZ("泰州", "320760000"),
    /*宿迁*/
    SQ("宿迁", "320770000");

    private String orgName;
    private String code;

    OrgCode(String orgName, String code){
        this.orgName = orgName;
        this.code = code;
    }

    public static Map<String, String> getEnumMap(){
        HashMap<String, String> map = new HashMap<>();
        for(OrgCode orgCode : EnumSet.allOf(OrgCode.class)){
            map.put(orgCode.orgName, orgCode.code);
        }
        return map;
    }

    public static Map<String, String> getOrgNameMap(){
        HashMap<String, String> map = new HashMap<>();
        for(OrgCode orgCode : EnumSet.allOf(OrgCode.class)){
            map.put(orgCode.code, orgCode.orgName);
        }
        return map;
    }
}
