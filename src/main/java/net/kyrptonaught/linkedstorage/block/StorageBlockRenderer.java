package net.kyrptonaught.linkedstorage.block;

import net.kyrptonaught.linkedstorage.LinkedStorageModClient;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.world.World;

import java.util.Random;

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

        World world = blockEntity.getWorld();
        BlockPos pos = blockEntity.getPos();
        BlockState state = world.getBlockState(pos);
        MinecraftClient client = MinecraftClient.getInstance();
        matrices.push();

        float f = state.get(StorageBlock.FACING).getOpposite().asRotation();
        matrices.translate(0.5D, 0.5D, 0.5D);
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-f));
        matrices.translate(-0.5D, -0.5D, -0.5D);

        BakedModel lid = MinecraftClient.getInstance().getBakedModelManager().getModel(LinkedStorageModClient.LINKED_CHEST_LID);
        //matrices.translate(0.0D, 1 + (blockEntity.getAnimationProgress(tickDelta) * 0.5F), 0.0D);
        /*float g = blockEntity.getAnimationProgress(tickDelta);
        g = 1.0F - g;
        g = 1.0F - g * g * g;
        matrices.translate(0.0D, g * 1.5, g*.3125);
        matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion((g * 90)));

         */
        client.getBlockRenderManager().getModelRenderer().render(world, lid, state, pos, matrices, vertexConsumers.getBuffer(RenderLayer.getSolid()), false, new Random(), state.getRenderingSeed(pos), overlay);
        button1.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, overlay, color1[0], color1[1], color1[2], 1);
        button2.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, overlay, color2[0], color2[1], color2[2], 1);
        button3.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, overlay, color3[0], color3[1], color3[2], 1);
        matrices.pop();
    }
}