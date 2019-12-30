package net.kyrptonaught.linkedstorage.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.linkedstorage.LinkedStorageModClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
@Environment(EnvType.CLIENT)
public class DummyStorageBlockEntityRenderer  extends BlockEntityRenderer<DummyStorageBlockEntity> {
    private static final Identifier WOOL_TEXTURE = new Identifier("textures/block/white_wool.png");

    public DummyStorageBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);

    }

    @Override
    public void render(DummyStorageBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        byte[] dyes = blockEntity.getChannel();
        float[] color1 = DyeColor.byId(dyes[0]).getColorComponents();
        float[] color2 = DyeColor.byId(dyes[1]).getColorComponents();
        float[] color3 = DyeColor.byId(dyes[2]).getColorComponents();

        LinkedChestModel model = new LinkedChestModel();

        //matrices.push();
        SpriteIdentifier spriteIdentifier = new SpriteIdentifier(TexturedRenderLayers.CHEST_ATLAS_TEXTURE, LinkedStorageModClient.TEXTURE);
        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);
        model.render(matrices, vertexConsumer, light, overlay);
        model.button1.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(WOOL_TEXTURE)), light, overlay, color1[0], color1[1], color1[2], 1);
        model.button2.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(WOOL_TEXTURE)), light, overlay, color2[0], color2[1], color2[2], 1);
        model.button3.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(WOOL_TEXTURE)), light, overlay, color3[0], color3[1], color3[2], 1);
       // matrices.pop();
    }
}