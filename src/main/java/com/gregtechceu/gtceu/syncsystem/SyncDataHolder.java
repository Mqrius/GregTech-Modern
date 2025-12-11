package com.gregtechceu.gtceu.syncsystem;

import com.gregtechceu.gtceu.GTCEu;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.util.*;

/**
 * Class that holds all sync info for an {@link ISyncManaged} object.
 */
public class SyncDataHolder {

    private final ClassSyncData syncData;
    private final ISyncManaged holder;

    private final ObjectSet<String> dirtySyncFields = new ObjectOpenHashSet<>();
    private boolean resyncAll = false;

    public SyncDataHolder(@NotNull ISyncManaged o) {
        holder = o;
        syncData = ClassSyncData.CACHE.get(o.getClass());
    }

    /**
     * Instructs the sync system that this field has been updated and must be synced with clients.
     * 
     * @param fieldName The field that has changed.
     */
    public void markClientSyncFieldDirty(String fieldName) {
        dirtySyncFields.add(fieldName);
        holder.markAsChanged();
    }

    public void resyncAllFields() {
        resyncAll = true;
        holder.markAsChanged();
    }

    public CompoundTag serializeNBT(boolean writeClientFields) {
        CompoundTag tag = serializeNBT(writeClientFields, resyncAll);
        resyncAll = false;
        dirtySyncFields.clear();
        return tag;
    }

    @SuppressWarnings("unchecked")
    public CompoundTag serializeNBT(boolean writeClientFields, boolean fullSync) {
        Map<String, ClassSyncData.FieldSyncData> fieldsToSerialize;
        if (!writeClientFields) {
            fieldsToSerialize = syncData.serverSaveFields;
        } else {
            fieldsToSerialize = syncData.clientSyncFields;
        }

        CompoundTag tag = new CompoundTag();
        for (var fieldEntry : fieldsToSerialize.entrySet()) {

            if (writeClientFields &&
                    (!fullSync && !dirtySyncFields.contains(fieldEntry.getKey()) && !fieldEntry.getValue().isComplex))
                continue;
            var field = fieldEntry.getValue();
            if (field.isCustomData) {
                try {
                    Object result = field.nbtSaveModifiers[0].invoke(holder, new CompoundTag(), writeClientFields);
                    tag.put(field.nbtSaveKey, (Tag) result);
                } catch (Throwable e) {
                    GTCEu.LOGGER.error("Error while invoking nbtSaveModifier for field {}", field.fieldName);
                    GTCEu.LOGGER.error(e.getMessage());
                    return new CompoundTag();
                }
                continue;
            }

            Tag nbtValue;
            Object currentValue = field.handle.get(holder);

            if (!field.isComplex && currentValue == null) {
                var nullCompound = new CompoundTag();
                nullCompound.putBoolean("null", true);
                tag.put(field.nbtSaveKey, nullCompound);
                continue;
            }

            try {
                if (field.transformer != null) {
                    if (writeClientFields) nbtValue = ((IValueTransformer<Object>) field.transformer)
                            .serializeClientSyncNBT(currentValue, holder);
                    else nbtValue = ((IValueTransformer<Object>) field.transformer).serializeNBT(currentValue, holder);
                } else if (field.isComplex && currentValue instanceof ISyncManaged syncObj) {
                    nbtValue = syncObj.getSyncDataHolder().serializeNBT(writeClientFields);
                } else {
                    nbtValue = new CompoundTag();
                }
            } catch (Exception e) {
                GTCEu.LOGGER.error("Sync: Failed to serialise field {}", field.fieldName);
                GTCEu.LOGGER.error(e);
                continue;
            }

            for (MethodHandle modifier : field.nbtSaveModifiers) {
                try {
                    nbtValue = (Tag) modifier.invoke(holder, nbtValue, writeClientFields);
                } catch (Throwable e) {
                    GTCEu.LOGGER.error("Error while invoking nbtSaveModifier for field {}", field.fieldName);
                    GTCEu.LOGGER.error(e.getMessage());
                    return new CompoundTag();
                }
            }

            tag.put(field.nbtSaveKey, nbtValue);
        }
        return tag;
    }

    @SuppressWarnings("unchecked")
    public void deserializeNBT(CompoundTag tag, boolean readingClientFields) {
        Map<String, ClassSyncData.FieldSyncData> fieldsToCheck = readingClientFields ? syncData.clientSyncFields :
                syncData.serverSaveFields;

        for (var fieldEntry : fieldsToCheck.entrySet()) {
            var field = fieldEntry.getValue();

            if (field.isCustomData) {
                try {
                    field.nbtLoadModifiers[0].invoke(holder, tag, readingClientFields);
                } catch (Throwable e) {
                    GTCEu.LOGGER.error("Error while invoking nbtLoadModifier for field {}", field.fieldName);
                    GTCEu.LOGGER.error(e.getMessage());
                    return;
                }
                continue;
            }

            Tag savedValue = tag.get(field.nbtSaveKey);
            Object currentVal = field.handle.get(holder);

            if (savedValue == null || savedValue instanceof CompoundTag compound && compound.isEmpty()) continue;

            if (savedValue instanceof CompoundTag compound && compound.getBoolean("null")) {
                field.handle.set(holder, null);
                continue;
            }

            try {
                if (field.transformer != null) {
                    IValueTransformer<Object> transformer = (IValueTransformer<Object>) field.transformer;
                    if (transformer.mustProvideObject()) {
                        if (readingClientFields)
                            transformer.deserializeClientNBT(savedValue, holder, field.handle.get(holder));
                        else transformer.deserializeNBT(savedValue, holder, field.handle.get(holder));
                    } else {
                        try {
                            if (readingClientFields)
                                field.handle.set(holder, transformer.deserializeClientNBT(savedValue, holder, null));
                            else field.handle.set(holder, transformer.deserializeNBT(savedValue, holder, null));
                        } catch (UnsupportedOperationException e) {
                            GTCEu.LOGGER.error("Sync error: failed to perform VarHandle set: unsupported op {} {}",
                                    field.fieldName, field.handle.toString());
                        }
                    }
                } else if (field.isComplex && savedValue instanceof CompoundTag compound) {
                    if (currentVal == null) {
                        GTCEu.LOGGER.error("Sync error: ISyncManaged field was null, cannot instantiate {}",
                                field.fieldName);
                        return;
                    }
                    if (currentVal instanceof ISyncManaged syncObj)
                        syncObj.getSyncDataHolder().deserializeNBT(compound, readingClientFields);
                }
            } catch (Exception e) {
                GTCEu.LOGGER.error("Sync: Failed to deserialise field {}", field.fieldName);
                GTCEu.LOGGER.error(e);
                continue;
            }

            for (MethodHandle modifier : field.nbtLoadModifiers) {
                try {
                    modifier.invoke(holder, savedValue, readingClientFields);
                } catch (Throwable e) {
                    GTCEu.LOGGER.error("Error while invoking nbtLoadModifier for field {}", field.fieldName);
                    GTCEu.LOGGER.error(e.getMessage());
                    return;
                }
            }

            if (readingClientFields && field.triggerClientRerender) {
                holder.scheduleRenderUpdate();
            }
        }
        holder.onSaveDataLoaded();
    }
}
