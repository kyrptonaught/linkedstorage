package net.kyrptonaught.linkedstorage.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.network.OpenStoragePacket;
import net.kyrptonaught.linkedstorage.network.SetDyePacket;
import net.kyrptonaught.linkedstorage.util.DyeChannel;
import net.kyrptonaught.linkedstorage.util.LinkedInventoryHelper;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.*;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.List;

public class StorageBlock extends HorizontalFacingBlock implements BlockEntityProvider, InventoryProvider {
    public static BlockEntityType<StorageBlockEntity> blockEntity;

    public StorageBlock(Settings block$Settings_1) {
        super(block$Settings_1);
        Registry.register(Registry.BLOCK, new Identifier(LinkedStorageMod.MOD_ID, "storageblock"), this);
        blockEntity = Registry.register(Registry.BLOCK_ENTITY, LinkedStorageMod.MOD_ID + ":storageblock", BlockEntityType.Builder.create(StorageBlockEntity::new, this).build(null));
        Registry.register(Registry.ITEM, new Identifier(LinkedStorageMod.MOD_ID, "storageblock"), new BlockItem(this, new Item.Settings().group(LinkedStorageMod.GROUP)));
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    private boolean didHitButton(VoxelShape button, BlockPos pos, Vec3d hit) {
        return button.getBoundingBox().expand(.001).offset(pos.getX(), pos.getY(), pos.getZ()).contains(hit);
    }

    private boolean checkButons(BlockState state, BlockPos pos, BlockHitResult hit) {
        VoxelShape[] buttons = BUTTONS;
        if (state.get(FACING).equals(Direction.EAST) || state.get(FACING).equals(Direction.WEST))
            buttons = BUTTONSEW;
        for (int i = 0; i < buttons.length; i++)
            if (didHitButton(buttons[i], pos, hit.getPos())) {
                if (state.get(FACING).equals(Direction.NORTH) || state.get(FACING).equals(Direction.EAST)) {
                    SetDyePacket.sendPacket(2 - i, pos);
                    return true;
                } else {
                    SetDyePacket.sendPacket(i, pos);
                    return true;
                }
            }
        return false;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getMainHandStack();
        if (world.isClient)
            if (stack.getItem() instanceof DyeItem) {
                if (!checkButons(state, pos, hit))
                    OpenStoragePacket.sendPacket(pos);
            } else {
                OpenStoragePacket.sendPacket(pos);
            }
        return ActionResult.SUCCESS;

    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState blockState_1, LivingEntity livingEntity_1, ItemStack stack) {
        if (!world.isClient()) {
            LinkedInventoryHelper.setBlockChannel(LinkedInventoryHelper.getItemChannel(stack), world, pos);
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockView var1) {
        return new StorageBlockEntity();
    }

    @Override
    public SidedInventory getInventory(BlockState var1, IWorld var2, BlockPos var3) {
        return ((StorageBlockEntity) var2.getBlockEntity(var3)).getLinkedInventory();
    }

    @Environment(EnvType.CLIENT)
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        DyeChannel dyechannel = LinkedInventoryHelper.getBlockChannel((World) world, pos);
        ItemStack stack = new ItemStack(this);
        LinkedInventoryHelper.setItemChannel(dyechannel, stack);
        return stack;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    public BlockRenderType getRenderType(BlockState blockState_1) {
        return BlockRenderType.MODEL;
    }

    private VoxelShape[] BUTTONS = new VoxelShape[]{Block.createCuboidShape(4, 14, 6, 6, 15, 10),
            Block.createCuboidShape(7, 14, 6, 9, 15, 10),
            Block.createCuboidShape(10, 14, 6, 12, 15, 10)};
    private VoxelShape SHAPE = VoxelShapes.union(Block.createCuboidShape(1, 0, 1, 15, 14, 15), BUTTONS);
    private VoxelShape[] BUTTONSEW = new VoxelShape[]{Block.createCuboidShape(6, 14, 4, 10, 15, 6),
            Block.createCuboidShape(6, 14, 7, 10, 15, 9),
            Block.createCuboidShape(6, 14, 10, 10, 15, 12)};
    private VoxelShape SHAPEEW = VoxelShapes.union(Block.createCuboidShape(1, 0, 1, 15, 14, 15), BUTTONSEW);

    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext ePos) {
        if (state.get(FACING).equals(Direction.EAST) || state.get(FACING).equals(Direction.WEST))
            return SHAPEEW;
        return SHAPE;
    }

    @Environment(EnvType.CLIENT)
    public void buildTooltip(ItemStack stack, BlockView view, List<Text> tooltip, TooltipContext options) {
        DyeChannel channel = LinkedInventoryHelper.getItemChannel(stack);
        tooltip.add(new TranslatableText("text.linkeditem.channel", channel.getCleanName()).formatted(Formatting.GRAY));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}