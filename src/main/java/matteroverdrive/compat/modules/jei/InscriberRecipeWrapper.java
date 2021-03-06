package matteroverdrive.compat.modules.jei;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import matteroverdrive.Reference;
import matteroverdrive.data.recipes.InscriberRecipe;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.RenderUtils;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author shadowfacts
 */
public class InscriberRecipeWrapper implements IRecipeWrapper
{

	private static final ResourceLocation ARROW = new ResourceLocation(Reference.TEXTURE_ARROW_PROGRESS);

	@Getter
	private final List inputs;
	@Getter
	private final List outputs;
	private final int energy;
	private final int time;

	public InscriberRecipeWrapper(InscriberRecipe recipe)
	{
		inputs = ImmutableList.of(recipe.getPrimary(), recipe.getSecondary());
		outputs = ImmutableList.of(recipe.getOutput());
		energy = recipe.getEnergy();
		time = recipe.getTime();
	}

	@Override
	public List<FluidStack> getFluidInputs()
	{
		return ImmutableList.of();
	}

	@Override
	public List<FluidStack> getFluidOutputs()
	{
		return ImmutableList.of();
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
	{
	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
	{
		minecraft.getTextureManager().bindTexture(ARROW);
		int width = (int)(((float)(minecraft.theWorld.getTotalWorldTime() % time) / (float)time) * 24);
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		RenderUtils.drawPlaneWithUV(35, 15, 0, width, 16, 0.5, 0, width / 48f, 1);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();

		String line1 = String.format("-%,d%s", energy, MOEnergyHelper.ENERGY_UNIT);
		int line1W = minecraft.fontRendererObj.getStringWidth(line1);
		minecraft.fontRendererObj.drawStringWithShadow(line1, 44 + 69 / 2 - (line1W) / 2, 38, Reference.COLOR_HOLO_RED.getColor());
		String line2 = I18n.format("mo.jei.time", time / 20);
		int line2W = minecraft.fontRendererObj.getStringWidth(line2);
		minecraft.fontRendererObj.drawStringWithShadow(line2, 44 + 69 / 2 - (line2W) / 2, 53, Reference.COLOR_HOLO_RED.getColor());
	}

	@Nullable
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY)
	{
		return ImmutableList.of();
	}

	@Override
	public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton)
	{
		return false;
	}

}
