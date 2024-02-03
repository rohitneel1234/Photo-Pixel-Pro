package com.rohitneel.photopixelpro.photocollage.drip.org.tensorflow;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Iterator;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public final class Graph implements AutoCloseable {
    static {
        TensorFlow.init();
    }

    public final Object nativeHandleLock;

    public long nativeHandle;

    public int refcount;

    public Graph() {
        this.nativeHandleLock = new Object();
        this.refcount = 0;
        this.nativeHandle = allocate();
    }

    Graph(long j) {
        this.nativeHandleLock = new Object();
        this.refcount = 0;
        this.nativeHandle = j;
    }

    private static native long[] addGradients(long j, long[] jArr, int[] iArr, long[] jArr2, int[] iArr2, long[] jArr3, int[] iArr3);

    private static native long allocate();

    private static native void delete(long j);

    private static native void importGraphDef(long j, byte[] bArr, String str) throws IllegalArgumentException;

    public static native long[] nextOperation(long j, int i);

    private static native long operation(long j, String str);

    private static native byte[] toGraphDef(long j);

    static /* synthetic */ int access$206(Graph graph) {
        int i = graph.refcount - 1;
        graph.refcount = i;
        return i;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:9|10|11|12|13|14|7) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0017 */
    public void close() {
        synchronized (this.nativeHandleLock) {
            if (this.nativeHandle != 0) {
                while (this.refcount > 0) {
                    try {
                        this.nativeHandleLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Thread.currentThread().interrupt();
                    return;
                }
                delete(this.nativeHandle);
                this.nativeHandle = 0;
            }
        }
    }

    public Operation operation(String str) {
        synchronized (this.nativeHandleLock) {
            long operation = operation(this.nativeHandle, str);
            if (operation == 0) {
                return null;
            }
            Operation operation2 = new Operation(this, operation);
            return operation2;
        }
    }

    public Iterator<Operation> operations() {
        return new OperationIterator(this);
    }

    public OperationBuilder opBuilder(String str, String str2) {
        return new OperationBuilder(this, str, str2);
    }

    public void importGraphDef(byte[] bArr) throws IllegalArgumentException {
        importGraphDef(bArr, "");
    }

    public void importGraphDef(byte[] bArr, String str) throws IllegalArgumentException {
        if (bArr == null || str == null) {
            throw new IllegalArgumentException("graphDef and prefix cannot be null");
        }
        synchronized (this.nativeHandleLock) {
            importGraphDef(this.nativeHandle, bArr, str);
        }
    }

    public byte[] toGraphDef() {
        byte[] graphDef;
        synchronized (this.nativeHandleLock) {
            graphDef = toGraphDef(this.nativeHandle);
        }
        return graphDef;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Output<?>[] addGradients(Output<?>[] outputArr, Output<?>[] outputArr2, Output<?>[] outputArr3) {
        Throwable th;
        int[] iArr;
        long[] jArr;
        Output<?>[] outputArr4 = new Output[outputArr2.length];
        long[] jArr2 = new long[outputArr.length];
        int[] iArr2 = new int[outputArr.length];
        long[] jArr3 = new long[outputArr2.length];
        int[] iArr3 = new int[outputArr2.length];
        Reference ref = ref();
        int i = 0;
        int i2 = 0;
        while (i2 < outputArr.length) {
            jArr2[i2] = outputArr[i2].op().getUnsafeNativeHandle();
            iArr2[i2] = outputArr[i2].index();
            i2++;
        }
        for (int i3 = 0; i3 < outputArr2.length; i3++) {
            jArr3[i3] = outputArr2[i3].op().getUnsafeNativeHandle();
            iArr3[i3] = outputArr2[i3].index();
        }
        if (outputArr3 == null || outputArr3.length <= 0) {
            jArr = null;
            iArr = null;
        } else {
            long[] jArr4 = new long[outputArr3.length];
            int[] iArr4 = new int[outputArr3.length];
            for (int i4 = 0; i4 < outputArr3.length; i4++) {
                jArr4[i4] = outputArr3[i4].op().getUnsafeNativeHandle();
                iArr4[i4] = outputArr3[i4].index();
            }
            jArr = jArr4;
            iArr = iArr4;
        }
        long[] addGradients = addGradients(ref.nativeHandle(), jArr2, iArr2, jArr3, iArr3, jArr, iArr);
        int length = addGradients.length >> 1;
        if (length == outputArr4.length) {
            int i5 = length;
            while (i < length) {
                outputArr4[i] = new Output<>(new Operation(this, addGradients[i]), (int) addGradients[i5]);
                i++;
                i5++;
            }
            if (ref != null) {
                ref.close();
            }
            return outputArr4;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(length);
        sb.append(" gradients were added to the graph when ");
        sb.append(outputArr4.length);
        sb.append(" were expected");
        throw new IllegalStateException(sb.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Output<?>[] addGradients(Output<?> output, Output<?>[] outputArr) {
        return addGradients(new Output[]{output}, outputArr, null);
    }


    public Reference ref() {
        return new Reference();
    }

    private static final class OperationIterator implements Iterator<Operation> {
        private final Graph graph;
        private Operation operation = null;
        private int position = 0;

        OperationIterator(Graph graph2) {
            this.graph = graph2;
            advance();
        }

        private final void advance() {
            Reference ref = this.graph.ref();
            this.operation = null;
            try {
                long[] access$400 = Graph.nextOperation(ref.nativeHandle(), this.position);
                if (!(access$400 == null || access$400[0] == 0)) {
                    this.operation = new Operation(this.graph, access$400[0]);
                    this.position = (int) access$400[1];
                }
            } finally {
                ref.close();
            }
        }

        public boolean hasNext() {
            return this.operation != null;
        }

        public Operation next() {
            Operation operation2 = this.operation;
            advance();
            return operation2;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove() is unsupported.");
        }
    }

    class Reference implements AutoCloseable {
        private boolean active;

        private Reference() {
            synchronized (Graph.this.nativeHandleLock) {
                this.active = Graph.this.nativeHandle != 0;
                if (this.active) {
                    this.active = true;
                    Graph.this.refcount = Graph.this.refcount + 1;
                } else {
                    throw new IllegalStateException("close() has been called on the Graph");
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0022, code lost:
            return;
         */
        public void close() {
            synchronized (Graph.this.nativeHandleLock) {
                if (this.active) {
                    this.active = false;
                    if (Graph.access$206(Graph.this) == 0) {
                        Graph.this.nativeHandleLock.notifyAll();
                    }
                }
            }
        }

        public long nativeHandle() {
            long access$100;
            synchronized (Graph.this.nativeHandleLock) {
                access$100 = this.active ? Graph.this.nativeHandle : 0;
            }
            return access$100;
        }
    }
}
