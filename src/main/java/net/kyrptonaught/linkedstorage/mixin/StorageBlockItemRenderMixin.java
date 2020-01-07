package net.kyrptonaught.linkedstorage.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.linkedstorage.block.StorageBlock;
import net.kyrptonaught.linkedstorage.client.DummyStorageBlockEntity;
import net.kyrptonaught.linkedstorage.util.LinkedInventoryHelper;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(BuiltinModelItemRenderer.class)
@Environment(EnvType.CLIENT)
public class StorageBlockItemRenderMixin {
    private static DummyStorageBlockEntity DUMMY = new DummyStorageBlockEntity();

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void render(ItemStack itemStack, MatrixStack matrixStack, VertexConsumerProvider consumerProvider, int light, int overlay, CallbackInfo info) {
        if (itemStack.getItem() instanceof BlockItem && ((BlockItem) itemStack.getItem()).getBlock() instanceof StorageBlock) {
            DUMMY.setChannel(LinkedInventoryHelper.getItemChannel(itemStack));
            BlockEntityRenderDispatcher.INSTANCE.renderEntity(DUMMY, matrixStack, consumerProvider, light, overlay);
            info.cancel();
        }
    }
}