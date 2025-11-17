package com.gregtechceu.gtceu.common.pipelike.cable;

import com.gregtechceu.gtceu.api.pipenet.LevelPipeNet;
import com.gregtechceu.gtceu.api.pipenet.PipeNet;
import com.gregtechceu.gtceu.common.pipelike.GTPipeNetworks;

import net.minecraft.core.BlockPos;

import java.util.*;

public class EnergyNet extends PipeNet {

    private final Map<BlockPos, List<EnergyRoutePath>> NET_DATA = new HashMap<>();

    public EnergyNet(LevelPipeNet world) {
        super(world, GTPipeNetworks.ENERGY);
    }

    public List<EnergyRoutePath> getNetData(BlockPos pipePos) {
        List<EnergyRoutePath> data = NET_DATA.get(pipePos);
        if (data == null) {
            data = EnergyNetWalker.createNetData(this, pipePos);
            if (data == null) {
                // walker failed, don't cache so it tries again on next insertion
                return Collections.emptyList();
            }
            data.sort(Comparator.comparingInt(EnergyRoutePath::getDistance));
            NET_DATA.put(pipePos, data);
        }
        return data;
    }
}
