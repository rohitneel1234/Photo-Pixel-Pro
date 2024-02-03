package com.rohitneel.photopixelpro.photocollage.picker;

import java.io.File;

public class FileUtils {
    public static boolean fileIsExists(String str) {
        if (str == null || str.trim().length() <= 0) {
            return false;
        }
        try {
            if (!new File(str).exists()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
