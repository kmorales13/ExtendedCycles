package net.officialenx.extendedcycles.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public interface OnPlayerWakeUp {
    Event<OnPlayerWakeUp> EVENT = EventFactory.createArrayBacked(OnPlayerWakeUp.class, (listeners) -> (player) -> {
        for (OnPlayerWakeUp listener : listeners) {
            ActionResult result = listener.interact(player);

            if (result != ActionResult.PASS) {
                return result;
            }
        }

        return ActionResult.PASS;
    });

    ActionResult interact(ServerPlayerEntity player);
}