package net.mathimomos.erythrium.util;

import net.mathimomos.erythrium.Erythrium;
import net.mathimomos.erythrium.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class ModToolTiers {
    public static final Tier ERYTHRIUM = TierSortingRegistry.registerTier(
            new ForgeTier(3,1250,5f,4f,12,
                    ModTags.Blocks.NEEDS_ERYTHRIUM_TOOL, () -> Ingredient.of(ModItems.ERYTHRIUM.get())),
            new ResourceLocation(Erythrium.MOD_ID, "erythrium"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND)
    );
}
