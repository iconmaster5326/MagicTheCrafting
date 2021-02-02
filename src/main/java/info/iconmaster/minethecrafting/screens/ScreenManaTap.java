package info.iconmaster.minethecrafting.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import info.iconmaster.minethecrafting.Mana;
import info.iconmaster.minethecrafting.MineTheCrafting;
import info.iconmaster.minethecrafting.containers.ContainerManaTap;
import info.iconmaster.minethecrafting.tes.TileEntityManaTap;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenManaTap extends ContainerScreen<ContainerManaTap> {
        public static final int WIDTH = 176, HEIGHT = 166, OUTPUTS_X = 60, OUTPUTS_Y = 11, PROGRESS_X = 71, PROGRESS_Y = 34,
                        PROGRESS_W = 29, PROGRESS_H = 21, ICON_SIZE = 16, STATUS_Y = 58, STATUS_H = 10;

        public static final ResourceLocation BACKGROUND = new ResourceLocation(
                        MineTheCrafting.MOD_ID + ":textures/screen/manatap_bg.png"),
                        PROGRESS_BAR = new ResourceLocation(
                                        MineTheCrafting.MOD_ID + ":textures/screen/manatap_progress_bar.png"),
                        TAP = new ResourceLocation(MineTheCrafting.MOD_ID + ":textures/screen/tap.png");

        public ScreenManaTap(ContainerManaTap container, PlayerInventory inventory, ITextComponent title) {
                super(container, inventory, title);
        }

        @Override
        protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float ticks, int mouseX, int mouseY) {
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

                FontRenderer fontRenderer = getMinecraft().fontRenderer;
                int originX = width / 2 - WIDTH / 2, originY = height / 2 - HEIGHT / 2;

                getMinecraft().getTextureManager().bindTexture(BACKGROUND);
                blit(ms, originX, originY, 0, 0, WIDTH, HEIGHT, 256, 256);

                float percentProgress = (((float) container.teData.progress) / TileEntityManaTap.MAX_PROGRESS);
                int barHeight = (int) (PROGRESS_H * percentProgress);
                getMinecraft().getTextureManager().bindTexture(PROGRESS_BAR);
                blit(ms, originX + PROGRESS_X, originY + PROGRESS_Y + (PROGRESS_H - barHeight), 0,
                                PROGRESS_H - barHeight, PROGRESS_W, barHeight, PROGRESS_W, PROGRESS_H);

                String part1 = ": Add", part2 = "to your mana pool.";
                int statusIconWidth = STATUS_H + 4;
                int statusWidth = fontRenderer.getStringWidth(part1) + fontRenderer.getStringWidth(part2)
                                + statusIconWidth + statusIconWidth * container.manaGeneratable.size();
                int statusOriginX = width / 2 - statusWidth / 2;
                getMinecraft().getTextureManager().bindTexture(TAP);
                blit(ms, statusOriginX + 2, originY + STATUS_Y - 1, 0, 0, STATUS_H, STATUS_H, STATUS_H, STATUS_H);
                fontRenderer.drawString(ms, part1, statusOriginX + statusIconWidth, originY + STATUS_Y,
                                TextFormatting.DARK_GRAY.getColor());

                int i = 0;
                for (Mana mana : container.manaGeneratable) {
                        getMinecraft().getTextureManager().bindTexture(mana.fullTextureLocation());
                        blit(ms, statusOriginX + statusIconWidth + fontRenderer.getStringWidth(part1)
                                        + statusIconWidth * i + 2, originY + STATUS_Y - 1, 0, 0, STATUS_H, STATUS_H,
                                        STATUS_H, STATUS_H);
                        i++;
                }

                fontRenderer.drawString(ms, part2,
                                statusOriginX + statusIconWidth + fontRenderer.getStringWidth(part1)
                                                + statusIconWidth * container.manaGeneratable.size(),
                                originY + STATUS_Y, TextFormatting.DARK_GRAY.getColor());
        }

        @Override
        public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
                this.renderBackground(matrixStack);
                super.render(matrixStack, mouseX, mouseY, partialTicks);
                this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        }
}
