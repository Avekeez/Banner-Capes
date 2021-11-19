package com.github.Gorden121.the_banner_capes.compat;

public class GeneralModCompat {

    public static boolean doesClassExist(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException ignored) {}
        return false;
    }

}
