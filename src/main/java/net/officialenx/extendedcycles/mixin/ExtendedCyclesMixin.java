package net.officialenx.extendedcycles.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.server.network.ServerPlayerEntity;
import net.officialenx.extendedcycles.callbacks.OnPlayerWakeUp;

@Mixin(ServerPlayerEntity.class)
public class ExtendedCyclesMixin {
    @Inject(at = @At("TAIL"), method = "wakeUp")
    private void onWakeUp(CallbackInfo info) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        OnPlayerWakeUp.EVENT.invoker().interact(player);
    }
}