package net.officialenx.extendedcycles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

public class ExtendedCyclesState extends PersistentState {
    public int dataExtendedTime = -1;
	public long dataWorldTime = -1; 

    public ExtendedCyclesState() {
        super("ExtendedCyclesState");
    }

    public void setData(int _dataExtendedTime, long _dataWorldTime) {
        dataExtendedTime = _dataExtendedTime;
        dataWorldTime = _dataWorldTime;
    }

    public static ExtendedCyclesState get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(ExtendedCyclesState::new, "ExtendedCyclesState");
    }

    @Override
    public void fromTag(CompoundTag tag) {
        dataExtendedTime = tag.getInt("dataExtendedTime");
        dataWorldTime = tag.getLong("dataWorldTime");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putInt("dataExtendedTime", dataExtendedTime);
        tag.putLong("dataWorldTime", dataWorldTime);

        return tag;
    }
}
