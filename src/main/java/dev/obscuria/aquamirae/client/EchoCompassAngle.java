package dev.obscuria.aquamirae.client;

import com.mojang.serialization.MapCodec;
import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.NeedleDirectionHelper;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * MC 26.1.2: ClampedItemPropertyFunction / CompassItemPropertyFunction were
 * removed. Item angle properties are data-driven now
 * (RangeSelectItemModelProperty, registered through
 * RegisterRangeSelectItemModelPropertyEvent and referenced from the client item
 * definition JSON). This is the port of the old Echo Compass property: it aims
 * the needle at the GlobalPos stored in the ECHO_TRACKER component.
 */
public final class EchoCompassAngle extends NeedleDirectionHelper implements RangeSelectItemModelProperty {

    public static final MapCodec<EchoCompassAngle> MAP_CODEC = MapCodec.unit(new EchoCompassAngle());

    private final NeedleDirectionHelper.Wobbler wobbler;
    private final NeedleDirectionHelper.Wobbler noTargetWobbler;
    private final RandomSource random = RandomSource.create();

    public EchoCompassAngle() {
        super(true);
        this.wobbler = this.newWobbler(0.8F);
        this.noTargetWobbler = this.newWobbler(0.8F);
    }

    @Override
    public MapCodec<? extends RangeSelectItemModelProperty> type() {
        return MAP_CODEC;
    }

    @Override
    protected float calculate(ItemStack itemStack, ClientLevel level, int seed, ItemOwner owner) {
        @Nullable GlobalPos target = itemStack.get(AquamiraeDataComponents.ECHO_TRACKER);
        long gameTime = level.getGameTime();
        if (!isValidTarget(owner, target)) return this.getRandomlySpinningRotation(seed, gameTime);
        return this.getRotationTowardsTarget(owner, gameTime, target.pos());
    }

    private float getRandomlySpinningRotation(int seed, long gameTime) {
        if (this.noTargetWobbler.shouldUpdate(gameTime)) {
            this.noTargetWobbler.update(gameTime, this.random.nextFloat());
        }
        float targetRotation = this.noTargetWobbler.rotation() + hash(seed) / 2.1474836E9F;
        return Mth.positiveModulo(targetRotation, 1.0F);
    }

    private float getRotationTowardsTarget(ItemOwner owner, long gameTime, BlockPos targetPos) {
        float angleToTarget = (float) getAngleFromOwnerToPos(owner, targetPos);
        float ownerYRotation = getWrappedVisualRotationY(owner);
        float targetRotation;
        if (owner.asLivingEntity() instanceof net.minecraft.world.entity.player.Player player
                && player.isLocalPlayer() && player.level().tickRateManager().runsNormally()) {
            if (this.wobbler.shouldUpdate(gameTime)) {
                this.wobbler.update(gameTime, 0.5F - (ownerYRotation - 0.25F));
            }
            targetRotation = angleToTarget + this.wobbler.rotation();
        } else {
            targetRotation = 0.5F - (ownerYRotation - 0.25F - angleToTarget);
        }
        return Mth.positiveModulo(targetRotation, 1.0F);
    }

    private static boolean isValidTarget(ItemOwner owner, @Nullable GlobalPos position) {
        return position != null
                && position.dimension() == owner.level().dimension()
                && !(position.pos().distToCenterSqr(owner.position()) < 1.0E-5F);
    }

    private static double getAngleFromOwnerToPos(ItemOwner owner, BlockPos position) {
        Vec3 target = Vec3.atCenterOf(position);
        Vec3 ownerPosition = owner.position();
        return Math.atan2(target.z() - ownerPosition.z(), target.x() - ownerPosition.x()) / (float) (Math.PI * 2);
    }

    private static float getWrappedVisualRotationY(ItemOwner owner) {
        return Mth.positiveModulo(owner.getVisualRotationYInDegrees() / 360.0F, 1.0F);
    }

    private static int hash(int input) {
        return input * 1327217883;
    }
}
