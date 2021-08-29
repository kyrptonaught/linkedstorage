package net.kyrptonaught.linkedstorage.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.linkedstorage.LinkedStorageModClient;
import net.kyrptonaught.linkedstorage.block.StorageBlock;
import net.kyrptonaught.linkedstorage.block.StorageBlockEntity;
import net.kyrptonaught.linkedstorage.util.PlayerDyeChannel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class StorageBlockRenderer implements BlockEntityRenderer<StorageBlockEntity> {
    private static final Identifier WOOL_TEXTURE = new Identifier("textures/block/white_wool.png");
    private static final Identifier DIAMOND_TEXTURE = new Identifier("textures/block/diamond_block.png");
    LinkedChestModel model;

    public StorageBlockRenderer(BlockEntityRendererFactory.Context ctx) {
        model = new LinkedChestModel(ctx);
    }

    @Override
    public void render(StorageBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        byte[] dyes = blockEntity.getChannel().dyeChannel;
        float[] color1 = DyeColor.byId(dyes[0]).getColorComponents();
        float[] color2 = DyeColor.byId(dyes[1]).getColorComponents();
        float[] color3 = DyeColor.byId(dyes[2]).getColorComponents();

        World world = blockEntity.getWorld();
        BlockPos pos = blockEntity.getPos();
        BlockState state = world.getBlockState(pos);

        //fixes crash with carpet
        if (state.getBlock() instanceof StorageBlock) {
            matrices.push();
            float f = state.get(StorageBlock.FACING).asRotation();
            matrices.translate(0.5D, 0.5D, 0.5D);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-f));
            matrices.translate(-0.5D, -0.5D, -0.5D);

            model.setLidPitch(blockEntity.getAnimationProgress(tickDelta));
            SpriteIdentifier spriteIdentifier = new SpriteIdentifier(TexturedRenderLayers.CHEST_ATLAS_TEXTURE, LinkedStorageModClient.TEXTURE);
            VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);
            model.render(matrices, vertexConsumer, light, overlay);

            model.button1.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(WOOL_TEXTURE)), light, overlay, color1[0], color1[1], color1[2], 1);
            model.button2.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(WOOL_TEXTURE)), light, overlay, color2[0], color2[1], color2[2], 1);
            model.button3.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(WOOL_TEXTURE)), light, overlay, color3[0], color3[1], color3[2], 1);

            if (blockEntity.getChannel() instanceof PlayerDyeChannel)
                model.latch.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(DIAMOND_TEXTURE)), light, overlay, 1, 1, 1, 1);
            else
                model.latch.render(matrices, vertexConsumer, light, overlay);
            matrices.pop();
        }
    }
}