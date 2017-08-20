package com.test.web.crawler.util;

import org.junit.Test;

import static com.test.web.crawler.util.StringUtils.checkNotBlank;

public class StringUtilsTest {
    /**
     * It should throw IllegalArgumentException when input is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckNotBlankForNullValue() {
        checkNotBlank(null);
    }

    /**
     * It should throw IllegalArgumentException when input is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckNotBlankForEmptyString() {
        checkNotBlank("");
    }

    /**
     * It should not throw any exception
     */
    @Test
    public void testCheckNotBlank() {
        checkNotBlank("String");
    }
}
