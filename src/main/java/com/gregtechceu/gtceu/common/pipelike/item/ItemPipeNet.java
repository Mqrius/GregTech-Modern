package com.gregtechceu.gtceu.common.pipelike.item;

import com.gregtechceu.gtceu.api.pipenet.LevelPipeNet;
import com.gregtechceu.gtceu.api.pipenet.PipeNet;
import com.gregtechceu.gtceu.common.pipelike.GTPipeNetworks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.*;

public class ItemPipeNet extends PipeNet {

    private final Map<BlockPos, List<ItemRoutePath>> NET_DATA = new HashMap<>();

    public ItemPipeNet(LevelPipeNet world) {
        super(world, GTPipeNetworks.ITEM);
    }

    public List<ItemRoutePath> getNetData(BlockPos pipePos, Direction facing) {
        List<ItemRoutePath> data = NET_DATA.get(pipePos);
        if (data == null) {
            data = ItemNetWalker.createNetData(this, pipePos, facing);
            if (data == null) {
                // walker failed, don't cache so it tries again on next insertion
                return Collections.emptyList();
            }
            data.sort(Comparator.comparingInt(inv -> inv.getProperties().getPriority()));
            NET_DATA.put(pipePos, data);
        }
        return data;
    }
}
