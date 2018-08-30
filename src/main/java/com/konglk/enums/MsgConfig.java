package com.konglk.enums;

/**
 * Created by konglk on 2018/8/24.
 */
public class MsgConfig {

    public static int READ = 1;
    public static int NOT_READ = 0;

    public enum MsgType {
        TXT(1), IMG(2), VOICE(3), VIDEO(4);
        private MsgType(int v){
            this.v = v;
        }
        private int v;
    }
}
