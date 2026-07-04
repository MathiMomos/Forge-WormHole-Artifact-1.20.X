package net.mathimomos.wormhole_artifact.client.screen;

import net.mathimomos.wormhole_artifact.WormholeArtifact;
import net.mathimomos.wormhole_artifact.server.message.PlayerData;
import net.mathimomos.wormhole_artifact.server.message.PlayerListRequestMessage;
import net.mathimomos.wormhole_artifact.server.message.TeleportToTargetMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
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
    private static final int BUTTON_GAP = 38;

    private List<PlayerData> playerDataList;
    private List<PlayerData> filteredPlayerDataList;
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
        this.filteredPlayerDataList = new ArrayList<>();
    }


    @Override
    protected void init() {
        super.init();
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
        for (GuiEventListener child : List.copyOf(this.children())) {
            if (child instanceof WormholeArtifactButton) {
                this.removeWidget(child);
            }
        }

        String searchText = searchBox.getValue().toLowerCase();
        filteredPlayerDataList = playerDataList.stream()
                .filter(p -> p.getPlayerName().toLowerCase().contains(searchText))
                .toList();

        if (scrollIndex >= filteredPlayerDataList.size()) {
            scrollIndex = Math.max(0, filteredPlayerDataList.size() - 1);
        }

        for (int i = scrollIndex; i < Math.min(scrollIndex + BUTTONS_PER_PAGE, filteredPlayerDataList.size()); i++) {
            PlayerData playerData = filteredPlayerDataList.get(i);
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

        super.render(graphics, mouseX, mouseY, partialTick);

        if (searchBox.getValue().isEmpty() && !searchBox.isFocused()) {
            graphics.drawString(this.font,
                    Component.translatable("gui.wormhole_artifact.wormhole_artifact_screen.search_text"),
                    searchBox.getX() + 4, searchBox.getY() + 2, 0x808080, false);
        }

        for (int i = scrollIndex; i < Math.min(scrollIndex + BUTTONS_PER_PAGE, filteredPlayerDataList.size()); i++) {
            PlayerData playerData = filteredPlayerDataList.get(i);
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
        } else if (delta < 0 && scrollIndex + BUTTONS_PER_PAGE < filteredPlayerDataList.size()) {
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
        if (!super.mouseClicked(mouseX, mouseY, button)) {
            searchBox.setFocused(false);
            return false;
        }
        return true;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (searchBox.charTyped(codePoint, modifiers)) {
            updateButtons();
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (Minecraft.getInstance().options.keyInventory.matches(keyCode, scanCode) && !searchBox.isFocused()) {
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
