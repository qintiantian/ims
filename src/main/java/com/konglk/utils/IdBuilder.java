package com.konglk.utils;

import java.util.UUID;

/**
 * Created by konglk on 2018/8/30.
 */
public class IdBuilder {

    public static String buildId() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
