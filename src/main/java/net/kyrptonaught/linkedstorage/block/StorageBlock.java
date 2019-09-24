package net.kyrptonaught.linkedstorage.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.item.LinkingCard;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class StorageBlock extends Block implements BlockEntityProvider, InventoryProvider {
    static BlockEntityType<StorageBlockEntity> blockEntity;

    public StorageBlock(Settings block$Settings_1) {
        super(block$Settings_1);
        Registry.register(Registry.BLOCK, new Identifier(LinkedStorageMod.MOD_ID, "storageblock"), this);
        Registry.register(Registry.ITEM, new Identifier(LinkedStorageMod.MOD_ID, "storageblock"), new BlockItem(this, new Item.Settings().group(ItemGroup.REDSTONE)));
        blockEntity = Registry.register(Registry.BLOCK_ENTITY, LinkedStorageMod.MOD_ID + ":storageblock", BlockEntityType.Builder.create(StorageBlockEntity::new, this).build(null));
    }

    private String getChannel(World world, BlockPos pos) {
        StorageBlockEntity sbe = (StorageBlockEntity) world.getBlockEntity(pos);
        return sbe.getChannel();
    }

    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        if (!world.isClient) {
            ItemStack stack = player.inventory.getMainHandStack();
            if (stack.getItem() instanceof LinkingCard) {
                ((LinkingCard) stack.getItem()).useOnStorageBlock(stack, (StorageBlockEntity) world.getBlockEntity(pos), player);
            } else {
                String channel = getChannel(world, pos);
                ContainerProviderRegistry.INSTANCE.openContainer(new Identifier(LinkedStorageMod.MOD_ID, "linkedstorage"), player, (buf) -> buf.writeString(channel));
            }
        }
        return true;
    }

    @Override
    public void onPlaced(World world_1, BlockPos blockPos_1, BlockState blockState_1, LivingEntity livingEntity_1, ItemStack itemStack_1) {
        if (itemStack_1.hasCustomName()) {
            BlockEntity blockEntity_1 = world_1.getBlockEntity(blockPos_1);
            if (blockEntity_1 instanceof StorageBlockEntity) {
                ((StorageBlockEntity) blockEntity_1).setChannel(itemStack_1.getName().asFormattedString());
            }
        }

    }

    @Override
    @Environment(EnvType.CLIENT)
    public ItemStack getPickStack(BlockView blockView_1, BlockPos blockPos_1, BlockState blockState_1) {
        ItemStack stack = new ItemStack(this);
        String channel = getChannel((World) blockView_1, blockPos_1);
        return channel.equals("") ? stack : stack.setCustomName(new LiteralText(channel));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView var1) {
        return new StorageBlockEntity();
    }

    @Override
    public SidedInventory getInventory(BlockState var1, IWorld var2, BlockPos var3) {
        return LinkedStorageMod.SMAN.get(var2.getLevelProperties()).getValue().getInv(getChannel((World) var2, var3));
    }
}
