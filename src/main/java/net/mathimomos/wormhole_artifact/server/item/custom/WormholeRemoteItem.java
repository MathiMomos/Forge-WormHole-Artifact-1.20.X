package net.mathimomos.wormhole_artifact.server.item.custom;

import net.mathimomos.wormhole_artifact.WormholeArtifact;
import net.mathimomos.wormhole_artifact.client.particle.ModParticles;
import net.mathimomos.wormhole_artifact.client.sound.ModSounds;
import net.mathimomos.wormhole_artifact.config.ModConfigs;
import net.mathimomos.wormhole_artifact.server.item.ModItems;
import net.mathimomos.wormhole_artifact.server.message.OpenWormholeArtifactScreenMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

public class WormholeRemoteItem extends Item {

    public WormholeRemoteItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack pStack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide) {
            if (pPlayer instanceof ServerPlayer serverPlayer) {
                OpenWormholeArtifactScreenMessage message = new OpenWormholeArtifactScreenMessage();
                WormholeArtifact.NETWORK_WRAPPER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), message);
            }
        }

        return InteractionResultHolder.sidedSuccess(pStack, pLevel.isClientSide());
    }

    @Override
    public boolean isValidRepairItem(ItemStack stackToRepair, ItemStack repairMaterial) {
        return repairMaterial.getItem() == ModItems.ENDER_NACRE.get() || super.isValidRepairItem(stackToRepair, repairMaterial);
    }

    public void teleportToTarget(ServerPlayer pPlayer, ServerPlayer pTargetPlayer, ItemStack pStack, Level pLevel) {
        if (pTargetPlayer != null && pTargetPlayer.isAlive()) {
            ServerLevel targetLevel = (ServerLevel) pTargetPlayer.level();

            teleportEffect(pPlayer, pLevel);

            pStack.hurtAndBreak(1, pPlayer, p -> p.broadcastBreakEvent(p.getUsedItemHand()));
            pPlayer.getCooldowns().addCooldown(this, WormholeArtifact.COMMON_CONFIG.WORMHOLE_REMOTE_COOLDOWN.get() * 20);

            String pPlayerName = pPlayer.getDisplayName().getString();
            String pTargetPlayerName = pTargetPlayer.getDisplayName().getString();
            pLevel.players().forEach(player -> player.sendSystemMessage(
                    Component.translatable("text.wormhole_artifact.teleported", pPlayerName, pTargetPlayerName)
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW))
            ));

            pPlayer.teleportTo(targetLevel, pTargetPlayer.getX(), pTargetPlayer.getY(), pTargetPlayer.getZ(), pTargetPlayer.getYRot(), pTargetPlayer.getXRot());
            teleportEffect(pTargetPlayer, targetLevel);
        }
    }

    public void teleportEffect(ServerPlayer pPlayer, Level pLevel) {
        if (pLevel instanceof ServerLevel serverLevel) {
            double spacing = 0.75;

            for (int i = 0; i < 3; i++) {
                double x = pPlayer.getX();
                double z = pPlayer.getZ();
                double y = pPlayer.getY() + i * spacing;

                serverLevel.sendParticles(
                        ModParticles.TELEPORT_WAVE_PARTICLES.get(),
                        x, y, z,
                        1,
                        0, 0, 0,
                        0.1f
                );
            }

            for (int j = 0; j < 30; j++) {
                double x = pPlayer.getX();
                double z = pPlayer.getZ();
                double y = pPlayer.getY();

                serverLevel.sendParticles(
                        ModParticles.TELEPORT_PARTICLES.get(),
                        x, y, z,
                        1,
                        0.5, 0.5, 0.5,
                        1f
                );
            }

            serverLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                    ModSounds.TELEPORT_SFX.get(), SoundSource.PLAYERS, 2.0F, 1.0F);
        }
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return WormholeArtifact.COMMON_CONFIG.WORMHOLE_REMOTE_MAX_DURABILITY.get();
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        int maxDamage = getMaxDamage(stack);
        if (damage >= maxDamage) {
            damage = maxDamage - 1;
        }
        super.setDamage(stack, damage);
    }
}

