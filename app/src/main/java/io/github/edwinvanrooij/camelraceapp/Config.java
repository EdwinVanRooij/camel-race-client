package io.github.edwinvanrooij.camelraceapp;

/**
 * Author: eddy
 * Date: 16-1-17.
 */

public class Config {
    public static final String GAME_ID = "url";

    public static final int CONNECTED_CHECKER_INTERVAL = 5; // in seconds
//    private final static String BACKEND_IP = "192.168.5.115";
    private final static String BACKEND_IP = "172.17.0.1";

    private final static int BACKEND_PORT = 8082;
    private final static String BACKEND_PATH = "client";

    public final static String BACKEND_CONNECTION_URL = String.format("ws://%s:%s/%s", BACKEND_IP, BACKEND_PORT, BACKEND_PATH);
}
