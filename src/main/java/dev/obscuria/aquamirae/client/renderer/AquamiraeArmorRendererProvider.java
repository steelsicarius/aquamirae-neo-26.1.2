package dev.obscuria.aquamirae.client.renderer;

import dev.obscuria.aquamirae.common.item.AquamiraeArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.renderer.GeoArmorRenderer;

public class AquamiraeArmorRendererProvider implements GeoRenderProvider {

    private final AquamiraeArmorRenderer renderer;

    public AquamiraeArmorRendererProvider(AquamiraeArmorItem armorItem) {
        this.renderer = new AquamiraeArmorRenderer(armorItem);
    }

    @Override
    public @Nullable GeoArmorRenderer<?, ?> getGeoArmorRenderer(ItemStack itemStack, EquipmentSlot equipmentSlot) {
        return this.renderer;
    }
}
