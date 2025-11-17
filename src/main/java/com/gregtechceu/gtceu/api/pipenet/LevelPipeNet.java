package com.gregtechceu.gtceu.api.pipenet;

import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class LevelPipeNet extends SavedData {

    public static LevelPipeNet getLevelPipeNet(ServerLevel sLvl, PipeNetworkType type) {
        return sLvl.getDataStorage().computeIfAbsent(tag -> new LevelPipeNet(sLvl, type),
                () -> new LevelPipeNet(sLvl, type), "gt_pipenet_ " + type.networkID.getPath());
    }

    private final ServerLevel serverLevel;
    private final PipeNetworkType networkType;
    protected List<PipeNet> pipeNets = new ArrayList<>();
    protected final Map<ChunkPos, List<PipeNet>> pipeNetsByChunk = new HashMap<>();

    public LevelPipeNet(ServerLevel serverLevel, PipeNetworkType networkType) {
        this.serverLevel = serverLevel;
        this.networkType = networkType;
    }

    public ServerLevel getWorld() {
        return serverLevel;
    }

    public void addNode(BlockPos nodePos, int openConnections, boolean isActive) {
        PipeNet myPipeNet = null;
        Node node = new Node(openConnections, isActive);
        for (Direction facing : GTUtil.DIRECTIONS) {
            BlockPos offsetPos = nodePos.relative(facing);
            PipeNet pipeNet = getNetFromPos(offsetPos);
            Node secondNode = pipeNet == null ? null : pipeNet.getAllNodes().get(offsetPos);
            if (pipeNet != null && pipeNet.canNodesConnect(secondNode, facing.getOpposite(), node, null)) {
                if (myPipeNet == null) {
                    myPipeNet = pipeNet;
                    myPipeNet.addNode(nodePos, node);
                } else if (myPipeNet != pipeNet) {
                    myPipeNet.uniteNetworks(pipeNet);
                }
            }

        }
        if (myPipeNet == null) {
            myPipeNet = networkType.netConstructor.apply(this);
            myPipeNet.addNode(nodePos, node);
            addPipeNet(myPipeNet);
        }
    }

    protected void addPipeNetToChunk(ChunkPos chunkPos, PipeNet pipeNet) {
        this.pipeNetsByChunk.computeIfAbsent(chunkPos, any -> new ArrayList<>()).add(pipeNet);
    }

    protected void removePipeNetFromChunk(ChunkPos chunkPos, PipeNet pipeNet) {
        List<PipeNet> list = this.pipeNetsByChunk.get(chunkPos);
        if (list != null) list.remove(pipeNet);
        if (list != null && list.isEmpty()) this.pipeNetsByChunk.remove(chunkPos);
    }

    public void removeNode(BlockPos nodePos) {
        PipeNet pipeNet = getNetFromPos(nodePos);
        if (pipeNet != null) {
            pipeNet.removeNode(nodePos);
        }
    }

    public void updateBlockedConnections(BlockPos nodePos, Direction side, boolean isBlocked) {
        PipeNet pipeNet = getNetFromPos(nodePos);
        if (pipeNet != null) {
            pipeNet.updateBlockedConnections(nodePos, side, isBlocked);
            pipeNet.onPipeConnectionsUpdate();
        }
    }

    public <T extends PipeNet> T getNetFromPos(BlockPos blockPos) {
        List<PipeNet> pipeNetsInChunk = pipeNetsByChunk.getOrDefault(new ChunkPos(blockPos), Collections.emptyList());
        for (PipeNet pipeNet : pipeNetsInChunk) {
            if (pipeNet.containsNode(blockPos))
                return (T) pipeNet;
        }
        return null;
    }

    protected void addPipeNet(PipeNet pipeNet) {
        if (pipeNet.getNetworkType() != networkType)
            throw new IllegalArgumentException("Attempted to add pipenet of type %s to the level network for %s"
                    .formatted(pipeNet.getNetworkType(), networkType));
        this.pipeNets.add(pipeNet);
        pipeNet.getContainedChunks().forEach(chunkPos -> addPipeNetToChunk(chunkPos, pipeNet));
        pipeNet.isValid = true;
    }

    protected void removePipeNet(PipeNet pipeNet) {
        this.pipeNets.remove(pipeNet);
        pipeNet.getContainedChunks().forEach(chunkPos -> removePipeNetFromChunk(chunkPos, pipeNet));
        pipeNet.isValid = false;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compound) {
        return compound;
    }
}
