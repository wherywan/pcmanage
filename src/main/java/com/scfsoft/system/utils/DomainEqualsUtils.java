package com.scfsoft.system.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

public class DomainEqualsUtils {

    public static boolean domainEquals(Object src, Object target) {
        if (src == null || target == null) {
            return false;
        }
        boolean rv = true;
        rv = classOfSrc(src, target, rv);
        return rv;
    }

    private static boolean classOfSrc(Object src, Object target, boolean rv) {
        Field[] fields = src.getClass().getDeclaredFields();
        for (Field field : fields) {
            String nameKey = field.getName();
            String srcValue = getClassValue(src, nameKey) == null ? "" : getClassValue(src, nameKey).toString();
            String tgtValue = getClassValue(target, nameKey) == null ? "" : getClassValue(target, nameKey).toString();
            if (!srcValue.equals(tgtValue)) {
                rv = false;
                break;
            }
        }
        return rv;
    }

    public static Object getClassValue(Object obj, String fieldName) {
        if (obj == null) {
            return null;
        }
        try {
            String getter = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Class beanClass = obj.getClass();
            Method method = beanClass.getMethod(getter, new Class[] {});
            Object value = method.invoke(obj, new Object[] {});
            return value;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean checkListDiff(List<String> src, List<String> tgt) {
        if (src != null && src.size() > 0 && tgt != null && tgt.size() > 0) {
            src.sort(Comparator.comparing(String::hashCode));
            tgt.sort(Comparator.comparing(String::hashCode));
            return src.toString().equals(tgt.toString());
        } else {
            return false;
        }
    }
}
