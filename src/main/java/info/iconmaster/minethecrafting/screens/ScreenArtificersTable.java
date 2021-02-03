package info.iconmaster.minethecrafting.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import info.iconmaster.minethecrafting.MineTheCrafting;
import info.iconmaster.minethecrafting.containers.ContainerArtificersTable;
import info.iconmaster.minethecrafting.tes.TileEntityArtificersTable;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenArtificersTable extends ContainerScreen<ContainerArtificersTable> {
        public static final int PROGRESS_TOP_X = 78, PROGRESS_TOP_Y = 43, PROGRESS_BOTTOM_X = 76,
                        PROGRESS_BOTTOM_Y = 84, PROGRESS_W = 17, PROGRESS_H = 19;
        public static final ResourceLocation BACKGROUND = new ResourceLocation(MineTheCrafting.MOD_ID,
                        "textures/screen/artificing_table_bg.png"),
                        PROGRESS_TOP = new ResourceLocation(MineTheCrafting.MOD_ID,
                                        "textures/screen/artificing_table_progress_bar_upper.png"),
                        PROGRESS_BOTTOM = new ResourceLocation(MineTheCrafting.MOD_ID,
                                        "textures/screen/artificing_table_progress_bar_lower.png");

        public ScreenArtificersTable(ContainerArtificersTable container, PlayerInventory inventory,
                        ITextComponent title) {
                super(container, inventory, title);

                titleY -= ContainerArtificersTable.HEIGHT_OFFSET;
                playerInventoryTitleY = 144 - ContainerArtificersTable.HEIGHT_OFFSET;
        }

        @Override
        protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float ticks, int mouseX, int mouseY) {
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

                int originX = width / 2 - ContainerArtificersTable.GUI_WIDTH / 2,
                                originY = height / 2 - ContainerArtificersTable.GUI_HEIGHT / 2;

                getMinecraft().getTextureManager().bindTexture(BACKGROUND);
                blit(ms, originX, originY, 0, 0, ContainerArtificersTable.GUI_WIDTH,
                                ContainerArtificersTable.GUI_HEIGHT, 256, 256);

                float percentProgress = (((float) container.teData.progress) / TileEntityArtificersTable.MAX_PROGRESS);
                int barHeight = (int) (PROGRESS_H * percentProgress);

                getMinecraft().getTextureManager().bindTexture(PROGRESS_TOP);
                blit(ms, originX + PROGRESS_TOP_X, originY + PROGRESS_TOP_Y, 0, 0, PROGRESS_W, barHeight, PROGRESS_W,
                                PROGRESS_H);

                getMinecraft().getTextureManager().bindTexture(PROGRESS_BOTTOM);
                blit(ms, originX + PROGRESS_BOTTOM_X, originY + PROGRESS_BOTTOM_Y + (PROGRESS_H - barHeight), 0,
                                PROGRESS_H - barHeight, PROGRESS_W, barHeight, PROGRESS_W, PROGRESS_H);
        }

        @Override
        public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
                this.renderBackground(matrixStack);
                super.render(matrixStack, mouseX, mouseY, partialTicks);
                this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        }
}
