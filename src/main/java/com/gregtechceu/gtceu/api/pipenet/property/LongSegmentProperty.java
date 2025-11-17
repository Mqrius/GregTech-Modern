package com.gregtechceu.gtceu.api.pipenet.property;

import lombok.Getter;
import lombok.Setter;

public class LongSegmentProperty extends PipeSegmentProperty<Long> {

    @Getter
    @Setter
    Long value;

    public LongSegmentProperty(long value) {
        this.value = value;
    }

    @Override
    public Long max(Long other) {
        return Math.max(value, other);
    }

    @Override
    public Long min(Long other) {
        return Math.max(value, other);
    }

    @Override
    public Long sum(Long other) {
        return value + other;
    }

    @Override
    public PipeSegmentProperty<Long> copy() {
        return new LongSegmentProperty(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
