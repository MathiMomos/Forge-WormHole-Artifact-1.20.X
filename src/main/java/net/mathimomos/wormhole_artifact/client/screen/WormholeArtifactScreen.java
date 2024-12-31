package net.mathimomos.wormhole_artifact.client.screen;

import net.mathimomos.wormhole_artifact.WormholeArtifact;
import net.mathimomos.wormhole_artifact.server.message.TeleportToTargetMessage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class WormholeArtifactScreen extends Screen {
    private final List<String> pPlayers;

    public WormholeArtifactScreen(List<String> pPlayers) {
        super(Component.translatable("item.wormhole_artifact.wormhole_artifact"));
        this.pPlayers = pPlayers;
    }

    @Override
    protected void init() {
        super.init();

        int startX = this.width / 2 - 100;
        int startY = this.height / 2 - 50;
        int buttonWidth = 200;
        int buttonHeight = 20;
        int gap = 25;

        for (int i = 0; i < pPlayers.size(); i++) {
            String pPlayerName = pPlayers.get(i);
            int buttonY = startY + (i * gap);

            this.addRenderableWidget(
                    Button.builder(Component.literal(pPlayerName), btn -> onPlayerSelected(pPlayerName))
                            .pos(startX, buttonY)
                            .size(buttonWidth, buttonHeight)
                            .build()
            );
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        pGuiGraphics.drawCenteredString(this.font, this.title.getString(), this.width / 2, 15, 0xFFFFFF);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    private void onPlayerSelected(String pTargetPlayerName) {
        TeleportToTargetMessage pMessage = new TeleportToTargetMessage(pTargetPlayerName);
        WormholeArtifact.NETWORK_WRAPPER.sendToServer(pMessage);

        this.onClose();
    }

}
