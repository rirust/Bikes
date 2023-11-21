package com.ririthenerd.bike;

import com.ririthenerd.entities.BikeEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class BikeEntityModel extends EntityModel<BikeEntity> {
	private final ModelPart handlebars;
	private final ModelPart front_wheel;
	private final ModelPart front_fork;
	private final ModelPart frame;
	private final ModelPart rear_wheel;
	private final ModelPart seat;

	public BikeEntityModel(ModelPart root) {
		this.handlebars = root.getChild("handlebars");
		this.front_wheel = root.getChild("front_wheel");
		this.front_fork = root.getChild("front_fork");
		this.frame = root.getChild("frame");
		this.rear_wheel = root.getChild("rear_wheel");
		this.seat = root.getChild("seat");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData handlebars = modelPartData.addChild("handlebars", ModelPartBuilder.create().uv(13, 0).cuboid(-1.0F, -33.0F, 5.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
		.uv(48, 38).cuboid(-7.0F, -33.0F, 8.0F, 14.0F, 2.0F, 2.0F, new Dilation(0.0F)),  ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData cube_r1 = handlebars.addChild("cube_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -17.0F, -28.0F, 2.0F, 2.0F, 9.0F, new Dilation(0.0F)),  ModelTransform.of(0.0F, 0.0F, 3.0F, -1.0908F, 0.0F, 0.0F));

		ModelPartData front_wheel = modelPartData.addChild("front_wheel", ModelPartBuilder.create().uv(28, 0).cuboid(-1.0F, -10.0F, 9.0F, 2.0F, 10.0F, 2.0F, new Dilation(0.0F))
		.uv(27, 26).cuboid(-1.0F, -10.0F, -3.0F, 2.0F, 10.0F, 2.0F, new Dilation(0.0F)),  ModelTransform.pivot(0.0F, 22.0F, 13.0F));

		ModelPartData cube_r2 = front_wheel.addChild("cube_r2", ModelPartBuilder.create().uv(35, 26).cuboid(-1.0F, -11.0F, 10.0F, 2.0F, 10.0F, 2.0F, new Dilation(0.0F))
		.uv(36, 0).cuboid(-1.0F, -11.0F, -2.0F, 2.0F, 10.0F, 2.0F, new Dilation(0.0F)),  ModelTransform.of(0.0F, 0.0F, 10.0F, 1.5708F, 0.0F, 0.0F));

		ModelPartData front_fork = modelPartData.addChild("front_fork", ModelPartBuilder.create(),  ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData cube_r3 = front_fork.addChild("cube_r3", ModelPartBuilder.create().uv(27, 26).cuboid(-2.0F, -17.0F, -18.0F, 1.0F, 2.0F, 19.0F, new Dilation(0.0F))
		.uv(9, 51).cuboid(1.0F, -17.0F, -18.0F, 1.0F, 2.0F, 19.0F, new Dilation(0.0F))
		.uv(13, 5).cuboid(-2.0F, -17.0F, -19.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)),  ModelTransform.of(0.0F, 0.0F, 3.0F, -1.0908F, 0.0F, 0.0F));

		ModelPartData frame = modelPartData.addChild("frame", ModelPartBuilder.create(),  ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData cube_r4 = frame.addChild("cube_r4", ModelPartBuilder.create().uv(0, 26).cuboid(-1.0F, 12.0F, 3.0F, 2.0F, 13.0F, 2.0F, new Dilation(0.0F)),  ModelTransform.of(0.0F, 0.0F, 3.0F, -2.5307F, 0.0F, 0.0F));

		ModelPartData cube_r5 = frame.addChild("cube_r5", ModelPartBuilder.create().uv(35, 47).cuboid(-4.0F, 6.0F, 10.0F, 1.0F, 2.0F, 15.0F, new Dilation(0.0F))
		.uv(48, 21).cuboid(-1.0F, 6.0F, 10.0F, 1.0F, 2.0F, 15.0F, new Dilation(0.0F)),  ModelTransform.of(2.0F, 0.0F, 3.0F, 3.098F, 0.0F, 0.0F));

		ModelPartData cube_r6 = frame.addChild("cube_r6", ModelPartBuilder.create().uv(0, 51).cuboid(3.0F, 23.0F, -3.0F, 1.0F, 2.0F, 13.0F, new Dilation(0.0F))
		.uv(52, 47).cuboid(0.0F, 23.0F, -3.0F, 1.0F, 2.0F, 13.0F, new Dilation(0.0F)),  ModelTransform.of(-2.0F, 0.0F, 3.0F, -2.2689F, 0.0F, 0.0F));

		ModelPartData cube_r7 = frame.addChild("cube_r7", ModelPartBuilder.create().uv(0, 26).cuboid(-1.0F, -14.0F, -1.0F, 2.0F, 2.0F, 23.0F, new Dilation(0.0F)),  ModelTransform.of(0.0F, 0.0F, 3.0F, 0.8727F, 0.0F, 0.0F));

		ModelPartData cube_r8 = frame.addChild("cube_r8", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -25.0F, -7.0F, 2.0F, 2.0F, 24.0F, new Dilation(0.0F)),  ModelTransform.of(0.0F, 0.0F, 3.0F, 0.48F, 0.0F, 0.0F));

		ModelPartData rear_wheel = modelPartData.addChild("rear_wheel", ModelPartBuilder.create().uv(8, 11).cuboid(-1.0F, -10.0F, 9.0F, 2.0F, 10.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 11).cuboid(-1.0F, -10.0F, -3.0F, 2.0F, 10.0F, 2.0F, new Dilation(0.0F)),  ModelTransform.pivot(0.0F, 22.0F, -26.0F));

		ModelPartData cube_r9 = rear_wheel.addChild("cube_r9", ModelPartBuilder.create().uv(16, 11).cuboid(-1.0F, -11.0F, 10.0F, 2.0F, 10.0F, 2.0F, new Dilation(0.0F))
		.uv(8, 26).cuboid(-1.0F, -11.0F, -2.0F, 2.0F, 10.0F, 2.0F, new Dilation(0.0F)),  ModelTransform.of(0.0F, 0.0F, 10.0F, 1.5708F, 0.0F, 0.0F));

		ModelPartData seat = modelPartData.addChild("seat", ModelPartBuilder.create().uv(57, 62).cuboid(-2.0F, -29.0F, -26.0F, 4.0F, 2.0F, 10.0F, new Dilation(0.0F)),  ModelTransform.pivot(0.0F, 24.0F, 3.0F));

		ModelPartData cube_r10 = seat.addChild("cube_r10", ModelPartBuilder.create().uv(0, 26).cuboid(-1.0F, 23.0F, 5.0F, 2.0F, 13.0F, 2.0F, new Dilation(0.0F)),  ModelTransform.of(0.0F, 0.0F, 0.0F, -2.618F, 0.0F, 0.0F));

		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void setAngles(BikeEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}

	@Override
	public void render(MatrixStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		handlebars.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		front_wheel.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		front_fork.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		frame.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rear_wheel.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		seat.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}