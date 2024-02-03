package com.rohitneel.photopixelpro.photocollage.drip.org.tensorflow;

import java.util.Objects;

public final class Output<T> implements Operand<T> {
    private final int index;
    private final Operation operation;

    public Output<T> asOutput() {
        return this;
    }

    public Output(Operation operation2, int i) {
        this.operation = operation2;
        this.index = i;
    }

    public Operation op() {
        return this.operation;
    }

    public int index() {
        return this.index;
    }

    public Shape shape() {
        return new Shape(this.operation.shape(this.index));
    }

    public DataType dataType() {
        return this.operation.dtype(this.index);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.operation, this.index});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Output)) {
            return false;
        }
        Output output = (Output) obj;
        if (this.index != output.index || !this.operation.equals(output.operation)) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return String.format("<%s '%s:%d' shape=%s dtype=%s>", new Object[]{this.operation.type(), this.operation.name(), this.index, shape().toString(), dataType()});
    }
}
