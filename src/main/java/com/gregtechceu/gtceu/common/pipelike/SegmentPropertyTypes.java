package com.gregtechceu.gtceu.common.pipelike;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.pipenet.property.SegmentPropertyType;

public class SegmentPropertyTypes {

    // Wires
    public static SegmentPropertyType MAX_VOLTAGE = new SegmentPropertyType(GTCEu.id("max_voltage"));
    public static SegmentPropertyType MAX_AMPS = new SegmentPropertyType(GTCEu.id("max_amps"));
    public static SegmentPropertyType LOSS_PER_BLOCK = new SegmentPropertyType(GTCEu.id("loss"));
    public static SegmentPropertyType IS_SUPERCONDUCTOR = new SegmentPropertyType(GTCEu.id("is_superconductor"));

    // Fluid pipes
    public static SegmentPropertyType FLUID_THROUGHPUT = new SegmentPropertyType(GTCEu.id("fluid_throughput"));
    public static SegmentPropertyType MAX_TEMPERATURE = new SegmentPropertyType(GTCEu.id("max_temperature"));
    public static SegmentPropertyType CHANNELS = new SegmentPropertyType(GTCEu.id("fluid_channels"));
    public static SegmentPropertyType GAS_PROOF = new SegmentPropertyType(GTCEu.id("gas_proof"));
    public static SegmentPropertyType ACID_PROOF = new SegmentPropertyType(GTCEu.id("acid_proof"));
    public static SegmentPropertyType CRYO_PROOF = new SegmentPropertyType(GTCEu.id("cryo_proof"));
    public static SegmentPropertyType PLASMA_PROOF = new SegmentPropertyType(GTCEu.id("plasma_proof"));

    public static SegmentPropertyType TRANSFER_RATE = new SegmentPropertyType(GTCEu.id("transfer_rate"));
    public static SegmentPropertyType PRIORITY = new SegmentPropertyType(GTCEu.id("priority"));
}
