package com.konglk.enums;

public class RelationshipConfig {

    public enum Status{
        WAITING(1), PASS(2);
        private Status(int v){this.v = v;}
        public int v;
    }

    public enum RelationshipType{
        TWOWAY(1), ONEWAY(2);
        private RelationshipType(int v){this.v = v;}
        public int v;
    }
}
