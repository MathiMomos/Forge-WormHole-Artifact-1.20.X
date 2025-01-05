package net.mathimomos.wormhole_artifact.client.screen;

import net.mathimomos.wormhole_artifact.WormholeArtifact;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class WormholeArtifactButton extends Button {
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(WormholeArtifact.MOD_ID,"textures/gui/wormhole_artifact_button.png");
    private static final ResourceLocation BUTTON_TEXTURE_HOVERED = new ResourceLocation(WormholeArtifact.MOD_ID,"textures/gui/wormhole_artifact_button_hovered.png");

    protected WormholeArtifactButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, Button.DEFAULT_NARRATION);
    }


    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        ResourceLocation texture = this.isHovered() ? BUTTON_TEXTURE_HOVERED : BUTTON_TEXTURE;

        pGuiGraphics.blit(texture, this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), 160, 30);

        Font font = Minecraft.getInstance().font;
        int textX = this.getX() + (this.getWidth() - font.width(this.getMessage()) + 25) / 2;
        int textY = this.getY() + (this.getHeight() - 8) / 2;
        pGuiGraphics.drawString(font, this.getMessage().getString(), textX, textY, 0xFFFFFF);
    }
}
