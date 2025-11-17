package com.gregtechceu.gtceu.common.pipelike;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.pipenet.PipeNetworkType;
import com.gregtechceu.gtceu.common.pipelike.cable.EnergyNet;
import com.gregtechceu.gtceu.common.pipelike.duct.DuctPipeNet;
import com.gregtechceu.gtceu.common.pipelike.fluidpipe.FluidPipeNet;
import com.gregtechceu.gtceu.common.pipelike.item.ItemPipeNet;
import com.gregtechceu.gtceu.common.pipelike.laser.LaserPipeNet;
import com.gregtechceu.gtceu.common.pipelike.optical.OpticalPipeNet;

public class GTPipeNetworks {

    public static PipeNetworkType FLUID = new PipeNetworkType(GTCEu.id("fluid"), FluidPipeNet::new);
    public static PipeNetworkType ITEM = new PipeNetworkType(GTCEu.id("item_pipe"), ItemPipeNet::new);
    public static PipeNetworkType ENERGY = new PipeNetworkType(GTCEu.id("energy"), EnergyNet::new);
    public static PipeNetworkType DUCT = new PipeNetworkType(GTCEu.id("duct"), DuctPipeNet::new);
    public static PipeNetworkType LASER = new PipeNetworkType(GTCEu.id("laser"), LaserPipeNet::new);
    public static PipeNetworkType OPTICAL = new PipeNetworkType(GTCEu.id("optical"), OpticalPipeNet::new);
}
