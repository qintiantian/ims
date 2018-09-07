package com.konglk.enums;

/**
 * Created by konglk on 2018/8/24.
 */
public class MsgConfig {

    public static int READ = 1;
    public static int NOT_READ = 0;

    public enum MsgType {
        TXT(0), VOICE(1), VIDEO(2), IMG(3), HYBRID(4), UNRECOGNIZED(-1);
        private MsgType(int v){
            this.v = v;
        }
        private int v;
    }
}
