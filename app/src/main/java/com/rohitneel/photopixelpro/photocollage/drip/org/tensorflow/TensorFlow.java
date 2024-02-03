package com.rohitneel.photopixelpro.photocollage.drip.org.tensorflow;

public final class TensorFlow {
    public static native String version();

    private TensorFlow() {
    }

    static void init() {
        NativeLibrary.load();
    }

    static {
        init();
    }
}
