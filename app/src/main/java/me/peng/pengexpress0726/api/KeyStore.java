package me.peng.pengexpress0726.api;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/7/31.
 */

public class KeyStore {
    public static String getKey(String keyName){
        try {
            String className = KeyStore.class.getPackage().getName() + ".Keys";
            Class apiKey = Class.forName(className);
            Field filed = apiKey.getField(keyName);
            filed.setAccessible(true);
            return (String) filed.get(null);
        } catch (Exception ignored) {

        }
        return "";
    }
}
