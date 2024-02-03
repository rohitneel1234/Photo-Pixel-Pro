package com.rohitneel.photopixelpro.photocollage.drip.org.tensorflow;

public final class Operation {
    private final Graph graph;
    private final long unsafeNativeHandle;

    private static native int dtype(long j, long j2, int i);

    private static native int inputListLength(long j, String str);

    private static native String name(long j);

    private static native int numOutputs(long j);

    private static native int outputListLength(long j, String str);

    private static native long[] shape(long j, long j2, int i);

    private static native String type(long j);

    Operation(Graph graph2, long j) {
        this.graph = graph2;
        this.unsafeNativeHandle = j;
    }

    public String name() {
        Graph.Reference ref = this.graph.ref();
        try {
            return name(this.unsafeNativeHandle);
        } finally {
            ref.close();
        }
    }

    public String type() {
        Graph.Reference ref = this.graph.ref();
        try {
            return type(this.unsafeNativeHandle);
        } finally {
            ref.close();
        }
    }

    public int numOutputs() {
        Graph.Reference ref = this.graph.ref();
        try {
            return numOutputs(this.unsafeNativeHandle);
        } finally {
            ref.close();
        }
    }

    public int outputListLength(String str) {
        Graph.Reference ref = this.graph.ref();
        try {
            return outputListLength(this.unsafeNativeHandle, str);
        } finally {
            ref.close();
        }
    }

    public Output<?>[] outputList(int i, int i2) {
        Output<?>[] outputArr = new Output[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            outputArr[i3] = output(i + i3);
        }
        return outputArr;
    }

    public <T> Output<T> output(int i) {
        return new Output<>(this, i);
    }

    public int hashCode() {
        return Long.valueOf(this.unsafeNativeHandle).hashCode();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Operation)) {
            return false;
        }
        Operation operation = (Operation) obj;
        if (this.graph != operation.graph) {
            return false;
        }
        Graph.Reference ref = this.graph.ref();
        try {
            if (this.unsafeNativeHandle != operation.unsafeNativeHandle) {
                z = false;
            }
            return z;
        } finally {
            ref.close();
        }
    }

    public String toString() {
        return String.format("<%s '%s'>", new Object[]{type(), name()});
    }

    public int inputListLength(String str) {
        Graph.Reference ref = this.graph.ref();
        try {
            return inputListLength(this.unsafeNativeHandle, str);
        } finally {
            ref.close();
        }
    }


    public long getUnsafeNativeHandle() {
        return this.unsafeNativeHandle;
    }


    public long[] shape(int i) {
        Graph.Reference ref = this.graph.ref();
        try {
            return shape(ref.nativeHandle(), this.unsafeNativeHandle, i);
        } finally {
            ref.close();
        }
    }


    public DataType dtype(int i) {
        Graph.Reference ref = this.graph.ref();
        try {
            return DataType.fromC(dtype(ref.nativeHandle(), this.unsafeNativeHandle, i));
        } finally {
            ref.close();
        }
    }
}
