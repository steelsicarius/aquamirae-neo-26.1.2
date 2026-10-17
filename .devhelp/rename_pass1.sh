#!/bin/bash
# Global mechanical renames for the Aquamirae 1.21.1 -> 26.1.2 port
# - ResourceLocation -> Identifier (MC 26.x rename)
# - Fragmentum v1 packages -> v2
# - GeckoLib 4 (software.bernie) -> GeckoLib 5 (com.geckolib)
set -e
cd /home/goose/proyectos/Aquamirae/aquamirae-26.1.2
FILES=$(find src/main/java -name "*.java")

for f in $FILES; do
  # --- Minecraft: ResourceLocation -> Identifier ---
  sed -i 's/import net\.minecraft\.resources\.ResourceLocation;/import net.minecraft.resources.Identifier;/' "$f"
  sed -i 's/ResourceLocation/Identifier/g' "$f"
  # Identifier.of(a, b) did not exist on ResourceLocation, but ResourceLocation.of -> fromNamespaceAndPath
  sed -i 's/Identifier\.of(/Identifier.fromNamespaceAndPath(/g' "$f"

  # --- Fragmentum v1 -> v2 ---
  sed -i 's/dev\.obscuria\.fragmentum\.content\.registry\.DeferredMobEffect/dev.obscuria.fragmentum.v2.api.common.registry.Deferred/' "$f"
  sed -i 's/dev\.obscuria\.fragmentum\.content\.registry\./dev.obscuria.fragmentum.v2.api.common.registry./' "$f"
  sed -i 's/dev\.obscuria\.fragmentum\.content\.network\./dev.obscuria.fragmentum.v2.api.common.network./' "$f"
  sed -i 's/dev\.obscuria\.fragmentum\.content\.util\.easing\./dev.obscuria.fragmentum.v2.api.common./' "$f"
  sed -i 's/dev\.obscuria\.fragmentum\.content\.world\.tooltip\.GroupTooltip/dev.obscuria.fragmentum.v2.core.common._TooltipStack/' "$f"
  sed -i 's/dev\.obscuria\.fragmentum\.content\.world\.tooltip\.TooltipOptions/dev.obscuria.fragmentum.v2.api.common.richtext.RichTextOptions/' "$f"
  sed -i 's/dev\.obscuria\.fragmentum\.content\.world\.tooltip\.Tooltips/dev.obscuria.fragmentum.v2.api.common.richtext.RichText/' "$f"
  sed -i 's/dev\.obscuria\.fragmentum\.config\./dev.obscuria.fragmentum.v2.api.config./' "$f"
  sed -i 's/dev\.obscuria\.fragmentum\.client\.FragmentumClientRegistry/dev.obscuria.fragmentum.v2.api.client.FragmentumClientRegistry/' "$f"
  sed -i 's/dev\.obscuria\.fragmentum\.server\.FragmentumServerRegistry/dev.obscuria.fragmentum.v2.api.server.FragmentumServerRegistry/' "$f"
  sed -i 's/dev\.obscuria\.fragmentum\.FragmentumProxy/dev.obscuria.fragmentum.v2.api.FragmentumProxy/' "$f"
  # short-name usages
  sed -i 's/\bGroupTooltip\b/_TooltipStack/g' "$f"
  sed -i 's/\bTooltipOptions\b/RichTextOptions/g' "$f"
  sed -i 's/\bTooltips\b/RichText/g' "$f"

  # --- GeckoLib 4 -> 5 ---
  sed -i 's/software\.bernie\.geckolib\.animatable\.client\./com.geckolib.animatable.client./' "$f"
  sed -i 's/software\.bernie\.geckolib\.animatable\.instance\./com.geckolib.animatable.instance./' "$f"
  sed -i 's/software\.bernie\.geckolib\.animatable\./com.geckolib.animatable./' "$f"
  sed -i 's/software\.bernie\.geckolib\.animation\.AnimatableManager/com.geckolib.animatable.manager.AnimatableManager/' "$f"
  sed -i 's/software\.bernie\.geckolib\.animation\.EasingType/com.geckolib.animation.object.EasingType/' "$f"
  sed -i 's/software\.bernie\.geckolib\.animation\./com.geckolib.animation./' "$f"
  sed -i 's/software\.bernie\.geckolib\.cache\.object\./com.geckolib.cache.model./' "$f"
  sed -i 's/software\.bernie\.geckolib\.constant\./com.geckolib.constant./' "$f"
  sed -i 's/software\.bernie\.geckolib\.loading\.math\./com.geckolib.loading.math./' "$f"
  sed -i 's/software\.bernie\.geckolib\.model\./com.geckolib.model./' "$f"
  sed -i 's/software\.bernie\.geckolib\.renderer\.layer\.AutoGlowingGeoLayer/com.geckolib.renderer.layer.builtin.AutoGlowingGeoLayer/' "$f"
  sed -i 's/software\.bernie\.geckolib\.renderer\.layer\.BlockAndItemGeoLayer/com.geckolib.renderer.layer.builtin.BlockAndItemGeoLayer/' "$f"
  sed -i 's/software\.bernie\.geckolib\.renderer\.layer\./com.geckolib.renderer.layer./' "$f"
  sed -i 's/software\.bernie\.geckolib\.renderer\.GeoRenderer/com.geckolib.renderer.base.GeoRenderer/' "$f"
  sed -i 's/software\.bernie\.geckolib\.renderer\./com.geckolib.renderer./' "$f"
  sed -i 's/software\.bernie\.geckolib\.util\./com.geckolib.util./' "$f"
  sed -i 's/software\.bernie\.geckolib\./com.geckolib./' "$f"
done
echo "Rename pass complete"
