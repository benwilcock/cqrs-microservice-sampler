package com.soagrowers.productcommand.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * Created by ben on 02/03/16.
 */
public final class Asserts {

    private static final Logger LOG = LoggerFactory.getLogger(Asserts.class);

    @Value("${debug}")
    private static boolean debug = false;
    public static final String EMPTY_STRING = "";
    public static final String UNEXPECTED_NULL = "Unexpected Null Parameter";
    public static final String UNEXPECTED_EMPTY_STRING = "Unexpected Null Parameter";
    public static final String NOT_TRUE = "Should have been TRUE but wasn't";
    public static final String NOT_FALSE = "Should have been FALSE but wasn't";
    public static final String USED_WRONG_METHOD = "I think you used the wrong Asserts method!";

    public static void isTrue(boolean trueism){
        if(debug && trueism == false){
            LOG.warn(NOT_TRUE);
            throw new AssertionError(NOT_TRUE);
        }
    }

    public static void isFalse(boolean falsehood){
        if(debug && falsehood == true){
            LOG.warn(NOT_FALSE);
            throw new AssertionError(NOT_FALSE);
        }
    }

    public static void isNotEmpty(Object object){

        if(debug) {

            if (null == object) {
                LOG.warn(UNEXPECTED_NULL);
                throw new AssertionError(UNEXPECTED_NULL);
            }

            if(List.class.isAssignableFrom(object.getClass())){
                LOG.warn(USED_WRONG_METHOD);
                throw new IllegalArgumentException(USED_WRONG_METHOD);
            }

            if (String.class.isAssignableFrom(object.getClass())) {
                if ((String) object == EMPTY_STRING) {
                    LOG.warn(UNEXPECTED_EMPTY_STRING);
                    throw new AssertionError(UNEXPECTED_EMPTY_STRING);
                }
            }
        }
    }

    public static void areNotEmpty(List<Object> objects){
        if(debug) {
            for (Object object : objects) {
                isNotEmpty(object);
            }
        }
    }

    public static boolean isAssertsOn() {
        return debug;
    }

    public static void setAssertsTo(boolean asserts) {
        Asserts.debug = asserts;
    }
}
