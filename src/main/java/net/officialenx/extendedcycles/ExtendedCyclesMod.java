package net.officialenx.extendedcycles;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonObject;
import com.oroarmor.config.Config;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.officialenx.extendedcycles.callbacks.OnPlayerWakeUp;

public class ExtendedCyclesMod implements ModInitializer {
	public static Config CONFIG = new ExtendedCyclesConfig();

	private int extendedTime = 0;
	public static Map<Integer, Float> extendedPeriods = new HashMap<>();

	@Override
	public void onInitialize() {
		System.out.println("[ExtendedCycles] Loading...");

		loadConfig();

		System.out.println("[ExtendedCycles] Ready :)");

		ServerTickEvents.END_WORLD_TICK.register(world -> doExtendTime(world));
		OnPlayerWakeUp.EVENT.register(player -> {
			onPlayerWakeUp(player);
			return ActionResult.PASS;
		});

		ServerLifecycleEvents.SERVER_STOPPED.register(instance -> CONFIG.saveConfigToFile());
	}

	private void loadConfig() {
		CONFIG.readConfigFromFile();
		CONFIG.saveConfigToFile();

		try {
			JsonObject config = CONFIG.getConfigs().get(0).toJson();

			int dayEntry = config.get("dayEntry").getAsInt();
			float dayMinutes = config.get("dayMinutes").getAsFloat();
			int nightEntry = config.get("nightEntry").getAsInt();
			float nightMinutes = config.get("nightMinutes").getAsFloat();

			extendedPeriods.put(dayEntry, dayMinutes);
			extendedPeriods.put(nightEntry, nightMinutes);
		} catch (Exception e) {
			System.out
                    .println("[ExtendedCycles] Error while reading config file, maybe it is malformed? Exiting now :(");
            e.printStackTrace();
            System.exit(0);
		}
	}

	private void doExtendTime(ServerWorld world) {
		if (world.getDimension() != world.getServer().getOverworld().getDimension())
			return;

		long worldTime = world.getTimeOfDay();
		int dayTime = (int) (worldTime % 24000L);

		ExtendedCyclesState state = ExtendedCyclesState.get(world);

		if (state == null)
			return;

		/*System.out.println("--------");
		System.out.println("worldTime: " + worldTime);
		System.out.println("state.dataWorldTime: " + state.dataWorldTime);
		System.out.println("extendedTime: " + extendedTime);
		System.out.println("state.dataExtendedTime: " + state.dataExtendedTime);*/

		if (state.dataExtendedTime > 0) {
			startExtendedPeriod(world, state.dataExtendedTime);

			if (worldTime > state.dataWorldTime && worldTime < state.dataWorldTime + 600)
				if (extendedPeriods.containsKey((int) (state.dataWorldTime % 24000L)) && extendedTime > 0
						&& state.dataWorldTime > 0) {
					worldTime = state.dataWorldTime;
					setWorldTime(world, worldTime);
				}
		}

		if (extendedTime > 0) {
			--extendedTime;

			if (extendedTime <= 0)
				endExtendedPeriod(world);

			if (worldTime != state.dataWorldTime)
				endExtendedPeriod(world);
		} else {
			for (Entry<Integer, Float> entry : extendedPeriods.entrySet()) {
				if (dayTime == entry.getKey()) {
					int ticks = entry.getValue().intValue() * 60 * 20;
					if (ticks > 0)
						startExtendedPeriod(world, ticks);
					else
						setWorldTime(world, worldTime - ticks);
				}
			}
		}

		state.dataExtendedTime = extendedTime;
		state.dataWorldTime = worldTime;
		state.markDirty();
	}

	private void onPlayerWakeUp(ServerPlayerEntity player) {
		ServerWorld world = player.getServerWorld();

		if (isInExtendedPeriod(world))
			endExtendedPeriod(world);
	}

	private void setExtendedTime(int value) {
		extendedTime = value;
	}

	private void startExtendedPeriod(ServerWorld world, int timeInTicks) {
		setExtendedTime(timeInTicks);

		ExtendedCyclesState state = ExtendedCyclesState.get(world);
		if (state != null)  {
			state.dataExtendedTime = timeInTicks;
			state.markDirty();
		}
	}

	public void endExtendedPeriod(ServerWorld world) {
		setExtendedTime(0);

		ExtendedCyclesState state = ExtendedCyclesState.get(world);
		if (state != null)  {
			state.dataExtendedTime = 0;
			state.markDirty();
		}
	}

	private boolean isInExtendedPeriod(ServerWorld world) {
		return extendedTime > 0;
	}

	private void setWorldTime(ServerWorld world, long worldTime) {
		if (world.getTimeOfDay() == worldTime || worldTime < 1)
			return;

		world.setTimeOfDay(worldTime);
	}
}
