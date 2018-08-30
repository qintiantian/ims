package com.konglk.enums;

/**
 * Created by konglk on 2018/8/24.
 */
public class UserConfig {

    public enum UserStatus {
        NORMAL(1), DISABLE(2);
        private UserStatus(int v){
            this.v = v;
        }
        public int v;
    }

}
