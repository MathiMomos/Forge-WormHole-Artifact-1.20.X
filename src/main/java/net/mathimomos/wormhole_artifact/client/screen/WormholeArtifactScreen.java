package net.mathimomos.wormhole_artifact.client.screen;

import net.mathimomos.wormhole_artifact.WormholeArtifact;
import net.mathimomos.wormhole_artifact.server.message.PlayerData;
import net.mathimomos.wormhole_artifact.server.message.PlayerListRequestMessage;
import net.mathimomos.wormhole_artifact.server.message.TeleportToTargetMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.multiplayer.PlayerInfo;
import com.mojang.authlib.GameProfile;

import java.util.ArrayList;
import java.util.List;

public class WormholeArtifactScreen extends Screen {

    private static final ResourceLocation GUI_TEXTURE =
            new ResourceLocation(WormholeArtifact.MOD_ID, "textures/gui/wormhole_artifact_gui.png");

    private static final int GUI_WIDTH = 236;
    private static final int GUI_HEIGHT = 192;
    private static final int BUTTONS_PER_PAGE = 4;
    private static final int BUTTON_WIDTH = 220;
    private static final int BUTTON_HEIGHT = 32;
    private static final int BUTTON_GAP = 0;

    private List<PlayerData> playerDataList;
    private int tickCount = 20;

    private int scrollIndex = 0;

    private int centerX;
    private int centerY;
    private int startX;
    private int startY;

    private EditBox searchBox;

    public WormholeArtifactScreen() {
        super(Component.translatable("gui.wormhole_artifact.wormhole_artifact_screen.title"));
        this.playerDataList = new ArrayList<>();
    }


    @Override
    protected void init() {
        super.init();
        //184
        centerX = (this.width - GUI_WIDTH) / 2;
        centerY = (this.height - GUI_HEIGHT) / 2;
        startX = centerX + 8;
        startY = centerY + 28;
        searchBox = new EditBox(this.font,
                centerX + 26, centerY + 12,
                198,
                12,
                Component.translatable("gui.wormhole_artifact.wormhole_artifact_screen.search_text"));
        searchBox.setMaxLength(50);
        searchBox.setBordered(true);
        searchBox.setSuggestion(Component.translatable("gui.wormhole_artifact.wormhole_artifact_screen.search_text").getString());
        searchBox.setValue("");
        this.addRenderableWidget(searchBox);

        WormholeArtifact.NETWORK_WRAPPER.sendToServer(new PlayerListRequestMessage());
        updateButtons();
    }

    @Override
    public void tick() {
        super.tick();
        tickCount++;

        if (tickCount >= 20) {
            tickCount = 0;

            WormholeArtifact.NETWORK_WRAPPER.sendToServer(new PlayerListRequestMessage());
        }
    }

    public void updatePlayerData(List<PlayerData> newPlayerDataList) {
        this.playerDataList = newPlayerDataList;
        updateButtons();
    }

    private void updateButtons() {
        this.clearWidgets();

        for (int i = scrollIndex; i < Math.min(scrollIndex + BUTTONS_PER_PAGE, playerDataList.size()); i++) {
            PlayerData playerData = playerDataList.get(i);
            int buttonY = startY + ((i - scrollIndex) * BUTTON_GAP);

            String playerName = playerData.getPlayerName();
            String playerDimension = playerData.getPlayerDimension();
            String playerDistance = Integer.toString(playerData.getPlayerDistance());

            this.addRenderableWidget(new WormholeArtifactButton(
                    startX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT,
                    playerName, playerDimension, playerDistance,
                    button -> onPlayerSelected(playerData)
            ));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        graphics.blit(GUI_TEXTURE, centerX, centerY, 0, 0, GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH, GUI_HEIGHT);

        int titleX = centerX + (GUI_WIDTH - this.font.width(this.title.getString())) / 2;
        graphics.drawString(this.font, this.title.getString(), titleX, centerY - 16, 0xFFFFFF, false);

        searchBox.render(graphics, mouseX, mouseY, partialTick);

        super.render(graphics, mouseX, mouseY, partialTick);

        for (int i = scrollIndex; i < Math.min(scrollIndex + BUTTONS_PER_PAGE, playerDataList.size()); i++) {
            PlayerData playerData = playerDataList.get(i);
            int buttonY = startY + ((i - scrollIndex) * BUTTON_GAP);
            int headX = startX + 4;
            int headY = buttonY + 4;

            PlayerInfo playerInfo = minecraft.getConnection().getPlayerInfo(playerData.getPlayerName());
            if (playerInfo != null) {
                GameProfile profile = playerInfo.getProfile();
                ResourceLocation skin = minecraft.getSkinManager().getInsecureSkinLocation(profile);

                graphics.blit(skin, headX, headY, 24, 24, 8, 8, 8, 8, 64, 64);
                graphics.blit(skin, headX, headY, 24, 24, 40, 8, 8, 8, 64, 64);
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (delta > 0 && scrollIndex > 0) {
            scrollIndex--;
        } else if (delta < 0 && scrollIndex + BUTTONS_PER_PAGE < playerDataList.size()) {
            scrollIndex++;
        }
        updateButtons();
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    private void onPlayerSelected(PlayerData targetPlayerData) {
        WormholeArtifact.NETWORK_WRAPPER.sendToServer(new TeleportToTargetMessage(targetPlayerData.getPlayerName()));
        this.onClose();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (searchBox.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (searchBox.charTyped(codePoint, modifiers)) {
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (searchBox.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (Minecraft.getInstance().options.keyInventory.matches(keyCode, scanCode)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
