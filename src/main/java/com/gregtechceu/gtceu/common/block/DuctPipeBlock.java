package com.gregtechceu.gtceu.common.block;

import com.gregtechceu.gtceu.api.block.PipeBlock;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.forge.GTCapability;
import com.gregtechceu.gtceu.api.machine.feature.IEnvironmentalHazardCleaner;
import com.gregtechceu.gtceu.api.machine.feature.IEnvironmentalHazardEmitter;
import com.gregtechceu.gtceu.api.pipenet.PipeBlockEntity;
import com.gregtechceu.gtceu.api.pipenet.PipeNetworkType;
import com.gregtechceu.gtceu.client.model.PipeModel;
import com.gregtechceu.gtceu.client.renderer.block.PipeBlockRenderer;
import com.gregtechceu.gtceu.common.data.GTBlockEntities;
import com.gregtechceu.gtceu.common.pipelike.GTPipeNetworks;
import com.gregtechceu.gtceu.common.pipelike.duct.DuctPipeProperties;
import com.gregtechceu.gtceu.common.pipelike.duct.DuctPipeType;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DuctPipeBlock extends PipeBlock<DuctPipeType, DuctPipeProperties> {

    private final DuctPipeProperties properties;

    public DuctPipeBlock(Properties properties, DuctPipeType type) {
        super(properties, type, new DuctPipeProperties(type.getRateMultiplier()), type.createPipeModel());
        this.properties = new DuctPipeProperties(type.getRateMultiplier());
    }

    @Override
    public BlockEntityType<? extends PipeBlockEntity<DuctPipeType, DuctPipeProperties>> getBlockEntityType() {
        return GTBlockEntities.DUCT_PIPE.get();
    }

    @Override
    public PipeNetworkType getPipeType() {
        return GTPipeNetworks.DUCT;
    }

    @Override
    public boolean canPipeConnectToBlock(PipeBlockEntity<DuctPipeType, DuctPipeProperties> selfTile, Direction side,
                                         @Nullable BlockEntity tile) {
        return tile != null &&
                (tile.getCapability(GTCapability.CAPABILITY_HAZARD_CONTAINER, side.getOpposite()).isPresent() ||
                        tile instanceof MetaMachineBlockEntity metaMachine &&
                                (metaMachine.getMetaMachine() instanceof IEnvironmentalHazardCleaner ||
                                        metaMachine.getMetaMachine() instanceof IEnvironmentalHazardEmitter));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip,
                                TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("gtceu.duct_pipe.transfer_rate",
                this.pipeType.modifyProperties(this.properties).getTransferRate()));
    }
}
