package info.iconmaster.minethecrafting.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import info.iconmaster.minethecrafting.MineTheCrafting;
import info.iconmaster.minethecrafting.containers.ContainerSpellcraftersDesk;
import info.iconmaster.minethecrafting.tes.TileEntitySpellcraftersDesk;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenSpellcraftersDesk extends ContainerScreen<ContainerSpellcraftersDesk> {
        public static int PROGRESS_X = 38, PROGRESS_Y = 38, PROGRESS_W = 98, PROGRESS_H = 10;

        private static ResourceLocation BACKGROUND = new ResourceLocation(
                        MineTheCrafting.MOD_ID + ":textures/screen/spellcrafters_desk_bg.png"),
                        PROGRESS_BAR = new ResourceLocation(MineTheCrafting.MOD_ID
                                        + ":textures/screen/spellcrafters_desk_progress_bar.png");

        public ScreenSpellcraftersDesk(ContainerSpellcraftersDesk container, PlayerInventory inventory,
                        ITextComponent title) {
                super(container, inventory, title);
        }

        @Override
        protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float ticks, int mouseX, int mouseY) {
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

                int originX = width / 2 - ContainerSpellcraftersDesk.GUI_WIDTH / 2,
                                originY = height / 2 - ContainerSpellcraftersDesk.GUI_HEIGHT / 2;

                getMinecraft().getTextureManager().bindTexture(BACKGROUND);
                blit(ms, originX, originY, 0, 0, ContainerSpellcraftersDesk.GUI_WIDTH,
                                ContainerSpellcraftersDesk.GUI_HEIGHT, ContainerSpellcraftersDesk.GUI_WIDTH,
                                ContainerSpellcraftersDesk.GUI_HEIGHT);

                // float percentProgress = (((float) container.teData.progress)
                //                 / TileEntitySpellcraftersDesk.MAX_PROGRESS);
                // int barHeight = (int) (PROGRESS_H * percentProgress);
                // getMinecraft().getTextureManager().bindTexture(PROGRESS_BAR);
                // blit(ms, originX + PROGRESS_X, originY + PROGRESS_Y + (PROGRESS_H - barHeight), 0,
                //                 PROGRESS_H - barHeight, PROGRESS_W, barHeight, PROGRESS_W, PROGRESS_H);
        }

        @Override
        public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
                this.renderBackground(matrixStack);
                super.render(matrixStack, mouseX, mouseY, partialTicks);
                this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        }
}
