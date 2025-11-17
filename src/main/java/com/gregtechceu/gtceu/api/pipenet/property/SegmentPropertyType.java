package com.gregtechceu.gtceu.api.pipenet.property;

import net.minecraft.resources.ResourceLocation;

import lombok.Getter;

public class SegmentPropertyType {

    @Getter
    private final ResourceLocation id;

    public SegmentPropertyType(ResourceLocation id) {
        this.id = id;
    }
}
