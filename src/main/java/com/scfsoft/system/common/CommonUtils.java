package com.scfsoft.system.common;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

@Slf4j
public class CommonUtils {
    public static void linuxCmd(String cmd) {
        StringBuffer result = null;
        try {
            //执行命令
            Process process = Runtime.getRuntime().exec(cmd);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            result = new StringBuffer();
            while((line = input.readLine())!=null) {
                result.append(line +"\n");
            }
        }catch(Exception e) {
            log.error("Exception:"+e.getMessage());
        }
    }

}
