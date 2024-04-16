package bigchadguys.dailyshop.screen;

import bigchadguys.dailyshop.DailyShopMod;
import bigchadguys.dailyshop.init.ModNetwork;
import bigchadguys.dailyshop.net.DailyShopTradeC2SPacket;
import bigchadguys.dailyshop.screen.handler.DailyShopScreenHandler;
import bigchadguys.dailyshop.trade.Trade;
import bigchadguys.dailyshop.util.ClientScheduler;
import bigchadguys.dailyshop.util.TradeExecutor;
import bigchadguys.dailyshop.world.data.DailyShopData;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.List;

@Environment(value = EnvType.CLIENT)
public class DailyShopScreen extends HandledScreen<DailyShopScreenHandler> {

    private static final Identifier TEXTURE = DailyShopMod.id("textures/gui/daily_shop.png");
    private static final Text TRADES_TEXT = Text.translatable("merchant.trades");

    private final OfferWidget[] offers;
    private int scrollOffset;
    private boolean scrolling;

    private int hoveredTrade;

    public DailyShopScreen(DailyShopScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title);
        this.backgroundWidth = 276;
        this.playerInventoryTitleX = 107;

        this.offers = new OfferWidget[7];
        this.scrollOffset = 0;
        this.hoveredTrade = -1;
    }

    public List<Trade> getTrades() {
        return DailyShopData.CLIENT.getShop().getTrades().toList();
    }

    public TradeExecutor.Result getResult(int index) {
        List<Trade> trades = this.getTrades();

        if(index >= 0 && index < trades.size()) {
            return TradeExecutor.test(trades.get(index), this.handler);
        }

        return null;
    }

    @Override
    protected void init() {
        super.init();
        int centerX = (this.width - this.backgroundWidth) / 2 - 1;
        int centerY = (this.height - this.backgroundHeight) / 2 - 1;
        int x = centerX + 5;
        int y = centerY + 18;

        for(int i = 0; i < 7; i++) {
            this.offers[i] = this.addDrawableChild(new OfferWidget(x, y, i, button -> {
                if(!(button instanceof OfferWidget offer)) return;
                ModNetwork.CHANNEL.sendToServer(new DailyShopTradeC2SPacket(this.scrollOffset + offer.getIndex()));
            }));

            y += 20;
        }
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title, 49 + this.backgroundWidth / 2 - this.textRenderer.getWidth(this.title) / 2, 6, 0x404040, false);
        context.drawText(this.textRenderer, this.playerInventoryTitle, this.playerInventoryTitleX, this.playerInventoryTitleY, 0x404040, false);
        int l = this.textRenderer.getWidth(TRADES_TEXT);
        context.drawText(this.textRenderer, TRADES_TEXT, 5 - l / 2 + 48, 6, 0x404040, false);
    }


    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int centerX = (this.width - this.backgroundWidth) / 2 - 1;
        int centerY = (this.height - this.backgroundHeight) / 2 - 1;
        context.drawTexture(TEXTURE, centerX, centerY, 0, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 512, 256);
    }

    private void renderScrollbar(DrawContext context, int x, int y, List<Trade> trades) {
        int i = trades.size() + 1 - 7;

        if(i > 1) {
            int j = 139 - (27 + (i - 1) * 139 / i);
            int k = 1 + j / i + 139 / i;
            int l = 113;
            int m = Math.min(113, this.scrollOffset * k);
            if (this.scrollOffset == i - 1) {
                m = 113;
            }
            context.drawTexture(TEXTURE, x + 94, y + 18 + m, 0, 0.0f, 199.0f, 6, 27, 512, 256);
        } else {
            context.drawTexture(TEXTURE, x + 94, y + 18, 0, 6.0f, 199.0f, 6, 27, 512, 256);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.renderInventoryHighlight(context);

        double time = ClientScheduler.TICK + delta;
        List<Trade> trades = this.getTrades();

        if(!trades.isEmpty()) {
            int i = (this.width - this.backgroundWidth) / 2 - 1;
            int j = (this.height - this.backgroundHeight) / 2 - 1;
            int k = j + 16 + 1;
            int l = i + 5 + 5;
            this.renderScrollbar(context, i, j, trades);
            int m = 0;

            for (Trade trade : trades) {
                if(this.canScroll(trades.size()) && (m < this.scrollOffset || m >= 7 + this.scrollOffset)) {
                    ++m;
                    continue;
                }

                ItemStack input1 = trade.getInput(1).getDisplay(time);
                ItemStack input2 = trade.getInput(2).getDisplay(time);
                ItemStack input3 = trade.getInput(3).getDisplay(time);
                ItemStack output = trade.getOutput();
                TradeExecutor.Result result = TradeExecutor.test(trade, this.handler);

                context.getMatrices().push();
                context.getMatrices().translate(0.0f, 0.0f, 100.0f);
                int n = k + 2;

                if(!input1.isEmpty()) {
                    context.drawItemWithoutEntity(input1, l - 2, n);

                    if(!result.checkInput(1)) {
                        RenderSystem.setShaderColor(1.0F, 0.332F, 0.332F, 1.0F);
                    }

                    context.drawItemInSlot(this.textRenderer, input1, l - 2, n);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                }

                if(!input2.isEmpty()) {
                    context.drawItemWithoutEntity(input2, i + 25, n);

                    if(!result.checkInput(2)) {
                        RenderSystem.setShaderColor(1.0F, 0.332F, 0.332F, 1.0F);
                    }

                    context.drawItemInSlot(this.textRenderer, input2, i + 25, n);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                }

                if(!input3.isEmpty()) {
                    context.drawItemWithoutEntity(input3, i + 42, n);

                    if(!result.checkInput(3)) {
                        RenderSystem.setShaderColor(1.0F, 0.332F, 0.332F, 1.0F);
                    }

                    context.drawItemInSlot(this.textRenderer, input3, i + 42, n);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                }

                RenderSystem.enableBlend();

                if(!trade.isAvailable()) {
                    context.drawTexture(TEXTURE, i + 2 + 35 + 25, n + 4, 0, 25.0f, 171.0f, 10, 9, 512, 256);
                } else {
                    context.drawTexture(TEXTURE, i + 2 + 35 + 25, n + 4, 0, 15.0f, 171.0f, 10, 9, 512, 256);
                }

                if(!output.isEmpty()) {
                    context.drawItemWithoutEntity(output, i + 3 + 70, n);

                    if(!result.checkOutput()) {
                        RenderSystem.setShaderColor(1.0F, 0.332F, 0.332F, 1.0F);
                    }

                    context.drawItemInSlot(this.textRenderer, output, i + 3 + 70, n);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                }

                context.getMatrices().pop();
                k += 20;
                ++m;
            }

            for(OfferWidget offer : this.offers) {
                if(offer.isSelected()) {
                    offer.renderTooltip(context, mouseX, mouseY, time);
                }

                offer.visible = offer.index < this.getTrades().size();
            }

            RenderSystem.enableDepthTest();
        }

        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    private void renderInventoryHighlight(DrawContext context) {
        this.hoveredTrade = -1;

        for(OfferWidget offer : this.offers) {
            if(!offer.isHovered()) continue;
            this.hoveredTrade = offer.index + DailyShopScreen.this.scrollOffset;
        }

        TradeExecutor.Result result = this.getResult(this.hoveredTrade);
        if(result == null) return;

        RenderSystem.disableDepthTest();
        context.getMatrices().push();
        context.getMatrices().translate(this.x, this.y, 0.0F);

        for(Integer index : result.getUsedSlots()) {
            Slot slot = this.handler.getSlot(index);
            context.fillGradient(RenderLayer.getGuiOverlay(), slot.x, slot.y,
                    slot.x + 16, slot.y + 16, 0x6555FF55, 0x6555FF55, 0);
        }

        for(Integer index : result.getIncompleteSlots()) {
            Slot slot = this.handler.getSlot(index);
            context.fillGradient(RenderLayer.getGuiOverlay(), slot.x, slot.y,
                    slot.x + 16, slot.y + 16, 0x65FF5555, 0x65FF5555, 0);
        }

        context.getMatrices().pop();
        RenderSystem.enableDepthTest();
    }

    private boolean canScroll(int listSize) {
        return listSize > 7;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int i = this.getTrades().size();

        if(this.canScroll(i)) {
            int j = i - 7;
            this.scrollOffset = MathHelper.clamp((int)((double)this.scrollOffset - amount), 0, j);
        }

        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(this.scrolling) {
            int j = this.y + 18;
            int k = j + 139;
            int l = this.getTrades().size() - 7;
            float f = ((float)mouseY - (float)j - 13.5f) / ((float)(k - j) - 27.0f);
            f = f * (float)l + 0.5f;
            this.scrollOffset = MathHelper.clamp((int)f, 0, l);
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.scrolling = false;
        int i = (this.width - this.backgroundWidth) / 2 - 1;
        int j = (this.height - this.backgroundHeight) / 2 - 1;

        if(this.canScroll(this.getTrades().size()) && mouseX > (double)(i + 94) && mouseX < (double)(i + 94 + 6) && mouseY > (double)(j + 18) && mouseY <= (double)(j + 18 + 139 + 1)) {
            this.scrolling = true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public class OfferWidget extends ButtonWidget {
        final int index;

        public OfferWidget(int x, int y, int index, ButtonWidget.PressAction onPress) {
            super(x, y, 88, 20, ScreenTexts.EMPTY, onPress, DEFAULT_NARRATION_SUPPLIER);
            this.index = index;
            this.visible = false;
        }

        public int getIndex() {
            return this.index;
        }

        public void renderTooltip(DrawContext context, int x, int y, double time) {
            List<Trade> trades = DailyShopScreen.this.getTrades();
            int index = this.index + DailyShopScreen.this.scrollOffset;

            if(this.hovered && trades.size() > index) {
                ItemStack stack = ItemStack.EMPTY;

                if(x >= this.getX() + 4 && x < this.getX() + 20) {
                    stack = trades.get(index).getInput(1).getDisplay(time);
                } else if(x >= this.getX() + 20 && x < this.getX() + 36) {
                    stack = trades.get(index).getInput(2).getDisplay(time);
                } else if(x >= this.getX() + 36 && x < this.getX() + 52) {
                    stack = trades.get(index).getInput(3).getDisplay(time);
                } else if(x > this.getX() + 65) {
                    stack = trades.get(index).getOutput();
                }

                if(!stack.isEmpty()) {
                    context.drawItemTooltip(DailyShopScreen.this.textRenderer, stack, x, y);
                }
            }
        }
    }

}
