package com.gregtechceu.gtceu.api.pipenet;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;

import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.Nullable;

public interface IPipeType<NodeDataType> {

    /**
     * the thickness of the pipe.
     */
    float getThickness();

    /**
     * modify the node data by the pipe type.
     */
    default NodeDataType modifyProperties(NodeDataType baseProperties) {
        return baseProperties;
    }

    /**
     * can the pipe be painted as other color.
     */
    boolean isPaintable();

    /**
     * indicate a unique type id.
     */
    ResourceLocation type();

    PipeSegmentPropertyHolder buildSegmentProperties(@Nullable Material material);
}
