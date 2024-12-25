package net.mathimomos.erythrium.datagen;

import net.mathimomos.erythrium.Erythrium;
import net.mathimomos.erythrium.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Erythrium.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.ERYTHRIUM_BLOCK.get(),
                    ModBlocks.ERYTHRIUM_TILES_BLOCK.get(),
                    ModBlocks.REDDISH_DEEPSLATE_BLOCK.get(),
                    ModBlocks.REDDISH_DEEPSLATE_TILES_BLOCK.get(),
                    ModBlocks.ERYTHRIUM_ORE.get(),
                    ModBlocks.DEEPSLATE_ERYTHRIUM_ORE.get(),
                    ModBlocks.COMPACTED_ERYTHRIUM.get());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.ERYTHRIUM_BLOCK.get(),
                        ModBlocks.ERYTHRIUM_TILES_BLOCK.get(),
                        ModBlocks.REDDISH_DEEPSLATE_BLOCK.get(),
                        ModBlocks.REDDISH_DEEPSLATE_TILES_BLOCK.get(),
                        ModBlocks.ERYTHRIUM_ORE.get(),
                        ModBlocks.DEEPSLATE_ERYTHRIUM_ORE.get(),
                        ModBlocks.COMPACTED_ERYTHRIUM.get());
    }
}
