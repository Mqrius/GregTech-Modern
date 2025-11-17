package com.gregtechceu.gtceu.api.pipenet.property;

import lombok.Getter;
import lombok.Setter;

public class FloatSegmentProperty extends PipeSegmentProperty<Float> {

    @Getter
    @Setter
    Float value;

    public FloatSegmentProperty(float value) {
        this.value = value;
    }

    @Override
    public Float max(Float other) {
        return Math.max(value, other);
    }

    @Override
    public Float min(Float other) {
        return Math.max(value, other);
    }

    @Override
    public Float sum(Float other) {
        return value + other;
    }

    @Override
    public PipeSegmentProperty<Float> copy() {
        return new FloatSegmentProperty(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
