package com.northwesterndevelopment.gtdumbbibfix;

import bartworks.system.material.Werkstoff;
import com.glodblock.github.common.storage.CellType;
import gregtech.api.enums.Materials;
import kubatech.loaders.item.items.ItemTeaUltimate;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.CustomMaterial;
import tconstruct.library.tools.DynamicToolPart;
import tconstruct.library.tools.ToolCore;
import tconstruct.library.tools.ToolMaterial;

import java.util.Locale;

public class TranslationHelper {
    public static String customTranslationLogic(String text) {
        /*
         * Handles items that have been renamed in an anvil, tool forged, or tool station
         * This prevents items with custom names from mistakenly being translated through StatCollector
         */
        if (text.startsWith("~!@#CUSTOM@!!~")) {
            return EnumChatFormatting.ITALIC + text.substring(14, text.length() - 5).replace("~!@#CUSTOM@!!~", "") + EnumChatFormatting.RESET;
        }
        //Handles completed Tinkers Tools, since those have a complicated custom method for getting display name
        if (text.startsWith("~!@#TINKERTOOL@!!~")) {
            String[] parts = text.substring(18, text.length() - 5).split(":");
            if (parts.length == 1) {
                return StatCollector.translateToLocal(parts[0]);
            } else {
                return String.format(StatCollector.translateToLocal("tool.nameformat"), StatCollector.translateToLocal(parts[0]), StatCollector.translateToLocal(parts[1]));
            }
        }
        // Handles Tinkers Tool Parts, since those also have a complicated custom method for getting display name
        if (text.startsWith("~!@#TINKERTOOLPART@!!~")) {
            String[] parts = text.substring(22, text.length() - 5).split(":");
            if (parts.length == 1) {
                return StatCollector.translateToLocal(parts[0]);
            } else {
                return StatCollector.translateToLocal(parts[0]).replaceAll("%%material", parts[1].startsWith("!!CUSTOMNAME@$@$") ? parts[1] : StatCollector.translateToLocal(parts[1]));
            }
        }
        String out = StatCollector.translateToLocal(text);
        // Many items in GT have a different system for getting the item's localized name
        if (text.startsWith("gt.")) {
            int dam = getDamage(text);
            // Handles materials like ingots and bars
            if (out.contains("%")) {
                if (dam < 32000 && dam >= 0) out = Materials.getLocalizedNameForItem(out, dam % 1000);
            // Handles most of the BartWorks stuff
            } else if (text.contains(".bwMetaGenerated")) {
                String[] sections = text.split("\\.");
                String temp = StatCollector.translateToLocal(String.format("bw.itemtype.%s", sections[1].replace("bwMetaGenerated", "")));
                if (temp.contains("%")) {
                    if (dam < 32000 && dam >= 0) {
                        Werkstoff werkstoff = Werkstoff.werkstoffHashMap.get((short) dam);
                        if (werkstoff == null) werkstoff = Werkstoff.default_null_Werkstoff;
                        out = temp.replace("%material", werkstoff.getLocalizedName());
                    }
                }
            }
        // Handles BartWorks ores
        } else if (text.startsWith("bw.")) {
            int dam = getDamage(text);
            if (text.contains(".blockores.01")) {
                out = StatCollector.translateToLocal("bw.blocktype.ore");
                if (dam < 32000 && dam >= 0) {
                    Werkstoff werkstoff = Werkstoff.werkstoffHashMap.get((short) dam);
                    if (werkstoff == null) werkstoff = Werkstoff.default_null_Werkstoff;
                    out = out.replace("%material", werkstoff.getLocalizedName());
                }
            } else if (text.contains(".blockores.02")) {
                out = StatCollector.translateToLocal("bw.blocktype.oreSmall");
                if (dam < 32000 && dam >= 0) {
                    Werkstoff werkstoff = Werkstoff.werkstoffHashMap.get((short) dam);
                    if (werkstoff == null) werkstoff = Werkstoff.default_null_Werkstoff;
                    out = out.replace("%material", werkstoff.getLocalizedName());
                }
            }
        // Handles IC2
        } else if (text.startsWith("ic2.")) {
            out = StatCollector.translateToLocal(text.substring(0, text.length() - 5));
        // Handles KubaTech
        } else if (text.equals("kubaitem.teacollection.ultimate_tea.name")) {
            out = ItemTeaUltimate.getUltimateTeaDisplayName(out);
        // Handles AE2 Fluid Crafting's custom cell name coloring
        } else if (text.contains("fluid_storage")) {
            int dam = getDamage(text);
            switch(dam) {
                case 1:
                    out = StatCollector.translateToLocalFormatted(text, CellType.getTypeColor(CellType.Cell1kPart), EnumChatFormatting.RESET);
                    break;
                case 4:
                    out = StatCollector.translateToLocalFormatted(text, CellType.getTypeColor(CellType.Cell4kPart), EnumChatFormatting.RESET);
                    break;
                case 16:
                    out = StatCollector.translateToLocalFormatted(text, CellType.getTypeColor(CellType.Cell16kPart), EnumChatFormatting.RESET);
                    break;
                case 64:
                    out = StatCollector.translateToLocalFormatted(text, CellType.getTypeColor(CellType.Cell64kPart), EnumChatFormatting.RESET);
                    break;
                case 256:
                    out = StatCollector.translateToLocalFormatted(text, CellType.getTypeColor(CellType.Cell256kPart), EnumChatFormatting.RESET);
                    break;
                case 1024:
                    out = StatCollector.translateToLocalFormatted(text, CellType.getTypeColor(CellType.Cell1024kPart), EnumChatFormatting.RESET);
                    break;
                case 4096:
                    out = StatCollector.translateToLocalFormatted(text, CellType.getTypeColor(CellType.Cell4096kPart), EnumChatFormatting.RESET);
                    break;
                case 16384:
                    out = StatCollector.translateToLocalFormatted(text, CellType.getTypeColor(CellType.Cell16384kPart), EnumChatFormatting.RESET);
                    break;
                default:
                    break;
            }
        // Handles the GT++ Hand Pump things
        } else if (text.startsWith("item.MU-metatool.01")) {
            out = StatCollector.translateToLocal("gtplusplus." + text.replace(":", "."));
        }

        return out;
    }

    public static String getToolFormatString(ItemStack tool) {
        if (!tool.hasTagCompound() || !tool.getTagCompound().hasKey("InfiTool")) return null;
        if (!(tool.getItem() instanceof ToolCore core)) return null;

        int mat = tool.getTagCompound().getCompoundTag("InfiTool").getInteger("Head");
        ToolMaterial material = TConstructRegistry.getMaterial(mat);

        String toolName = core.getToolName();
        String matName = material.materialName.replaceAll(" ", "").replaceAll("_", "");

        if (StatCollector.canTranslate("tool." + toolName + "." + matName)) {
            return "tool." + toolName + "." + matName;
        }

        return material.localizationString + ":" + "tool." + core.getToolName().toLowerCase();
    }

    public static String getToolPartFormatString(ItemStack tool) {
        if (!(tool.getItem() instanceof DynamicToolPart part)) return null;

        String material;
        String matName;

        if (part.customMaterialClass == null) {
            ToolMaterial mat = TConstructRegistry.getMaterial(part.getMaterialID(tool));
            if (mat == null) return tool.getUnlocalizedName();

            material = mat.localizationString.toLowerCase(Locale.ENGLISH).startsWith("material.")
                ? mat.localizationString.substring(9)
                : mat.localizationString;
            matName = StatCollector.canTranslate(String.format("%s.display", mat.localizationString)) ? String.format("%s.display", mat.localizationString) : mat.localizationString;
        } else {
            CustomMaterial customMaterial = TConstructRegistry
                .getCustomMaterial(part.getMaterialID(tool), part.customMaterialClass);

            if (customMaterial.input != null) {
                material = customMaterial.input.getUnlocalizedName();
                String s = customMaterial.input.getUnlocalizedName();

                if (customMaterial.input.stackTagCompound != null && customMaterial.input.stackTagCompound.hasKey("display", 10))
                {
                    NBTTagCompound nbttagcompound = customMaterial.input.stackTagCompound.getCompoundTag("display");

                    if (nbttagcompound.hasKey("Name", 8))
                    {
                        s = "!!CUSTOMNAME@$@$" + nbttagcompound.getString("Name");
                    }
                }

                matName = s;
            }
            else {
                material = customMaterial.oredict;
                matName = customMaterial.oredict;
            }
        }

        if (StatCollector.canTranslate("toolpart." + part.partName + "." + material)) {
            return "toolpart." + part.partName + "." + material;
        }
        else {
            if (StatCollector.canTranslate("toolpart.material." + material))
                matName = "toolpart.material." + material;
            return "toolpart." + part.partName + ":" + matName;
        }
    }

    private static int getDamage(String text) {
        String[] bits = text.split("\\.");
        try {
            return Integer.parseInt(bits[bits.length - 2]);
        } catch(NumberFormatException e) {
            return -1;
        }
    }
}
