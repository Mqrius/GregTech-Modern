package com.gregtechceu.gtceu.api.pipenet.property;

import lombok.Getter;
import lombok.Setter;

public class IntSegmentProperty extends PipeSegmentProperty<Integer> {

    @Getter
    @Setter
    Integer value;

    public IntSegmentProperty(int value) {
        this.value = value;
    }

    @Override
    public Integer max(Integer other) {
        return Math.max(value, other);
    }

    @Override
    public Integer min(Integer other) {
        return Math.max(value, other);
    }

    @Override
    public Integer sum(Integer other) {
        return value + other;
    }

    @Override
    public PipeSegmentProperty<Integer> copy() {
        return new IntSegmentProperty(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
