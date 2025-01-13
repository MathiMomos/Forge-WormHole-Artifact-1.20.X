package net.mathimomos.wormhole_artifact.client.screen;

import net.mathimomos.wormhole_artifact.WormholeArtifact;
import net.mathimomos.wormhole_artifact.server.message.PlayerData;
import net.mathimomos.wormhole_artifact.server.message.PlayerListRequestMessage;
import net.mathimomos.wormhole_artifact.server.message.TeleportToTargetMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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

    private static final int GUI_WIDTH = 184;
    private static final int GUI_HEIGHT = 216;
    private static final int BUTTONS_PER_PAGE = 6;
    private static final int BUTTON_WIDTH = 160;
    private static final int BUTTON_HEIGHT = 30;
    private static final int BUTTON_GAP = 32;

    private List<PlayerData> playerDataList;
    private int tickCount = 20;

    private int scrollIndex = 0;

    private int centerX;
    private int centerY;
    private int startX;
    private int startY;

    public WormholeArtifactScreen() {
        super(Component.translatable("item.wormhole_artifact.wormhole_artifact_screen"));
        this.playerDataList = new ArrayList<>();
    }


    @Override
    protected void init() {
        super.init();

        centerX = (this.width - GUI_WIDTH) / 2;
        centerY = (this.height - GUI_HEIGHT) / 2;
        startX = centerX + 12;
        startY = centerY + 20;

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
        graphics.drawString(this.font, this.title.getString(), titleX, centerY + 6, 0x3F3F3F, false);

        super.render(graphics, mouseX, mouseY, partialTick);

        Minecraft.getInstance();
        for (int i = scrollIndex; i < Math.min(scrollIndex + BUTTONS_PER_PAGE, playerDataList.size()); i++) {
            PlayerData playerData = playerDataList.get(i);
            int buttonY = startY + ((i - scrollIndex) * BUTTON_GAP);
            int headX = startX + 7;
            int headY = buttonY + 7;

            PlayerInfo playerInfo = minecraft.getConnection().getPlayerInfo(playerData.getPlayerName());
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
    public boolean isPauseScreen() {
        return false;
    }
}
