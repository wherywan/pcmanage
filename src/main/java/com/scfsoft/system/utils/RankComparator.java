package com.scfsoft.system.utils;

import java.math.BigDecimal;
import java.util.Comparator;

public class RankComparator implements Comparator<BigDecimal> {
    @Override
    public int compare(BigDecimal o1, BigDecimal o2) {
        if(o2.compareTo(o1)==-1){
            return -1;
        }else if(o2.compareTo(o1)==1){
            return 0;
        }else{
            return 1;
        }
    }
}
