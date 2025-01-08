package net.mathimomos.wormhole_artifact.client.screen;

import net.mathimomos.wormhole_artifact.WormholeArtifact;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class WormholeArtifactButton extends Button {
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(WormholeArtifact.MOD_ID, "textures/gui/wormhole_artifact_button.png");
    private static final ResourceLocation BUTTON_TEXTURE_HOVERED = new ResourceLocation(WormholeArtifact.MOD_ID, "textures/gui/wormhole_artifact_button_hovered.png");

    private final String playerName;
    private final String dimension;
    private final String distance;

    protected WormholeArtifactButton(int pX, int pY, int pWidth, int pHeight, String playerName, String dimension, String distance, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, Component.literal(playerName), pOnPress, Button.DEFAULT_NARRATION);
        this.playerName = playerName;
        this.dimension = dimension;
        this.distance = distance;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        ResourceLocation texture = this.isHovered() ? BUTTON_TEXTURE_HOVERED : BUTTON_TEXTURE;

        pGuiGraphics.blit(texture, this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), 160, 30);

        Font font = Minecraft.getInstance().font;

        int textNameX = this.getX() + (26 + this.getWidth() - font.width(playerName)) / 2;
        int textNameY = this.getY() + (this.getHeight() - 18) / 2;
        pGuiGraphics.drawString(font, playerName, textNameX, textNameY, 0xFFFFFF);

        String dimensionAndDistance = getDimensionName(dimension) + getDistanceNumber(distance);

        int textDimensionAndDistanceX = this.getX() + (26 + this.getWidth() - font.width(dimensionAndDistance)) / 2;
        int textDimensionAndDistanceY = textNameY + 10;

        int dimensionColor = getDimensionColor(dimension);

        pGuiGraphics.drawString(font, dimensionAndDistance, textDimensionAndDistanceX, textDimensionAndDistanceY, dimensionColor);
    }

    private int getDimensionColor(String dimension) {
        if (dimension.equals("minecraft:overworld")) {
            return 0x00AA00;
        } else if (dimension.equals("minecraft:the_nether")) {
            return 0xAA0000;
        } else if (dimension.equals("minecraft:the_end")) {
            return 0xAA00AA;
        } else if (dimension.equals("aether:the_aether")) {
            return 0x55FFFF;
        } else if (dimension.equals("twilightforest:twilight_forest")) {
            return 0xFFFF55;
        } else if (dimension.equals("deeperdarker:otherside")) {
            return 0x00AAAA;
        } else if (dimension.equals("the_bumblezone:the_bumblezone")) {
            return 0xFFAA00;
        } else if (dimension.equals("undergarden:undergarden")) {
            return 0x47A036;
        } else {
            return 0xFFFFFF;
        }
    }

    private String getDimensionName(String dimension) {
        if (dimension.equals("minecraft:overworld")) {
            return "Overworld";
        } else if (dimension.equals("minecraft:the_nether")) {
            return "The Nether";
        } else if (dimension.equals("minecraft:the_end")) {
            return "The End";
        } else if (dimension.equals("aether:the_aether")) {
            return "The Aether";
        } else if (dimension.equals("twilightforest:twilight_forest")) {
            return "Twilight Forest";
        } else if (dimension.equals("deeperdarker:otherside")) {
            return "Otherside";
        } else if (dimension.equals("the_bumblezone:the_bumblezone")) {
            return "The Bumblezone";
        } else if (dimension.equals("undergarden:undergarden")) {
            return "Undergarden";
        } else {
            return "???";
        }
    }

    private String getDistanceNumber(String distance) {
        if (distance.equals("-1")) {
            return "";
        } else {
            return " - " + distance + " m";
        }
    }
}
