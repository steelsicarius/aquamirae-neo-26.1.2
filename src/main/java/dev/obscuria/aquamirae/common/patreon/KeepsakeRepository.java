package dev.obscuria.aquamirae.common.patreon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Map;

public record KeepsakeRepository(
        Map<Identifier, KeepsakeArmor> keepsakes
) {

    public static final Codec<KeepsakeRepository> CODEC;
    public static final KeepsakeRepository EMPTY;

    static {
        EMPTY = new KeepsakeRepository(Map.of());
        CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Codec.unboundedMap(Identifier.CODEC, KeepsakeArmor.CODEC).fieldOf("keepsakes").forGetter(KeepsakeRepository::keepsakes)
        ).apply(builder, KeepsakeRepository::new));
    }
}
