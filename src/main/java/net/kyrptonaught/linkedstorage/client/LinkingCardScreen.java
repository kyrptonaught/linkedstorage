package net.kyrptonaught.linkedstorage.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.kyrptonaught.linkedstorage.LinkingCardRenamePacket;
import net.kyrptonaught.linkedstorage.inventory.LinkedInventoryHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class LinkingCardScreen extends Screen {
    private TextFieldWidget nameField;
    ItemStack item;

    public LinkingCardScreen(Text text_1, ItemStack stack) {
        super(text_1);
        this.item = stack;
    }

    protected void init() {
        super.init();
        this.minecraft.keyboard.enableRepeatEvents(true);
        this.nameField = new TextFieldWidget(this.font, this.width / 2, this.height / 2, 103, 12, I18n.translate("container.repair"));
        this.nameField.setText(LinkedInventoryHelper.getItemChannel(item));
        this.nameField.method_1856(false);
        this.nameField.changeFocus(true);
        this.nameField.setEditableColor(-1);
        this.nameField.setUneditableColor(-1);
        this.nameField.setHasBorder(false);
        this.nameField.setMaxLength(35);
        this.nameField.setChangedListener(this::onRenamed);
        this.children.add(this.nameField);
        this.setInitialFocus(this.nameField);
    }

    private void onRenamed(String string_1) {
        LinkingCardRenamePacket.sendPacket(string_1);
    }

    public boolean keyPressed(int int_1, int int_2, int int_3) {
        if (int_1 == 256) {
            this.minecraft.player.closeContainer();
            return true;
        }
        return this.nameField.keyPressed(int_1, int_2, int_3) || this.nameField.method_20315() || super.keyPressed(int_1, int_2, int_3);
    }

    public void render(int int_1, int int_2, float float_1) {
        this.renderBackground();
        super.render(int_1, int_2, float_1);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.nameField.render(int_1, int_2, float_1);
    }
}