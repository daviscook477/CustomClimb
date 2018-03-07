package customclimb.modifiers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.daily.DailyMods;
import com.megacrit.cardcrawl.daily.mods.AbstractDailyMod;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;

import customclimb.CustomClimb;

public class Charity extends AbstractDailyMod {
	public static final String ID = "Charity";
	public static final String NAME = "Charity";
	public static final String DESC = "The Merchant's prices are decreased by 25%";
	public static final float MODIFIER = 0.75f;
	public static final String CHARITY = "charity.png";
	
	public Charity() {
		super("Charity", NAME, DESC, "vintage.png", true);
		this.img = new Texture(CustomClimb.makePath(CHARITY));
	}

	@SpirePatch(cls = "com.megacrit.cardcrawl.shop.ShopScreen", method = "init")
	public static class ShopInit {

		public static void Postfix(Object __obj_instance, ArrayList<AbstractCard> coloredCards,
				ArrayList<AbstractCard> colorlessCards) {
			ShopScreen screen = (ShopScreen) __obj_instance;
			if (((Boolean) DailyMods.mods.get("Charity")) != null
					&& ((Boolean) DailyMods.mods.get("Charity")).booleanValue()) {
				screen.applyDiscount(MODIFIER);
			}
		}

	}

	@SpirePatch(cls = "com.megacrit.cardcrawl.shop.ShopScreen", method = "getNewPrice", paramtypes={"com.megacrit.cardcrawl.shop.StoreRelic"})
	public static class GetNewRelicPrice {

		@SpireInsertPatch(rloc = 13, localvars = { "retVal" })
		public static void Insert(Object __obj_instance, StoreRelic r, @ByRef int[] retVal) {
			try {
				ShopScreen screen = (ShopScreen) __obj_instance;
				if (((Boolean) DailyMods.mods.get("Charity")) != null
						&& ((Boolean) DailyMods.mods.get("Charity")).booleanValue()) {
					Method applyDiscountToRelic = screen.getClass().getDeclaredMethod("applyDiscountToRelic");
					applyDiscountToRelic.setAccessible(true);
					retVal[0] = (int) applyDiscountToRelic.invoke(screen, retVal[0], MODIFIER);
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SpirePatch(cls = "com.megacrit.cardcrawl.shop.ShopScreen", method = "getNewPrice", paramtypes={"com.megacrit.cardcrawl.shop.StorePotion"})
	public static class GetNewPotionPrice {

		@SpireInsertPatch(rloc = 13, localvars = { "retVal" })
		public static void Insert(Object __obj_instance, StorePotion p, @ByRef int[] retVal) {
			try {
				ShopScreen screen = (ShopScreen) __obj_instance;
				if (((Boolean) DailyMods.mods.get("Charity")) != null
						&& ((Boolean) DailyMods.mods.get("Charity")).booleanValue()) {
					Method applyDiscountToRelic = screen.getClass().getDeclaredMethod("applyDiscountToRelic");
					applyDiscountToRelic.setAccessible(true);
					retVal[0] = (int) applyDiscountToRelic.invoke(screen, retVal[0], MODIFIER);
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}

}
