package com.gregtechceu.gtceu.common.pipelike.duct;

import com.gregtechceu.gtceu.api.pipenet.LevelPipeNet;
import com.gregtechceu.gtceu.api.pipenet.PipeNet;
import com.gregtechceu.gtceu.common.pipelike.GTPipeNetworks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.*;

public class DuctPipeNet extends PipeNet {

    private final Map<BlockPos, List<DuctRoutePath>> NET_DATA = new HashMap<>();

    public DuctPipeNet(LevelPipeNet levelPipeNet) {
        super(levelPipeNet, GTPipeNetworks.DUCT);
    }

    public List<DuctRoutePath> getNetData(BlockPos pipePos, Direction facing) {
        List<DuctRoutePath> data = NET_DATA.get(pipePos);
        if (data == null) {
            data = DuctNetWalker.createNetData(this, pipePos, facing);
            if (data == null) {
                // walker failed, don't cache so it tries again on next insertion
                return Collections.emptyList();
            }
            NET_DATA.put(pipePos, data);
        }
        return data;
    }
}
