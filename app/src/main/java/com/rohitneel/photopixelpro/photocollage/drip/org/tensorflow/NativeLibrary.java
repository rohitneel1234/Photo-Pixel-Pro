package com.rohitneel.photopixelpro.photocollage.drip.org.tensorflow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

final class NativeLibrary {
    private static final boolean DEBUG = (System.getProperty("org.tensorflow.NativeLibrary.DEBUG") != null);
    private static final String JNI_LIBNAME = "tensorflow_jni";

    public static void load() {
        if (!isLoaded() && !tryLoadLibrary()) {
            String mapLibraryName = System.mapLibraryName(JNI_LIBNAME);
            String makeResourceName = makeResourceName(mapLibraryName);
            StringBuilder sb = new StringBuilder();
            sb.append("jniResourceName: ");
            sb.append(makeResourceName);
            log(sb.toString());
            InputStream resourceAsStream = NativeLibrary.class.getClassLoader().getResourceAsStream(makeResourceName);
            String maybeAdjustForMacOS = maybeAdjustForMacOS(System.mapLibraryName("tensorflow_framework"));
            String makeResourceName2 = makeResourceName(maybeAdjustForMacOS);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("frameworkResourceName: ");
            sb2.append(makeResourceName2);
            log(sb2.toString());
            InputStream resourceAsStream2 = NativeLibrary.class.getClassLoader().getResourceAsStream(makeResourceName2);
            if (resourceAsStream != null) {
                try {
                    File createTemporaryDirectory = createTemporaryDirectory();
                    createTemporaryDirectory.deleteOnExit();
                    String canonicalPath = createTemporaryDirectory.getCanonicalPath();
                    if (resourceAsStream2 != null) {
                        extractResource(resourceAsStream2, maybeAdjustForMacOS, canonicalPath);
                    } else {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(makeResourceName2);
                        sb3.append(" not found. This is fine assuming ");
                        sb3.append(makeResourceName);
                        sb3.append(" is not built to depend on it.");
                        log(sb3.toString());
                    }
                    System.load(extractResource(resourceAsStream, mapLibraryName, canonicalPath));
                } catch (IOException e) {
                    throw new UnsatisfiedLinkError(String.format("Unable to extract native library into a temporary file (%s)", new Object[]{e.toString()}));
                }
            } else {
                throw new UnsatisfiedLinkError(String.format("Cannot find TensorFlow native library for OS: %s, architecture: %s. See https://github.com/tensorflow/tensorflow/tree/master/tensorflow/java/README.md for possible solutions (such as building the library from source). Additional information on attempts to find the native library can be obtained by adding org.tensorflow.NativeLibrary.DEBUG=1 to the system properties of the JVM.", new Object[]{os(), architecture()}));
            }
        }
    }

    private static boolean tryLoadLibrary() {
        try {
            System.loadLibrary(JNI_LIBNAME);
            return true;
        } catch (UnsatisfiedLinkError e) {
            StringBuilder sb = new StringBuilder();
            sb.append("tryLoadLibraryFailed: ");
            sb.append(e.getMessage());
            log(sb.toString());
            return false;
        }
    }

    private static boolean isLoaded() {
        try {
            TensorFlow.version();
            log("isLoaded: true");
            return true;
        } catch (UnsatisfiedLinkError unused) {
            return false;
        }
    }

    private static String maybeAdjustForMacOS(String str) {
        if (!System.getProperty("os.name").contains("OS X") || NativeLibrary.class.getClassLoader().getResource(makeResourceName(str)) != null || !str.endsWith(".dylib")) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, str.length() - ".dylib".length()));
        sb.append(".so");
        return sb.toString();
    }

    private static String extractResource(InputStream inputStream, String str, String str2) throws IOException {
        File file = new File(str2, str);
        file.deleteOnExit();
        String file2 = file.toString();
        StringBuilder sb = new StringBuilder();
        sb.append("extracting native library to: ");
        sb.append(file2);
        log(sb.toString());
        log(String.format("copied %d bytes to %s", new Object[]{copy(inputStream, file), file2}));
        return file2;
    }

    private static String os() {
        String lowerCase = System.getProperty("os.name").toLowerCase();
        if (lowerCase.contains("linux")) {
            return "linux";
        }
        if (lowerCase.contains("os x") || lowerCase.contains("darwin")) {
            return "darwin";
        }
        if (lowerCase.contains("windows")) {
            return "windows";
        }
        return lowerCase.replaceAll("\\s", "");
    }

    private static String architecture() {
        String lowerCase = System.getProperty("os.arch").toLowerCase();
        return lowerCase.equals("amd64") ? "x86_64" : lowerCase;
    }

    private static void log(String str) {
        if (DEBUG) {
            PrintStream printStream = System.err;
            StringBuilder sb = new StringBuilder();
            sb.append("org.tensorflow.NativeLibrary: ");
            sb.append(str);
            printStream.println(sb.toString());
        }
    }

    private static String makeResourceName(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("org/tensorflow/native/");
        sb.append(String.format("%s-%s/", new Object[]{os(), architecture()}));
        sb.append(str);
        return sb.toString();
    }

    private static long copy(InputStream inputStream, File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            byte[] bArr = new byte[1048576];
            long j = 0;
            while (true) {
                int read = inputStream.read(bArr);
                if (read < 0) {
                    return j;
                }
                fileOutputStream.write(bArr, 0, read);
                j += (long) read;
            }
        } finally {
            fileOutputStream.close();
            inputStream.close();
        }
    }

    private static File createTemporaryDirectory() {
        File file = new File(System.getProperty("java.io.tmpdir"));
        StringBuilder sb = new StringBuilder();
        sb.append("tensorflow_native_libraries-");
        sb.append(System.currentTimeMillis());
        sb.append("-");
        String sb2 = sb.toString();
        for (int i = 0; i < 1000; i++) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(sb2);
            sb3.append(i);
            File file2 = new File(file, sb3.toString());
            if (file2.mkdir()) {
                return file2;
            }
        }
        StringBuilder sb4 = new StringBuilder();
        sb4.append("Could not create a temporary directory (tried to make ");
        sb4.append(sb2);
        sb4.append("*) to extract TensorFlow native libraries.");
        throw new IllegalStateException(sb4.toString());
    }

    private NativeLibrary() {
    }
}
