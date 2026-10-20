package dev.obscuria.aquamirae.registry;

import com.mojang.serialization.Codec;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.fragmentum.v2.api.common.registry.Deferred;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

/**
 * MC 26.1.2: NeoForge forbids injecting synced entity data from mixins
 * (CommonHooks#verifyEntityDataAccessorRegistration), so the per-player
 * Remnant Saber bonus moved from a SynchedEntityData field in MixinPlayer to
 * a synced + serialized data attachment.
 */
public final class AquamiraeAttachments {

    public static final Deferred<AttachmentType<Float>> REMNANT_SABER_BONUS = register("remnant_saber_bonus",
            () -> AttachmentType.<Float>builder(() -> 0f)
                    .serialize(Codec.FLOAT.fieldOf("value"))
                    .copyOnDeath()
                    .sync(ByteBufCodecs.FLOAT)
                    .build());

    @SuppressWarnings("unchecked")
    private static <T> Deferred<AttachmentType<T>> register(String name, Supplier<AttachmentType<T>> supplier) {
        return (Deferred<AttachmentType<T>>) (Object) AquamiraeRegistries.REGISTRAR
                .register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Aquamirae.id(name),
                        (Supplier<AttachmentType<?>>) (Object) supplier);
    }

    static void init() {}
}
