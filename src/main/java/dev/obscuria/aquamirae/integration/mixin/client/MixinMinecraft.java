package dev.obscuria.aquamirae.integration.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.obscuria.aquamirae.integration.hook.client.HookMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.Music;
import net.minecraft.world.attribute.BackgroundMusic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Inject(method = "setLevel(Lnet/minecraft/client/ClientLevel;)V", at = @At("HEAD"))
    private void setLevel$onHead(ClientLevel level, CallbackInfo ci) {
        HookMinecraft.onSetLevel((Minecraft) (Object) this);
    }

    @Inject(method = "runTick(Z)V", at = @At("TAIL"))
    private void runTick$onTail(boolean advanceGameTime, CallbackInfo ci) {
        HookMinecraft.onRunTick((Minecraft) (Object) this);
    }

    @Inject(method = "getSituationalMusic", at = @At("HEAD"), cancellable = true)
    private void getSituationalMusic$onHead(CallbackInfoReturnable<Music> cir) {
        HookMinecraft.supplyBossMusic((Minecraft) (Object) this, cir::setReturnValue);
    }

    /**
     * MC 26.1.2: situational music comes from the environment attribute
     * BACKGROUND_MUSIC now (the old Level.getBiome call is gone), so the
     * gameplay-music hook wraps BackgroundMusic.select instead.
     */
    @WrapOperation(method = "getSituationalMusic",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/attribute/BackgroundMusic;select(ZZ)Ljava/util/Optional;"))
    private java.util.Optional<Music> getSituationalMusic$supplyGameplayMusic(
            BackgroundMusic instance, boolean isCreative, boolean isUnderwater, Operation<java.util.Optional<Music>> original
    ) {
        var supplied = HookMinecraft.gameplayMusic((Minecraft) (Object) this);
        if (supplied != null) return java.util.Optional.of(supplied);
        return original.call(instance, isCreative, isUnderwater);
    }

    @Inject(method = "getSituationalMusic", at = @At("RETURN"), cancellable = true)
    private void getSituationalMusic$onReturn(CallbackInfoReturnable<Music> cir) {
        HookMinecraft.supplyMenuMusic((Minecraft) (Object) this, cir.getReturnValue(), cir::setReturnValue);
    }
}
