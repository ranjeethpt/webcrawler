package com.test.web.crawler.util;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class StringUtils {

    public static void checkNotBlank(String str) {
        checkArgument(isNotBlank(str));
    }
}
