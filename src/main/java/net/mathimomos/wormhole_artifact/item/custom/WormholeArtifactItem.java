package net.mathimomos.wormhole_artifact.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class WormholeArtifactItem extends Item {

    private static final int COOLDOWN_TIME_TICKS = 100;
    private static final String TARGET_PLAYER_NAME = "Dev";

    public WormholeArtifactItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide() && pPlayer instanceof ServerPlayer) {
            ServerPlayer pTargetPlayer = getTargetPlayer(pLevel);

            if (pTargetPlayer != null) {
                tpPlayerToTarget(pPlayer, pTargetPlayer);

                itemstack.hurtAndBreak(1, pPlayer, p -> p.broadcastBreakEvent(pUsedHand));

                pPlayer.getCooldowns().addCooldown(this, COOLDOWN_TIME_TICKS);

                sendTpMessage(pPlayer, pTargetPlayer, pLevel);
            } else {
                System.out.println("Couldn't find target player");
            }
        }
        return InteractionResultHolder.success(itemstack);
    }

    private ServerPlayer getTargetPlayer(Level pLevel) {
        return pLevel.getServer().getPlayerList().getPlayerByName(TARGET_PLAYER_NAME);
    }

    private void tpPlayerToTarget(Player pPlayer, ServerPlayer ptargetPlayer) {
        double targetX = ptargetPlayer.getX();
        double targetY = ptargetPlayer.getY();
        double targetZ = ptargetPlayer.getZ();
        pPlayer.teleportTo(targetX, targetY, targetZ);
    }

    private void sendTpMessage(Player pPlayer, ServerPlayer ptargetPlayer, Level pLevel) {
        String playerName = pPlayer.getDisplayName().getString();
        String targetName = ptargetPlayer.getDisplayName().getString();
        Component message = Component.translatable("text.wormhole_artifact.teleported", playerName, targetName)
                .setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW));
        pLevel.players().forEach(player -> player.sendSystemMessage(message));
    }

    private void wormholeArtifactTp() {

    }
}
