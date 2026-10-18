package dev.obscuria.aquamirae.common.recipe;

import com.mojang.serialization.MapCodec;
import dev.obscuria.aquamirae.common.component.RuneDamage;
import dev.obscuria.aquamirae.common.component.RuneProtection;
import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import dev.obscuria.aquamirae.registry.AquamiraeItems;
import dev.obscuria.aquamirae.registry.AquamiraeRecipeSerializers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * MC 26.1.2: {@link SmithingRecipe} ingredients are ingredient suppliers
 * (template/addition optional, base required) and {@link RecipeSerializer} is
 * a (codec, streamCodec) record. The dynamic "base is a weapon/armor without
 * a rune" check stays in {@link #matches}.
 */
public class RuneSmithingRecipe implements SmithingRecipe {

    public static final RuneSmithingRecipe SHARED = new RuneSmithingRecipe();
    public static final RecipeSerializer<RuneSmithingRecipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);

    private static final MapCodec<RuneSmithingRecipe> CODEC = MapCodec.unit(SHARED);
    private static final StreamCodec<RegistryFriendlyByteBuf, RuneSmithingRecipe> STREAM_CODEC = StreamCodec.unit(SHARED);

    @Override
    public boolean matches(SmithingRecipeInput input, Level level) {
        return isTemplateIngredient(input.template())
                && isBaseIngredient(input.base())
                && isAdditionIngredient(input.addition());
    }

    public boolean isTemplateIngredient(ItemStack stack) {
        return stack.isEmpty();
    }

    /**
     * Base is any tool/weapon (has a TOOL component) or armor piece (has an
     * EQUIPPABLE component) that does not already carry a rune.
     */
    public boolean isBaseIngredient(ItemStack stack) {
        return (stack.has(DataComponents.TOOL) || stack.has(DataComponents.EQUIPPABLE))
                && !stack.has(AquamiraeDataComponents.RUNE_OF_THE_STORM_DAMAGE)
                && !stack.has(AquamiraeDataComponents.RUNE_OF_THE_STORM_PROTECTION);
    }

    public boolean isAdditionIngredient(ItemStack stack) {
        return stack.is(AquamiraeItems.RUNE_OF_THE_STORM.get());
    }

    @Override
    public ItemStack assemble(SmithingRecipeInput input) {
        var result = input.base().copy();
        if (result.has(DataComponents.EQUIPPABLE)) {
            result.set(AquamiraeDataComponents.RUNE_OF_THE_STORM_PROTECTION, RuneProtection.SHARED);
        } else {
            result.set(AquamiraeDataComponents.RUNE_OF_THE_STORM_DAMAGE, RuneDamage.SHARED);
        }
        return result;
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<? extends SmithingRecipe> getSerializer() {
        return AquamiraeRecipeSerializers.RUNE_SMITHING;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public Optional<Ingredient> templateIngredient() {
        return Optional.empty();
    }

    @Override
    public Ingredient baseIngredient() {
        // the real base test is dynamic (see #matches / #isBaseIngredient)
        return Ingredient.of();
    }

    @Override
    public Optional<Ingredient> additionIngredient() {
        return Optional.of(Ingredient.of(AquamiraeItems.RUNE_OF_THE_STORM.get()));
    }
}
