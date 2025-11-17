package com.gregtechceu.gtceu.api.pipenet;

import com.gregtechceu.gtceu.api.pipenet.property.PipeSegmentProperty;
import com.gregtechceu.gtceu.api.pipenet.property.SegmentPropertyType;

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
