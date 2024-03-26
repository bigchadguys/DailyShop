package bigchadguys.dailyshop.block;

import bigchadguys.dailyshop.block.entity.DailyShopBlockEntity;
import bigchadguys.dailyshop.init.ModBlocks;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.List;

public class DailyShopBlock extends BlockWithEntity implements InventoryProvider {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public DailyShopBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES[state.get(FACING).ordinal() - 2];
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(FACING, context.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DailyShopBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlocks.Entities.DAILY_SHOP.get(), DailyShopBlockEntity::tick);
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        return world.getBlockEntity(pos) instanceof SidedInventory inventory ? inventory : null;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(world.isClient() || !(player instanceof ServerPlayerEntity serverPlayer)) {
            return ActionResult.SUCCESS;
        }

        if(world.getBlockEntity(pos) instanceof ExtendedMenuProvider menu) {
            MenuRegistry.openExtendedMenu(serverPlayer, menu);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if(!world.isClient && world.getBlockEntity(pos) instanceof DailyShopBlockEntity shop) {
            if(player.isCreative() && !shop.isEmpty()) {
                ItemStack stack = new ItemStack(ModBlocks.DAILY_SHOP.get());
                shop.setStackNbt(stack);

                ItemEntity item = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
                item.setToDefaultPickupDelay();
                world.spawnEntity(item);
            }
        }

        super.onBreak(world, pos, state, player);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        List<ItemStack> drops = super.getDroppedStacks(state, builder);

        if(builder.getOptional(LootContextParameters.BLOCK_ENTITY) instanceof DailyShopBlockEntity shop) {
            ItemStack stack = new ItemStack(ModBlocks.DAILY_SHOP.get());
            shop.setStackNbt(stack);
            drops.add(stack);
        }

        return drops;
    }

    private static final VoxelShape[] SHAPES = {
            VoxelShapes.union(
                    Block.createCuboidShape(0.5, 6, 1.5, 1.5, 11, 2.5),
                    Block.createCuboidShape(14.5, 6, 1.5, 15.5, 11, 2.5),
                    Block.createCuboidShape(0.5, 6, 14.5, 1.5, 14, 15.5),
                    Block.createCuboidShape(14.5, 6, 14.5, 15.5, 14, 15.5),

                    VoxelShapes.combine(
                            Block.createCuboidShape(0, 0, 1, 16, 6, 16),
                            Block.createCuboidShape(2, 0, 3, 14, 6, 16),
                            BooleanBiFunction.ONLY_FIRST),
                    VoxelShapes.combine(
                            Block.createCuboidShape(2, 0, 15, 14, 5, 16),
                            Block.createCuboidShape(6, 0, 15, 10, 5, 16),
                            BooleanBiFunction.ONLY_FIRST),

                    Block.createCuboidShape(0, 11, 0, 16, 13, 4),
                    Block.createCuboidShape(0, 12, 4, 16, 14, 8),
                    Block.createCuboidShape(0, 13, 8, 16, 15, 12),
                    Block.createCuboidShape(0, 14, 12, 16, 16, 16)),
            VoxelShapes.union(
                    Block.createCuboidShape(0.5, 6, 0.5, 1.5, 14, 1.5),
                    Block.createCuboidShape(14.5, 6, 0.5, 15.5, 14, 1.5),
                    Block.createCuboidShape(0.5, 6, 13.5, 1.5, 11, 14.5),
                    Block.createCuboidShape(14.5, 6, 13.5, 15.5, 11, 14.5),

                    VoxelShapes.combine(
                            Block.createCuboidShape(0, 0, 0, 16, 6, 15),
                            Block.createCuboidShape(2, 0, 0, 14, 6, 13),
                            BooleanBiFunction.ONLY_FIRST),
                    VoxelShapes.combine(
                            Block.createCuboidShape(2, 0, 0, 14, 5, 1),
                            Block.createCuboidShape(6, 0, 0, 10, 5, 1),
                            BooleanBiFunction.ONLY_FIRST),

                    Block.createCuboidShape(0, 11, 12, 16, 13, 16),
                    Block.createCuboidShape(0, 12, 8, 16, 14, 12),
                    Block.createCuboidShape(0, 13, 4, 16, 15, 8),
                    Block.createCuboidShape(0, 14, 0, 16, 16, 4)),
            VoxelShapes.union(
                    Block.createCuboidShape(1.5, 6, 0.5, 2.5, 11, 1.5),
                    Block.createCuboidShape(14.5, 6, 0.5, 15.5, 14, 1.5),
                    Block.createCuboidShape(1.5, 6, 14.5, 2.5, 11, 15.5),
                    Block.createCuboidShape(14.5, 6, 14.5, 15.5, 14, 15.5),

                    VoxelShapes.combine(
                            Block.createCuboidShape(1, 0, 0, 16, 6, 16),
                            Block.createCuboidShape(3, 0, 2, 16, 6, 14),
                            BooleanBiFunction.ONLY_FIRST),
                    VoxelShapes.combine(
                            Block.createCuboidShape(15, 0, 2, 16, 5, 14),
                            Block.createCuboidShape(15, 0, 6, 16, 5, 10),
                            BooleanBiFunction.ONLY_FIRST),

                    Block.createCuboidShape(0, 11, 0, 4, 13, 16),
                    Block.createCuboidShape(4, 12, 0, 8, 14, 16),
                    Block.createCuboidShape(8, 13, 0, 12, 15, 16),
                    Block.createCuboidShape(12, 14, 0, 16, 16, 16)),
            VoxelShapes.union(
                    Block.createCuboidShape(0.5, 6, 0.5, 1.5, 14, 1.5),
                    Block.createCuboidShape(13.5, 6, 0.5, 14.5, 11, 1.5),
                    Block.createCuboidShape(0.5, 6, 14.5, 1.5, 14, 15.5),
                    Block.createCuboidShape(13.5, 6, 14.5, 14.5, 11, 15.5),

                    VoxelShapes.combine(
                            Block.createCuboidShape(0, 0, 0, 15, 6, 16),
                            Block.createCuboidShape(0, 0, 2, 13, 6, 14),
                            BooleanBiFunction.ONLY_FIRST),
                    VoxelShapes.combine(
                            Block.createCuboidShape(0, 0, 2, 1, 5, 14),
                            Block.createCuboidShape(0, 0, 6, 1, 5, 10),
                            BooleanBiFunction.ONLY_FIRST),

                    Block.createCuboidShape(12, 11, 0, 16, 13, 16),
                    Block.createCuboidShape(8, 12, 0, 12, 14, 16),
                    Block.createCuboidShape(4, 13, 0, 8, 15, 16),
                    Block.createCuboidShape(0, 14, 0, 4, 16, 16))
    };

}
