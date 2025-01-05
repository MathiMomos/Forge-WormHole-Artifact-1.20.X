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

    private static final ResourceLocation GUI_TEXTURE =
            new ResourceLocation(WormholeArtifact.MOD_ID, "textures/gui/wormhole_artifact_gui.png");

    private static final int GUI_WIDTH = 184;
    private static final int GUI_HEIGHT = 216;
    private static final int BUTTONS_PER_PAGE = 6;
    private static final int BUTTON_WIDTH = 160;
    private static final int BUTTON_HEIGHT = 30;
    private static final int BUTTON_GAP = 32;

    private final List<String> playerNames;
    private int scrollIndex = 0;

    private int centerX;
    private int centerY;
    private int startX;
    private int startY;

    public WormholeArtifactScreen(List<String> playerNames) {
        super(Component.translatable("item.wormhole_artifact.wormhole_artifact_screen"));
        this.playerNames = playerNames;
    }

    @Override
    protected void init() {
        super.init();

        centerX = (this.width - GUI_WIDTH) / 2;
        centerY = (this.height - GUI_HEIGHT) / 2;
        startX = centerX + 12;
        startY = centerY + 20;

        updateButtons();
    }

    private void updateButtons() {
        this.clearWidgets();

        for (int i = scrollIndex; i < Math.min(scrollIndex + BUTTONS_PER_PAGE, playerNames.size()); i++) {
            String playerName = playerNames.get(i);
            int buttonY = startY + ((i - scrollIndex) * BUTTON_GAP);

            this.addRenderableWidget(new WormholeArtifactButton(
                    startX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT,
                    Component.literal(playerName),
                    button -> onPlayerSelected(playerName)
            ));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        graphics.blit(GUI_TEXTURE, centerX, centerY, 0, 0, GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH, GUI_HEIGHT);

        int titleX = centerX + (GUI_WIDTH - this.font.width(this.title.getString())) / 2;
        graphics.drawString(this.font, this.title.getString(), titleX, centerY + 6, 0x3F3F3F, false);

        super.render(graphics, mouseX, mouseY, partialTick);

        Minecraft.getInstance();
        for (int i = scrollIndex; i < Math.min(scrollIndex + BUTTONS_PER_PAGE, playerNames.size()); i++) {
            String playerName = playerNames.get(i);
            int buttonY = startY + ((i - scrollIndex) * BUTTON_GAP);
            int headX = startX + 7;
            int headY = buttonY + 7;

            PlayerInfo playerInfo = minecraft.getConnection().getPlayerInfo(playerName);
            if (playerInfo != null) {
                GameProfile profile = playerInfo.getProfile();
                ResourceLocation skin = minecraft.getSkinManager().getInsecureSkinLocation(profile);

                graphics.blit(skin, headX, headY, 16, 16, 8, 8, 8, 8, 64, 64);
            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (Minecraft.getInstance().options.keyInventory.matches(keyCode, scanCode)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (delta > 0 && scrollIndex > 0) {
            scrollIndex--;
        } else if (delta < 0 && scrollIndex + BUTTONS_PER_PAGE < playerNames.size()) {
            scrollIndex++;
        }
        updateButtons();
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    private void onPlayerSelected(String targetPlayerName) {
        WormholeArtifact.NETWORK_WRAPPER.sendToServer(new TeleportToTargetMessage(targetPlayerName));
        this.onClose();
    }
}