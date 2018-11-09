package com.pankesh.utils;


public class Statics {

    public static final int PORT_FOR_GATEWAY = 8080;
    public static final int PORT_FOR_COMMANDS = 9000;
    public static final int PORT_FOR_QUERIES = 9001;
    public static final int PORT_FOR_DISCOVERY = 8761;
    public static final int PORT_FOR_CONFIG = 8888;
    public static final String QRY_SERVICE_ID = "PRODUCT-QUERY-SIDE";
    public static final String CMD_SERVICE_ID = "PRODUCT-COMMAND-SIDE";


    public static final String API = "";
    public static final String VERSION = "";
    public static final String CMD_ROUTE = "/commands";
    public static final String QRY_ROUTE = "/queries";
    public static final String PRODUCTS_CMD_BASE_PATH = API + VERSION + CMD_ROUTE + "/products";
    public static final String PRODUCTS_QRY_BASE_PATH = API + VERSION + QRY_ROUTE + "/products";
    public static final String CMD_PRODUCT_ADD = "/add";

    public static final String PROD_CMD_MESSAGE = "Greetings from the PRODUCT-COMMAND-SIDE microservice [using the PRODUCTION config].";
    public static final String PROD_QRY_MESSAGE = "Greetings from the PRODUCT-QUERY-SIDE microservice [using the PRODUCTION config].";

    public static final String LOCAL_CMD_MESSAGE = "Greetings from the PRODUCT-COMMAND-SIDE microservice [using the LOCALHOST config].";
    public static final String LOCAL_QRY_MESSAGE = "Greetings from the PRODUCT-QUERY-SIDE microservice [using the LOCALHOST config].";

    public static final boolean PRODUCTION = Boolean.valueOf(System.getProperty("production", "true"));

}
