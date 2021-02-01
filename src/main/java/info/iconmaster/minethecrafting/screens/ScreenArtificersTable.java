package info.iconmaster.minethecrafting.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import info.iconmaster.minethecrafting.MineTheCrafting;
import info.iconmaster.minethecrafting.containers.ContainerArtificersTable;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenArtificersTable extends ContainerScreen<ContainerArtificersTable> {
        private static ResourceLocation BACKGROUND = new ResourceLocation(
                        MineTheCrafting.MOD_ID + ":textures/screen/artificing_table_bg.png");

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
                                ContainerArtificersTable.GUI_HEIGHT, ContainerArtificersTable.GUI_WIDTH,
                                ContainerArtificersTable.GUI_HEIGHT);
        }

        @Override
        public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
                this.renderBackground(matrixStack);
                super.render(matrixStack, mouseX, mouseY, partialTicks);
                this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        }
}
