package com.northwesterndevelopment.gtdumbbibfix.mixins.late;

import com.northwesterndevelopment.gtdumbbibfix.TranslationHelper;
import gtPlusPlus.core.item.tool.misc.ItemGregtechPump;
import jds.bibliocraft.BiblioSortingHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tconstruct.library.tools.DynamicToolPart;
import tconstruct.library.tools.ToolCore;


@Mixin(BiblioSortingHelper.class)
public class BibFix2 {
    @Redirect(
        method="getSortedListByAlphabet(Ljava/util/ArrayList;)Ljava/util/ArrayList;",
        at=@At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/StatCollector;translateToLocal(Ljava/lang/String;)Ljava/lang/String;"
        )
    )
    private static String alphaListOnTranslate(String text) {
        return TranslationHelper.customTranslationLogic(text);
    }

    @Redirect(
        method = "buildUnsortedItemList(Ljava/util/ArrayList;)Ljava/util/ArrayList;",
        at=@At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getUnlocalizedName()Ljava/lang/String;"
        )
    )
    private static String buildUnsortedOnGetUnlocalized(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().getCompoundTag("display").hasKey("Name")) {
            return "~!@#CUSTOM@!!~" + stack.getTagCompound().getCompoundTag("display").getString("Name");
        }
        if (stack.getItem() instanceof ToolCore) {

            return "~!@#TINKERTOOL@!!~" + TranslationHelper.getToolFormatString(stack);
        }
        if (stack.getItem() instanceof DynamicToolPart)
        {
            return "~!@#TINKERTOOLPART@!!~" + TranslationHelper.getToolPartFormatString(stack);
        }
        String mName = stack.getUnlocalizedName();
        if (mName.equals("item.MU-metatool.01")) {
            try {
                return mName + ":" + ((ItemGregtechPump) stack.getItem()).getCorrectMetaForItemstack(stack);
            } catch (Exception e) {
                return mName;
            }
        }
        return mName;
    }
}
