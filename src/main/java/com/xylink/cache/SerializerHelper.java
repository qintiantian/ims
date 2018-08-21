package com.xylink.cache;

import java.io.*;

/**
 * Created by konglk on 2018/8/15.
 */
public class SerializerHelper {

    public static byte[] serialize(Object obj) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
            out.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public static Object deserialize(byte[] bytes) {
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return in.readObject();
        }catch (Exception e) {
            return "";
        }
    }
}
