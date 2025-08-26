package com.northwesterndevelopment.gtdumbbibfix.mixins.late;

import com.northwesterndevelopment.gtdumbbibfix.TranslationHelper;
import jds.bibliocraft.gui.GuiStockCatalog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiStockCatalog.class)
public class BibFix {
    /**
     * Fixes the fix for the fix in BibFix to remove the .name from the end of items in GUI
     * Uses obfuscated name for drawScreen method for Minecraft Reasons
     *
     * @author North Western Development
     * @reason see above
     * @version 1.0
     * @since 2025-08-25
     */
    @Redirect(
        method="func_73863_a(IIF)V",
        at=@At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/StatCollector;translateToLocal(Ljava/lang/String;)Ljava/lang/String;",
            ordinal = 6
        )
    )
    public String onDrawScreenTranslate1(String text) {
        return TranslationHelper.customTranslationLogic(text);
    }

    /**
     * Fixes the fix for the fix in BibFix to remove the .name from the end of items in GUI compass screen
     * Uses obfuscated name for drawScreen method for Minecraft Reasons
     *
     * @author North Western Development
     * @reason see above
     * @version 1.0
     * @since 2025-08-25
     */
    @Redirect(
        method="func_73863_a(IIF)V",
        at=@At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/StatCollector;translateToLocal(Ljava/lang/String;)Ljava/lang/String;",
            ordinal = 9
        )
    )
    public String onDrawScreenTranslate2(String text) {
        return TranslationHelper.customTranslationLogic(text);
    }

    /**
     * Fixes the fix for the fix in BibFix to remove the .name from the end of items in compass chat messages
     * Uses obfuscated name for drawScreen method for Minecraft Reasons
     *
     * @author North Western Development
     * @reason see above
     * @version 1.0
     * @since 2025-08-25
     */
    @Redirect(
        method = "sendCompassUpdatePacket(I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/StatCollector;translateToLocal(Ljava/lang/String;)Ljava/lang/String;",
            ordinal = 0
        )
    )
    private static String onCompassTranslate(String text) {
        return TranslationHelper.customTranslationLogic(text);
    }
}
