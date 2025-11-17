package com.gregtechceu.gtceu.api.pipenet.property;

import org.apache.commons.lang3.NotImplementedException;

public abstract class PipeSegmentProperty<T> {

    public abstract T getValue();

    public T min(T other) {
        throw new NotImplementedException("Property behaviour not implemented");
    }

    public T max(T other) {
        throw new NotImplementedException("Property behaviour not implemented");
    }

    public T sum(T other) {
        throw new NotImplementedException("Property behaviour not implemented");
    }

    public abstract PipeSegmentProperty<T> copy();

    public abstract String toString();
}
