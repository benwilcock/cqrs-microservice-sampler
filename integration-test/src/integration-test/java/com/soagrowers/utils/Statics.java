package com.soagrowers.utils;

/**
 * Created by ben on 24/02/16.
 */
public class Statics {

    public static final int PORT_FOR_COMMANDS = 9000;
    public static final int PORT_FOR_QUERIES = 9001;
    public static final int PORT_FOR_DISCOVERY = 8761;
    public static final int PORT_FOR_CONFIG = 8888;
    public static final String QRY_SERVICE_ID = "PRODUCT-QUERY-SIDE";
    public static final String CMD_SERVICE_ID = "PRODUCT-COMMAND-SIDE";


    public static final String API = "";
    public static final String VERSION = "";
    public static final String PRODUCTS_CMD_BASE_PATH = API + VERSION + "/products";
    public static final String PRODUCTS_QRY_BASE_PATH = API + VERSION + "/products";
    public static final String CMD_PRODUCT_ADD = "/add";

}
