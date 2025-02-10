package net.mathimomos.wormhole_artifact.server.event;

import net.mathimomos.wormhole_artifact.WormholeArtifact;
import net.mathimomos.wormhole_artifact.server.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = WormholeArtifact.MOD_ID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onEnderPearlImpact(ProjectileImpactEvent event) {
            if (event.getProjectile() instanceof ThrownEnderpearl enderpearl) {
                Level level = enderpearl.level();

                if(level instanceof ServerLevel serverLevel) {
                    BlockPos pos = enderpearl.blockPosition();

                    double prob = new Random().nextDouble();

                    if(prob < WormholeArtifact.COMMON_CONFIG.ENDER_NACRE_DROP_PROBABILITY.get()) {
                        ItemStack enderNacreDrop = new ItemStack(ModItems.ENDER_NACRE.get());
                        serverLevel.addFreshEntity(new ItemEntity(serverLevel,
                                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, enderNacreDrop));
                    }
                }
            }
        }
    }
}
