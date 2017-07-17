package io.github.edwinvanrooij.camelraceapp;

/**
 * Author: eddy
 * Date: 16-1-17.
 */

public class Config {
    public static final String GAME_ID = "url";

    public static final int CONNECTED_CHECKER_INTERVAL = 10; // in seconds
//    private final static String BACKEND_IP = "192.168.178.37 "; // idk
//    private final static String BACKEND_IP = "192.168.5.131"; // idk


    private final static String BACKEND_IP = "88.159.34.253"; // production
//    private final static String BACKEND_IP = "192.168.5.115"; // home laptop

    private final static int BACKEND_PORT = 8085;
    private final static String BACKEND_PATH = "client";

    public final static String BACKEND_CONNECTION_URL = String.format("ws://%s:%s/%s", BACKEND_IP, BACKEND_PORT, BACKEND_PATH);
}
