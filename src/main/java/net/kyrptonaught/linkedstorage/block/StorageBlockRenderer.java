package net.kyrptonaught.linkedstorage.block;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;

public class StorageBlockRenderer extends BlockEntityRenderer<StorageBlockEntity> {

    private ModelPart button1, button2, button3;
    private static final Identifier TEXTURE = new Identifier("textures/block/white_shulker_box.png");

    private static final Quaternion RIGHTSIDE_UP = Vector3f.POSITIVE_X.getDegreesQuaternion(180);
    private static final Quaternion SOUTH_ROTAION_QUAT = Vector3f.POSITIVE_Y.getDegreesQuaternion(180);
    private static final Quaternion EAST_ROTATION_QUAT = Vector3f.POSITIVE_Y.getDegreesQuaternion(270);
    private static final Quaternion WEST_ROTATION_QUAT = Vector3f.POSITIVE_Y.getDegreesQuaternion(90);

    public StorageBlockRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
        button1 = new ModelPart(64, 64, 0, 19);
        button1.addCuboid(10.0F, 14.0F, 6.0F, 2, 1, 4, 0.0F);
        button2 = new ModelPart(64, 64, 0, 19);
        button2.addCuboid(7, 14.0F, 6.0F, 2, 1, 4, 0.0F);
        button3 = new ModelPart(64, 64, 0, 19);
        button3.addCuboid(4, 14.0F, 6.0F, 2, 1, 4, 0.0F);

    }

    @Override
    public void render(StorageBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        int[] dyes = blockEntity.getChannel();
        float[] color1 = DyeColor.byId(dyes[0]).getColorComponents();
        float[] color2 = DyeColor.byId(dyes[1]).getColorComponents();
        float[] color3 = DyeColor.byId(dyes[2]).getColorComponents();

        matrices.push();

        switch (blockEntity.getCachedState().get(StorageBlock.FACING)) {
            case NORTH:
                break;
            case EAST:
                matrices.multiply(EAST_ROTATION_QUAT);
                matrices.translate(0, 0, -1);
                break;
            case SOUTH:
                matrices.multiply(SOUTH_ROTAION_QUAT);
                matrices.translate(-1, 0, -1);
                break;
            case WEST:
                matrices.multiply(WEST_ROTATION_QUAT);
                matrices.translate(-1, 0, 0);
                break;
            default:
                break;
        }

        button1.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, overlay, color1[0], color1[1], color1[2], 1);
        button2.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, overlay, color2[0], color2[1], color2[2], 1);
        button3.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, overlay, color3[0], color3[1], color3[2], 1);
        matrices.pop();
    }
}