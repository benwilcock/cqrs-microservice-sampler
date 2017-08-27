package com.soagrowers.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by ben on 02/03/16.
 */
public class AssertsTest {

    @Before
    public void setup(){
        Asserts.INSTANCE.setAssertsTo(true);
    }

    @Test
    public void testSetAsserts(){

        Asserts.INSTANCE.setAssertsTo(true);
        assertTrue(Asserts.INSTANCE.isAssertsOn());

        Asserts.INSTANCE.setAssertsTo(false);
        assertFalse(Asserts.INSTANCE.isAssertsOn());
    }

    @Test
    public void testIsNotEmpty(){

        Asserts.INSTANCE.isNotEmpty(new String("test"));

        try {
            Asserts.INSTANCE.isNotEmpty(null);
            assertTrue(false);
        } catch (AssertionError ae){
            assertEquals(Asserts.INSTANCE.UNEXPECTED_NULL, ae.getMessage());
        }

        try {
            Asserts.INSTANCE.isNotEmpty(Asserts.EMPTY_STRING);
            assertTrue(false);
        } catch (AssertionError ae){
            assertEquals(Asserts.INSTANCE.UNEXPECTED_EMPTY_STRING, ae.getMessage());
        }
    }

    @Test
    public void testAreNotEmpty(){

        Asserts.INSTANCE.areNotEmpty(Arrays.asList((Object)"test", (Object)"test"));

        try {
            String id = UUID.randomUUID().toString();
            String name = Asserts.EMPTY_STRING;
            Asserts.INSTANCE.areNotEmpty(Arrays.asList((Object)id, (Object)name));
            assertTrue(false);
        } catch (AssertionError ae){
            assertEquals(ae.getMessage(), Asserts.UNEXPECTED_EMPTY_STRING);
        }

        try {
            String id = null;
            String name = Asserts.EMPTY_STRING;
            Asserts.INSTANCE.areNotEmpty(Arrays.asList((Object)id, (Object)name));
            assertTrue(false);
        } catch (AssertionError ae){
            assertEquals(ae.getMessage(), Asserts.UNEXPECTED_NULL);
        }
    }

    @Test
    public void testIsNotEmptyWithAList(){
        String id = UUID.randomUUID().toString();
        String name = Asserts.EMPTY_STRING;

        try {
            Asserts.INSTANCE.isNotEmpty(Arrays.asList(id, name));
            assertTrue(false);
        } catch (IllegalArgumentException ia){
            assertTrue(true);
        }
    }
}
