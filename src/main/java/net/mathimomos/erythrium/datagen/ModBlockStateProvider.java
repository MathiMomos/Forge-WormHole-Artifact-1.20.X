package net.mathimomos.erythrium.datagen;


import net.mathimomos.erythrium.Erythrium;
import net.mathimomos.erythrium.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Erythrium.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.ERYTHRIUM_BLOCK);
        blockWithItem(ModBlocks.ERYTHRIUM_TILES_BLOCK);
        blockWithItem(ModBlocks.REDDISH_DEEPSLATE_BLOCK);
        blockWithItem(ModBlocks.REDDISH_DEEPSLATE_TILES_BLOCK);
        blockWithItem(ModBlocks.ERYTHRIUM_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_ERYTHRIUM_ORE);
        blockWithItem(ModBlocks.COMPACTED_ERYTHRIUM);

    }
    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
