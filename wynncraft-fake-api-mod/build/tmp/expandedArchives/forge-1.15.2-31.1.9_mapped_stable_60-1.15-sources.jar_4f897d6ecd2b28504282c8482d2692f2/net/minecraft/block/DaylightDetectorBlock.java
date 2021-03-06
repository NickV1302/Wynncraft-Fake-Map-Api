package net.minecraft.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.DaylightDetectorTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class DaylightDetectorBlock extends ContainerBlock {
   public static final IntegerProperty POWER = BlockStateProperties.POWER_0_15;
   public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;
   protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);

   public DaylightDetectorBlock(Block.Properties properties) {
      super(properties);
      this.setDefaultState(this.stateContainer.getBaseState().with(POWER, Integer.valueOf(0)).with(INVERTED, Boolean.valueOf(false)));
   }

   public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
      return SHAPE;
   }

   public boolean func_220074_n(BlockState state) {
      return true;
   }

   public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
      return blockState.get(POWER);
   }

   public static void updatePower(BlockState p_196319_0_, World p_196319_1_, BlockPos p_196319_2_) {
      if (p_196319_1_.dimension.hasSkyLight()) {
         int i = p_196319_1_.func_226658_a_(LightType.SKY, p_196319_2_) - p_196319_1_.getSkylightSubtracted();
         float f = p_196319_1_.getCelestialAngleRadians(1.0F);
         boolean flag = p_196319_0_.get(INVERTED);
         if (flag) {
            i = 15 - i;
         } else if (i > 0) {
            float f1 = f < (float)Math.PI ? 0.0F : ((float)Math.PI * 2F);
            f = f + (f1 - f) * 0.2F;
            i = Math.round((float)i * MathHelper.cos(f));
         }

         i = MathHelper.clamp(i, 0, 15);
         if (p_196319_0_.get(POWER) != i) {
            p_196319_1_.setBlockState(p_196319_2_, p_196319_0_.with(POWER, Integer.valueOf(i)), 3);
         }

      }
   }

   public ActionResultType func_225533_a_(BlockState p_225533_1_, World p_225533_2_, BlockPos p_225533_3_, PlayerEntity p_225533_4_, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
      if (p_225533_4_.isAllowEdit()) {
         if (p_225533_2_.isRemote) {
            return ActionResultType.SUCCESS;
         } else {
            BlockState blockstate = p_225533_1_.cycle(INVERTED);
            p_225533_2_.setBlockState(p_225533_3_, blockstate, 4);
            updatePower(blockstate, p_225533_2_, p_225533_3_);
            return ActionResultType.SUCCESS;
         }
      } else {
         return super.func_225533_a_(p_225533_1_, p_225533_2_, p_225533_3_, p_225533_4_, p_225533_5_, p_225533_6_);
      }
   }

   public BlockRenderType getRenderType(BlockState state) {
      return BlockRenderType.MODEL;
   }

   public boolean canProvidePower(BlockState state) {
      return true;
   }

   public TileEntity createNewTileEntity(IBlockReader worldIn) {
      return new DaylightDetectorTileEntity();
   }

   protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
      builder.add(POWER, INVERTED);
   }
}