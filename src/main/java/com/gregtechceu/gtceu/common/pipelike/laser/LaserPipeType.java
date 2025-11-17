package com.gregtechceu.gtceu.common.pipelike.laser;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.pipenet.IPipeType;
import com.gregtechceu.gtceu.api.pipenet.PipeSegmentPropertyHolder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum LaserPipeType implements IPipeType<LaserPipeProperties>, StringRepresentable {

    NORMAL;

    public static final ResourceLocation TYPE_ID = GTCEu.id("laser");

    @Override
    public float getThickness() {
        return 0.375f;
    }

    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public ResourceLocation type() {
        return TYPE_ID;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Override
    public PipeSegmentPropertyHolder buildSegmentProperties(@Nullable Material material) {
        return new PipeSegmentPropertyHolder();
    }
}
