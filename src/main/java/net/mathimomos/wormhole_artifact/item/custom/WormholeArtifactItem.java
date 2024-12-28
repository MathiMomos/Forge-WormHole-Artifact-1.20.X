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

    private static final int COOLDOWN_TIME = 100;

    public WormholeArtifactItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide() && pPlayer instanceof ServerPlayer) {
            itemstack.hurtAndBreak(1, pPlayer, p -> p.broadcastBreakEvent(pUsedHand));

            String playerName = pPlayer.getDisplayName().getString();
            Component message = Component.translatable("text.wormhole_artifact.teleported", playerName).setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW));
            pLevel.players().forEach(player -> player.sendSystemMessage(message));
        }
        return InteractionResultHolder.success(itemstack);
    }
}
