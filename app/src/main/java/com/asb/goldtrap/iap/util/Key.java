package com.asb.goldtrap.iap.util;

/**
 * Key. Lol.
 * Created by arjun on 17/04/16.
 */
public final class Key {
    private Key() {
    }

    private static final String PART_1 =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu8sHR3bXzQAodCmGOM3l9M+";
    private static final String PART_2 = "Zw42IzpZ+";
    private static final String PART_3 =
            "ivPCmX9efZvADD9sSpKlATQczcAUpgFxdXgkvOZVcJkUI3/yUxAVekh68SLG+";
    private static final String PART_4 = "AbIJ7AgMYlu+";
    private static final String PART_5 = "REJNs1gxFlDwI58IPwLem7dy+";
    private static final String PART_6 = "yyJuWIZH5Y/uYsrqt3EegrsGXs/+";
    private static final String PART_7 =
            "r/jQ4ESxlZtIWM2quxj3/rEq6mUi1lDPelnFzp2wI0yP9bb7m6K6H32OHMOKdNuZQPVVX8c9b/";
    private static final String PART_8 =
            "C4EG7qiLzclXoD/bi6CC17LQXENFh60o1dek9kIC/PhWqNukXvwHdK6kWktglq6aP/sR+";
    private static final String PART_9 = "nxpwXVNhYvPJW1jrQg1smKXQj6pgecSNMSMFnQ+";
    private static final String PART_10 = "uwIDAQAB";

    public static String value() {
        return PART_1 + PART_2
                + PART_3 + PART_4
                + PART_5 + PART_6
                + PART_7 + PART_8
                + PART_9 + PART_10;
    }
}
