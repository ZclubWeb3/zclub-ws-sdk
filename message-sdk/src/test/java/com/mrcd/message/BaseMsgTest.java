package com.mrcd.message;

import org.junit.Assert;

public class BaseMsgTest extends Assert {

    protected void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * not null
     *
     * @param obj
     */
    protected void notNull(Object obj) {
        assertNotNull(obj);
    }

    /**
     * is null
     *
     * @param obj
     */
    protected void isNull(Object obj) {
        assertNull(obj);
    }

    /**
     * @param value1
     * @param value2
     */
    protected void eq(Object value1, Object value2) {
        assertEquals(value1, value2);
    }

    protected void isTrue(boolean value) {
        assertTrue(value);
    }

    protected void isFalse(boolean value) {
        assertFalse(value);
    }

    protected void isNullStr(String text) {
        isTrue(text == null || text.length() == 0);
    }
}
