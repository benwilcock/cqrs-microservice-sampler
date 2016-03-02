package com.soagrowers.productcommand.utils;

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
        Asserts.setAssertsTo(true);
    }

    @Test
    public void testSetAsserts(){

        Asserts.setAssertsTo(true);
        assertTrue(Asserts.isAssertsOn());

        Asserts.setAssertsTo(false);
        assertFalse(Asserts.isAssertsOn());
    }

    @Test
    public void testIsNotEmpty(){

        Asserts.isNotEmpty(new String("test"));

        try {
            Asserts.isNotEmpty(null);
            assertTrue(false);
        } catch (AssertionError ae){
            assertEquals(Asserts.UNEXPECTED_NULL, ae.getMessage());
        }

        try {
            Asserts.isNotEmpty(Asserts.EMPTY_STRING);
            assertTrue(false);
        } catch (AssertionError ae){
            assertEquals(Asserts.UNEXPECTED_EMPTY_STRING, ae.getMessage());
        }
    }

    @Test
    public void testAreNotEmpty(){

        Asserts.areNotEmpty(Arrays.asList("test", "test"));

        try {
            String id = UUID.randomUUID().toString();
            String name = Asserts.EMPTY_STRING;
            Asserts.areNotEmpty(Arrays.asList(id, name));
            assertTrue(false);
        } catch (AssertionError ae){
            assertEquals(ae.getMessage(), Asserts.UNEXPECTED_EMPTY_STRING);
        }

        try {
            String id = null;
            String name = Asserts.EMPTY_STRING;
            Asserts.areNotEmpty(Arrays.asList(id, name));
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
            Asserts.isNotEmpty(Arrays.asList(id, name));
            assertTrue(false);
        } catch (IllegalArgumentException ia){
            assertTrue(true);
        }
    }
}
