package com.gregtechceu.gtceu.common.pipelike.optical;

import com.gregtechceu.gtceu.api.pipenet.LevelPipeNet;
import com.gregtechceu.gtceu.api.pipenet.PipeNet;
import com.gregtechceu.gtceu.common.pipelike.GTPipeNetworks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class OpticalPipeNet extends PipeNet {

    private final Map<BlockPos, OpticalRoutePath> NET_DATA = new Object2ObjectOpenHashMap<>();

    public OpticalPipeNet(LevelPipeNet world) {
        super(world, GTPipeNetworks.OPTICAL);
    }

    @Nullable
    public OpticalRoutePath getNetData(BlockPos pipePos, Direction facing) {
        if (NET_DATA.containsKey(pipePos)) {
            return NET_DATA.get(pipePos);
        }
        OpticalRoutePath data = OpticalNetWalker.createNetData(this, pipePos, facing);
        if (data == OpticalNetWalker.FAILED_MARKER) {
            // walker failed, don't cache, so it tries again on next insertion
            return null;
        }

        NET_DATA.put(pipePos, data);
        return data;
    }
}
