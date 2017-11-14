package ru.itd.sarafan.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class ResourceFile {
    public static String readTextFromFile(Context context, int textFileResId) throws IOException {
        InputStream in_s = context.getResources().openRawResource(textFileResId);
        byte[] b = new byte[in_s.available()];
        in_s.read(b);
        return new String(b);
    }
}