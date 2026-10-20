package dev.obscuria.aquamirae.integration.mixin;

import dev.obscuria.aquamirae.integration.hook.HookLivingEntity;
import dev.obscuria.aquamirae.integration.hook.HookPlayer;
import dev.obscuria.aquamirae.integration.extension.ExtensionPlayer;
import dev.obscuria.aquamirae.registry.AquamiraeAttachments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity implements ExtensionPlayer {

    private MixinPlayer(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    // MC 26.1.2: synced data can no longer be added from mixins; the bonus is
    // stored in the remnant_saber_bonus data attachment (serialized + synced).
    @Override
    public float aquamirae$getRemnantSaberBonus() {
        var self = (Player) (Object) this;
        return self.getData(AquamiraeAttachments.REMNANT_SABER_BONUS);
    }

    @Override
    public void aquamirae$setRemnantSaberBonus(float value) {
        var self = (Player) (Object) this;
        self.setData(AquamiraeAttachments.REMNANT_SABER_BONUS, Math.max(value, 0f));
    }

    // MC 26.1.2: actuallyHurt gained the ServerLevel parameter.
    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;gameEvent(Lnet/minecraft/core/Holder;)V"))
    private void actuallyHurt$onDamageEvent(ServerLevel level, DamageSource damageSrc, float damageAmount, CallbackInfo ci) {
        HookLivingEntity.onHurt((Player) (Object) this, damageSrc);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick$onTail(CallbackInfo ci) {
        HookPlayer.tick((Player) (Object) this);
    }

    // MC 26.1.2: isInvulnerableTo gained the ServerLevel parameter.
    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    private void isInvulnerableTo$modifyResult(ServerLevel level, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (HookPlayer.isInvulnerableTo((Player) (Object) this, source))
            cir.setReturnValue(true);
    }
}
