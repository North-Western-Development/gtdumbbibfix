package com.northwesterndevelopment.gtdumbbibfix.mixins.late;

import jds.bibliocraft.gui.GuiStockCatalog;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiStockCatalog.class)
public class BibFix2 {
    /**
     * Fixes the fix for the fix in BibFix to remove the .name from the end of items in GUI
     * Uses obfuscated name for drawScreen method for Minecraft Reasons
     *
     * @author North Western Development
     * @reason see above
     * @version 1.0
     * @since 2025-08-25
     */
    @Redirect(method="func_73863_a(IIF)V", at=@At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/gui/FontRenderer;func_78276_b(Ljava/lang/String;III)I",
        ordinal = 6
    ), remap = false)
    public int onDrawString(FontRenderer instance, String text, int x, int y, int color) {
        return instance.drawString(text.substring(0, text.length() - 5), x, y, color);
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
    @Redirect(method="func_73863_a(IIF)V", at=@At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/gui/FontRenderer;func_78276_b(Ljava/lang/String;III)I",
        ordinal = 8
    ), remap = false)
    public int onDrawString2(FontRenderer instance, String text, int x, int y, int color) {
        return instance.drawString(text.substring(0, text.length() - 5), x, y, color);
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
    private static String onTranslate(String text) {
        return text.substring(0, text.length() - 5);
    }
}
