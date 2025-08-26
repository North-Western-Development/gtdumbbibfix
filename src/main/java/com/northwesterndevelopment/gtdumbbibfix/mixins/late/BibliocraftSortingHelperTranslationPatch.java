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
public class BibliocraftSortingHelperTranslationPatch {
    /**
     * Run custom translation logic when sorting the items list
     *
     * @author North Western Development
     * @reason see above
     * @since 2025-08-25
     */
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

    /**
     * Inject certain additional information about an item on the server side that isn't available once the client
     * receives it. We don't just call getDisplayName() on the stack even though that is where most of this
     * logic comes from as we are on the server currently so translating here will use the server's locale setting
     * instead of the client's. The alternative to this system would require sending a full ItemStack class for each
     * item type to the client to call getDisplayName() on. This would require a much larger and more complicated
     * patch and would also increase the amount of data that has to be sent. The system implemented here is definitely
     * not perfect as any item or mod that uses a custom getDisplayName system will need its own handling logic
     * implemented in this patch. I believe most mods and items in GTNH should be supported in this patch's current
     * state.
     *
     * @author North Western Development
     * @reason see above
     * @since 2025-08-25
     */
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
        if (mName.equals("item.MU-metatool.01") && stack.getItem() instanceof ItemGregtechPump) {
            try {
                return mName + ":" + ((ItemGregtechPump) stack.getItem()).getCorrectMetaForItemstack(stack);
            } catch (Exception e) {
                return mName;
            }
        }
        return mName;
    }
}
