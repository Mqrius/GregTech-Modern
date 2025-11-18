package com.gregtechceu.gtceu.api.pipenet;

import com.gregtechceu.gtceu.api.pipenet.property.*;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class PipeSegmentPropertyHolder {

    private final Object2ObjectOpenHashMap<SegmentPropertyType, PipeSegmentProperty<?>> properties = new Object2ObjectOpenHashMap<>();

    public PipeSegmentPropertyHolder setProperty(SegmentPropertyType prop, PipeSegmentProperty<?> value) {
        properties.put(prop, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends PipeSegmentProperty<?>> T getProperty(SegmentPropertyType prop) {
        return (T) properties.get(prop);
    }

    public int getIntProperty(SegmentPropertyType prop) {
        return ((IntSegmentProperty)properties.get(prop)).getValue();
    }

    public long getLongProperty(SegmentPropertyType prop) {
        return ((LongSegmentProperty)properties.get(prop)).getValue();
    }

    public float getFloatProperty(SegmentPropertyType prop) {
        return ((FloatSegmentProperty)properties.get(prop)).getValue();
    }

    public boolean getBoolProperty(SegmentPropertyType prop) {
        return ((BoolSegmentProperty)properties.get(prop)).getValue();
    }

    public PipeSegmentPropertyHolder copy() {
        PipeSegmentPropertyHolder newHolder = new PipeSegmentPropertyHolder();
        for (var p : properties.entrySet()) {
            newHolder.setProperty(p.getKey(), p.getValue().copy());
        }
        return newHolder;
    }

    @Override
    public String toString() {
        String str = "PipeSegmentPropertyHolder{";
        for (var p : properties.entrySet()) {
            str = str.concat(p.getKey().getId().toString() + "=" + p.getValue().toString() + ",");
        }

        str = str.concat("}");
        return str;
    }
}
