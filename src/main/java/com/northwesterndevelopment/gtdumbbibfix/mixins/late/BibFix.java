package com.northwesterndevelopment.gtdumbbibfix.mixins.late;

import jds.bibliocraft.BiblioSortingHelper;
import jds.bibliocraft.helpers.InventoryListItem;
import jds.bibliocraft.helpers.InventorySet;
import jds.bibliocraft.helpers.SortedListItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;

@Mixin(BiblioSortingHelper.class)
public class BibFix {
    /**
     * Fixes a bug in BiblioCraft's handling of translations, this is actually a bug in GregTech's handling of
     * translation entries for items it generates but we're not assigning blame here, causing some things to show up as
     * %material ingot, %material bar, %material block, or just it's translation key.
     *
     * @author North Western Development
     * @reason see above
     * @version 1.0
     * @since 2025-08-25
     */
    @Overwrite(remap = false)
    public static ArrayList<SortedListItem> buildUnsortedItemList(ArrayList<InventorySet> inventoryset) {
        ArrayList<SortedListItem> itemList = new ArrayList();

        for(int i = 0; i < inventoryset.size(); ++i) {
            InventorySet invSet = (InventorySet)inventoryset.get(i);
            if (invSet != null) {
                ArrayList<ItemStack> inv = invSet.inventoryList;

                for(int j = 0; j < inv.size(); ++j) {
                    ItemStack stack = (ItemStack)inv.get(j);
                    if (stack != null) {
                        InventoryListItem newInvListItem = new InventoryListItem(stack.getDisplayName(), stack.stackSize, invSet.inventoryName, invSet.tileX, invSet.tileY, invSet.tileZ);
                        boolean listHasItem = false;

                        for(int k = 0; k < itemList.size(); ++k) {
                            SortedListItem item = (SortedListItem)itemList.get(k);
                            if (item.itemName.contentEquals(stack.getDisplayName())) {
                                ArrayList<InventoryListItem> listOfInventories = item.inventoryList;
                                boolean alreadyHasCurrentInventory = false;

                                for(int n = 0; n < listOfInventories.size(); ++n) {
                                    InventoryListItem invListItem = (InventoryListItem)listOfInventories.get(n);
                                    if (invListItem.inventoryName.contentEquals(invSet.inventoryName) && invListItem.tileX == invSet.tileX && invListItem.tileY == invSet.tileY && invListItem.tileZ == invSet.tileZ) {
                                        invListItem.itemQuantity += stack.stackSize;
                                        alreadyHasCurrentInventory = true;
                                        listOfInventories.set(n, invListItem);
                                        break;
                                    }
                                }

                                if (!alreadyHasCurrentInventory) {
                                    listOfInventories.add(newInvListItem);
                                }

                                listOfInventories = BiblioSortingHelper.sortListOfInventories(listOfInventories);
                                item.inventoryList = listOfInventories;
                                item.itemQuantity += stack.stackSize;
                                listHasItem = true;
                                break;
                            }
                        }

                        if (!listHasItem) {
                            ArrayList<InventoryListItem> listOfInventories = new ArrayList();
                            listOfInventories.add(newInvListItem);
                            itemList.add(new SortedListItem(stack.getDisplayName(), stack.stackSize, listOfInventories));
                        }
                    }
                }
            }
        }

        return itemList;
    }
}
