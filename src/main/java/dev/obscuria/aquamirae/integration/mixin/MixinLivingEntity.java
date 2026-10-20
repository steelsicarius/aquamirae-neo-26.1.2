package dev.obscuria.aquamirae.integration.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.obscuria.aquamirae.api.common.entity.INoHurtWalkAnim;
import dev.obscuria.aquamirae.integration.hook.HookLivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.WalkAnimationState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    // MC 26.1.2: actuallyHurt gained the ServerLevel parameter.
    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;gameEvent(Lnet/minecraft/core/Holder;)V"))
    private void actuallyHurt$onGameEvent(ServerLevel level, DamageSource damageSource, float damageAmount, CallbackInfo ci) {
        HookLivingEntity.onHurt((LivingEntity) (Object) this, damageSource);
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("RETURN"))
    private void addEffect$onReturn(MobEffectInstance effectInstance, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        HookLivingEntity.onEffectAdded((LivingEntity) (Object) this, effectInstance);
    }

    // MC 26.1.2: onEffectRemoved(MobEffectInstance) became
    // onEffectsRemoved(Collection<MobEffectInstance>).
    @Inject(method = "onEffectsRemoved", at = @At("HEAD"))
    private void onEffectsRemoved$onHead(Collection<MobEffectInstance> effects, CallbackInfo ci) {
        for (var effectInstance : effects)
            HookLivingEntity.onEffectRemoved((LivingEntity) (Object) this, effectInstance);
    }

    // MC 26.1.2: hurt(DamageSource, float) -> hurtServer(ServerLevel, DamageSource, float);
    // the shield check isDamageSourceBlocked became applyItemBlocking.
    @ModifyVariable(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;applyItemBlocking(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)F", shift = At.Shift.BEFORE), argsOnly = true)
    private float hurtServer$modifyDamageAmount(float amount, @Local(argsOnly = true) DamageSource source) {
        return HookLivingEntity.modifyIncomingDamage((LivingEntity) (Object) this, source, amount);
    }

    @ModifyArg(method = "getDamageAfterArmorAbsorb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterAbsorb(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;FF)F"), index = 3)
    private float getDamageAfterArmorAbsorb$modifyArmorValue(float armorValue, @Local(argsOnly = true) DamageSource source) {
        return HookLivingEntity.modifyArmorValue((LivingEntity) (Object) this, source, armorValue);
    }

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    private void die$cancelDeath(DamageSource damageSource, CallbackInfo ci) {
        if (!HookLivingEntity.cancelDeath((LivingEntity) (Object) this, damageSource)) return;
        ci.cancel();
    }

    // MC 26.1.2: LivingEntity no longer drives the hurt walk animation from
    // the damage pipeline; it only comes through handleDamageEvent.
    @WrapOperation(method = "handleDamageEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/WalkAnimationState;setSpeed(F)V"))
    private void handleDamageEvent$cancelAnimation(WalkAnimationState instance, float speed, Operation<Void> original) {
        if (this instanceof INoHurtWalkAnim) return;
        original.call(instance, speed);
    }
}
