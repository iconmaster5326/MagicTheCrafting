package info.iconmaster.minethecrafting.blocks;

import javax.annotation.Nullable;

import info.iconmaster.minethecrafting.tes.TileEntityManaTap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class BlockManaTap extends Block {

  public BlockManaTap() {
    super(Block.Properties.create(Material.ANVIL).hardnessAndResistance(5).harvestTool(ToolType.PICKAXE)
        .setRequiresTool());
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileEntityManaTap();
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
      Hand handIn, BlockRayTraceResult hit) {
    if (!worldIn.isRemote) {
      INamedContainerProvider inamedcontainerprovider = this.getContainer(state, worldIn, pos);
      if (inamedcontainerprovider != null) {
        player.openContainer(inamedcontainerprovider);
      }
    }
    return ActionResultType.SUCCESS;
  }

  @Override
  @Nullable
  public INamedContainerProvider getContainer(BlockState state, World world, BlockPos pos) {
    TileEntity tileentity = world.getTileEntity(pos);
    return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider) tileentity : null;
  }

  @Override
  public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      TileEntity tileentity = worldIn.getTileEntity(pos);

      if (tileentity instanceof TileEntityManaTap) {
        InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityManaTap) tileentity);
      }

      super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
  }
}
