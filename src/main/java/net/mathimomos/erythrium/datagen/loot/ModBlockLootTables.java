package net.mathimomos.erythrium.datagen.loot;

import net.mathimomos.erythrium.block.ModBlocks;
import net.mathimomos.erythrium.item.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.ERYTHRIUM_BLOCK.get());
        this.dropSelf(ModBlocks.ERYTHRIUM_TILES_BLOCK.get());
        this.dropSelf(ModBlocks.REDDISH_DEEPSLATE_BLOCK.get());
        this.dropSelf(ModBlocks.REDDISH_DEEPSLATE_TILES_BLOCK.get());

        this.add(ModBlocks.ERYTHRIUM_ORE.get(),
                block -> createOreDrop(ModBlocks.ERYTHRIUM_ORE.get(), ModItems.ROUGH_ERYTHRIUM.get()));
        this.add(ModBlocks.DEEPSLATE_ERYTHRIUM_ORE.get(),
                block -> createOreDrop(ModBlocks.DEEPSLATE_ERYTHRIUM_ORE.get(), ModItems.ROUGH_ERYTHRIUM.get()));
        this.add(ModBlocks.COMPACTED_ERYTHRIUM.get(),
                block -> createErythriumOreDrops(ModBlocks.COMPACTED_ERYTHRIUM.get(), ModItems.ERYTHRIUM.get()));
    }

    protected LootTable.Builder createErythriumOreDrops(Block pBlock, Item pItem) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                LootItem.lootTableItem(pItem)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }


    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
