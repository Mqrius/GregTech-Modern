package com.gregtechceu.gtceu.api.pipenet;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class PipeNetworkType {

    public ResourceLocation networkID;
    public Function<LevelPipeNet, PipeNet> netConstructor;

    public PipeNetworkType(ResourceLocation id, Function<LevelPipeNet, PipeNet> netConstructor) {
        networkID = id;
        this.netConstructor = netConstructor;
    }

    @Override
    public String toString() {
        return networkID.toString();
    }
}
