package bigchadguys.dailyshop.block.entity.renderer;

import bigchadguys.dailyshop.block.entity.DailyShopBlockEntity;
import bigchadguys.dailyshop.model.ExclamationPointModel;
import bigchadguys.dailyshop.util.ClientScheduler;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class DailyShopBlockEntityRenderer implements BlockEntityRenderer<DailyShopBlockEntity> {

    private final ExclamationPointModel exclamationPoint;

    public DailyShopBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.exclamationPoint = new ExclamationPointModel();
    }

    @Override
    public void render(DailyShopBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if(entity.isRefreshed()) {
            matrices.push();
            matrices.translate(0.5F, 0.5F + Math.sin(ClientScheduler.getTick(tickDelta) / 10.0F) / 20.0F, 0.5F);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)ClientScheduler.getTick(tickDelta)));

            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(ExclamationPointModel.TEXTURE));
            this.exclamationPoint.render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            matrices.pop();
        }
    }

}
