package com.anez.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cxw
 * @description FilterTokenConstant
 * @date 2020/11/9 11:13
 */
public class FilterUrlWhenNoTokenConstant {
    private static final List<String> FILTER_URL = new ArrayList<>();
    private static final String LOGIN = "/login";
    static {
        FILTER_URL.add(LOGIN);
    }
    public static List<String> getFilterUrl(){
        return FILTER_URL;
    }
}
