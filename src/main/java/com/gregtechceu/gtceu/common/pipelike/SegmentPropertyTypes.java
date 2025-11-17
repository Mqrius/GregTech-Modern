package com.gregtechceu.gtceu.common.pipelike;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.pipenet.property.SegmentPropertyType;

public class SegmentPropertyTypes {

    // Wires
    public static SegmentPropertyType MAX_VOLTAGE = new SegmentPropertyType(GTCEu.id("max_voltage"));
    public static SegmentPropertyType MAX_AMPS = new SegmentPropertyType(GTCEu.id("max_amps"));
    public static SegmentPropertyType LOSS_PER_BLOCK = new SegmentPropertyType(GTCEu.id("loss"));

    public static SegmentPropertyType TRANSFER_RATE = new SegmentPropertyType(GTCEu.id("transfer_rate"));
    public static SegmentPropertyType PRIORITY = new SegmentPropertyType(GTCEu.id("priority"));
}
