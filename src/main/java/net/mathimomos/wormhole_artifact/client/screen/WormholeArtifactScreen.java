package net.mathimomos.wormhole_artifact.client.screen;

import com.mojang.authlib.GameProfile;
import net.mathimomos.wormhole_artifact.WormholeArtifact;
import net.mathimomos.wormhole_artifact.server.message.TeleportToTargetMessage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class WormholeArtifactScreen extends Screen {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(WormholeArtifact.MOD_ID, "textures/gui/wormhole_artifact_gui.png");

    private final List<String> pPlayers;
    private final int guiWidth = 160;
    private final int guiHeight = 216;
    private int scrollIndex = 0;

    private static final int BUTTONS_PER_PAGE = 6;

    public WormholeArtifactScreen(List<String> pPlayers) {
        super(Component.translatable("item.wormhole_artifact.wormhole_artifact_screen"));
        this.pPlayers = pPlayers;
    }

    @Override
    protected void init() {
        super.init();

        updateButtons();
    }

    private void updateButtons() {
        this.clearWidgets();

        int centerX = (this.width - guiWidth) / 2;
        int centerY = (this.height - guiHeight) / 2;

        int startX = centerX + 8;
        int startY = centerY + 40;

        int buttonWidth = 144;
        int buttonHeight = 30;
        int gap = 35;

        for (int i = scrollIndex; i < Math.min(scrollIndex + BUTTONS_PER_PAGE, pPlayers.size()); i++) {
            String pPlayerName = pPlayers.get(i);
            int buttonY = startY + ((i - scrollIndex) * gap);

            this.addRenderableWidget(new WormholeArtifactButton(
                    startX, buttonY, buttonWidth, buttonHeight, Component.literal(pPlayerName),
                    btn -> onPlayerSelected(pPlayerName)
            ));
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);

        int centerX = (this.width - guiWidth) / 2;
        int centerY = (this.height - guiHeight) / 2;

        pGuiGraphics.blit(GUI_TEXTURE, centerX, centerY, 0, 0, guiWidth, guiHeight, guiWidth, guiHeight);

        int textWidth = this.font.width(this.title.getString());
        int x = (this.width - textWidth) / 2;
        pGuiGraphics.drawString(this.font, this.title.getString(), x, centerY + 10, 0x3F3F3F, false);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        int startX = centerX + 8;
        int startY = centerY + 40;

        for (int i = scrollIndex; i < Math.min(scrollIndex + BUTTONS_PER_PAGE, pPlayers.size()); i++) {
            String pPlayerName = pPlayers.get(i);
            int buttonY = startY + ((i - scrollIndex) * 35);
            int headY = buttonY + 7;
            int headX = startX + 7;

            PlayerInfo playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(pPlayerName);
            if (playerInfo != null) {
                GameProfile gameProfile = playerInfo.getProfile();
                ResourceLocation skinLocation = Minecraft.getInstance().getSkinManager().getInsecureSkinLocation(gameProfile);

                pGuiGraphics.blit(skinLocation, headX, headY, 16, 16, 8, 8, 8, 8, 64, 64);
            }
        }
    }
    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        KeyMapping keyInventory = Minecraft.getInstance().options.keyInventory;

        if (keyInventory.matches(pKeyCode, pScanCode)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (pDelta > 0) {
            if (scrollIndex > 0) {
                scrollIndex--;
                updateButtons();
            }
        } else if (pDelta < 0) {
            if (scrollIndex + BUTTONS_PER_PAGE < pPlayers.size()) {
                scrollIndex++;
                updateButtons();
            }
        }
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    private void onPlayerSelected(String pTargetPlayerName) {
        TeleportToTargetMessage pMessage = new TeleportToTargetMessage(pTargetPlayerName);
        WormholeArtifact.NETWORK_WRAPPER.sendToServer(pMessage);

        this.onClose();
    }
}
