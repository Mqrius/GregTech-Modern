package com.gregtechceu.gtceu.common.pipelike.cable;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconType;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.WireProperties;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.pipenet.IMaterialPipeType;
import com.gregtechceu.gtceu.api.pipenet.PipeSegmentPropertyHolder;
import com.gregtechceu.gtceu.api.pipenet.property.BoolSegmentProperty;
import com.gregtechceu.gtceu.api.pipenet.property.IntSegmentProperty;
import com.gregtechceu.gtceu.api.pipenet.property.LongSegmentProperty;
import com.gregtechceu.gtceu.client.model.PipeModel;
import com.gregtechceu.gtceu.common.pipelike.SegmentPropertyTypes;

import net.minecraft.resources.ResourceLocation;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;

public enum WireType implements IMaterialPipeType<WireProperties> {

    WIRE_SINGLE("single_wire", 0.1875f, 1, 2, wireGtSingle, -1, false),
    WIRE_DOUBLE("double_wire", 0.3125f, 2, 2, wireGtDouble, -1, false),
    WIRE_QUADRUPLE("quadruple_wire", 0.4375f, 4, 3, wireGtQuadruple, -1, false),
    WIRE_OCTAL("octal_wire", 0.5625f, 8, 3, wireGtOctal, -1, false),
    WIRE_HEX("hex_wire", 0.8125f, 16, 3, wireGtHex, -1, false),

    CABLE_SINGLE("single_cable", 0.25f, 1, 1, cableGtSingle, 0, true),
    CABLE_DOUBLE("double_cable", 0.375f, 2, 1, cableGtDouble, 1, true),
    CABLE_QUADRUPLE("quadruple_cable", 0.5f, 4, 1, cableGtQuadruple, 2, true),
    CABLE_OCTAL("octal_cable", 0.625f, 8, 1, cableGtOctal, 3, true),
    CABLE_HEX("hex_cable", 0.875f, 16, 1, cableGtHex, 4, true);

    public static final ResourceLocation TYPE_ID = GTCEu.id("insulation");

    public final String name;
    public final float thickness;
    public final int amperage;
    public final int lossMultiplier;
    @Getter
    public final TagPrefix tagPrefix;
    public final int insulationLevel;
    @Getter
    public final boolean isCable;

    WireType(String name, float thickness, int amperage, int lossMultiplier, TagPrefix TagPrefix, int insulated,
             boolean isCable) {
        this.name = name;
        this.thickness = thickness;
        this.amperage = amperage;
        this.tagPrefix = TagPrefix;
        this.insulationLevel = insulated;
        this.lossMultiplier = lossMultiplier;
        this.isCable = isCable;
    }

    @Override
    public float getThickness() {
        return thickness;
    }

    @Override
    public WireProperties modifyProperties(WireProperties baseProperties) {
        int lossPerBlock;
        if (!baseProperties.isSuperconductor() && baseProperties.getLossPerBlock() == 0)
            lossPerBlock = (int) (0.75 * lossMultiplier);
        else lossPerBlock = baseProperties.getLossPerBlock() * lossMultiplier;

        return new WireProperties(baseProperties.getVoltage(), baseProperties.getAmperage() * amperage, lossPerBlock,
                baseProperties.isSuperconductor());
    }

    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public ResourceLocation type() {
        return TYPE_ID;
    }

    public PipeModel createPipeModel(Material material) {
        Supplier<ResourceLocation> wireSideTexturePath = () -> MaterialIconType.wire
                .getBlockTexturePath(material.getMaterialIconSet(), "side", true);
        Supplier<ResourceLocation> wireEndTexturePath = () -> MaterialIconType.wire
                .getBlockTexturePath(material.getMaterialIconSet(), "end", true);
        Supplier<@Nullable ResourceLocation> wireSideOverlayTexturePath = () -> MaterialIconType.wire
                .getBlockTexturePath(material.getMaterialIconSet(), "side_overlay", true);
        Supplier<@Nullable ResourceLocation> wireEndOverlayTexturePath = () -> MaterialIconType.wire
                .getBlockTexturePath(material.getMaterialIconSet(), "end_overlay", true);
        PipeModel model = new PipeModel(thickness,
                isCable ? () -> GTCEu.id("block/cable/insulation_5") : wireSideTexturePath, wireEndTexturePath,
                wireSideOverlayTexturePath, wireEndOverlayTexturePath);
        if (isCable) {
            model.setEndOverlayTexture(GTCEu.id("block/cable/insulation_%s".formatted(insulationLevel)));
        }
        return model;
    }

    @Override
    public PipeSegmentPropertyHolder buildSegmentProperties(@Nullable Material material) {
        if (material == null || material.getProperty(PropertyKey.WIRE) == null) throw new IllegalArgumentException(
                "Attempted to build wire properties for null material or material without PropertyKey.WIRE");
        var segmentProperties = new PipeSegmentPropertyHolder();
        var materialProperties = material.getProperty(PropertyKey.WIRE);

        int lossPerBlock = 0;
        if (materialProperties.getLossPerBlock() == 0 && !materialProperties.isSuperconductor())
            lossPerBlock = (int) (0.75 * lossMultiplier);
        else lossPerBlock = materialProperties.getLossPerBlock() * lossMultiplier;

        long voltage = materialProperties.getVoltage();
        int amps = materialProperties.getAmperage() * amperage;

        segmentProperties.setProperty(SegmentPropertyTypes.MAX_VOLTAGE, new LongSegmentProperty(voltage))
                .setProperty(SegmentPropertyTypes.MAX_AMPS, new IntSegmentProperty(amps))
                .setProperty(SegmentPropertyTypes.LOSS_PER_BLOCK, new IntSegmentProperty(lossPerBlock))
                .setProperty(SegmentPropertyTypes.IS_SUPERCONDUCTOR, new BoolSegmentProperty(materialProperties.isSuperconductor()));

        return segmentProperties;
    }
}
