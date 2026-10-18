package dev.obscuria.aquamirae.registry;

import com.mojang.serialization.MapCodec;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.patreon.KeepsakeArmorLoot;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

/**
 * MC 26.1.2: the loot pool entry type registry now stores the
 * {@link MapCodec} itself instead of a wrapper {@code LootPoolEntryType}.
 */
public final class AquamiraeLootPoolEntries {

    public static final MapCodec<? extends LootPoolEntryContainer> KEEPSAKE_ARMOR = register("keepsake_armor", KeepsakeArmorLoot.CODEC);

    private static MapCodec<? extends LootPoolEntryContainer> register(String name, MapCodec<? extends LootPoolEntryContainer> codec) {
        AquamiraeRegistries.REGISTRAR.register(Registries.LOOT_POOL_ENTRY_TYPE, Aquamirae.id(name), () -> codec);
        return codec;
    }

    static void init() {}
}
