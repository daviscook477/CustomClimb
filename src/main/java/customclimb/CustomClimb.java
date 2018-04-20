package customclimb;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.daily.DailyMods;
import com.megacrit.cardcrawl.daily.mods.AbstractDailyMod;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.InputHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.ui.buttons.ConfirmButton;

import basemod.BaseMod;
import basemod.ModButton;
import basemod.ModLabel;
import basemod.ModPanel;
import basemod.ModTextPanel;
import basemod.interfaces.PostInitializeSubscriber;

@SpireInitializer
public class CustomClimb implements PostInitializeSubscriber {
	public static final Logger logger = LogManager.getLogger(CustomClimb.class.getName());

	private static final String MODNAME = "CustomClimb";
	private static final String AUTHOR = "test447";
	private static final String DESCRIPTION = "Adds a new Custom Climb mode like the Daily Mode but where you get to choose your modifiers.";

	private static final String ASSETS_FOLDER = "img";

	private static final String BADGE_IMG = "badge.png";
	private static final String PLUS_IMG = "plus.png";
	private static final String MINUS_IMG = "minus.png";
	private static final String SEED_IMG = "seed.png";
	private static final String CHAOTIC_RANDOM_IMG = "chaotic_random.png";
	private static final String BALANCED_RANDOM_IMG = "balanced_random.png";
	
	/**
	 * Make path to resource
	 * 
	 * @param resource
	 *            the name of the resource file
	 * @return full path
	 */
	public static final String makePath(String resource) {
		return ASSETS_FOLDER + "/" + resource;
	}

	public CustomClimb() {
		logger.info("subscribing to postInitialize event");
		BaseMod.subscribeToPostInitialize(this);
	}

	public static void initialize() {
		logger.info("========================= CUSTOMCLIMB INIT =========================");

		@SuppressWarnings("unused")
		CustomClimb climb = new CustomClimb();

		logger.info("================================================================");
	}

	@Override
	public void receivePostInitialize() {
		buildUI();

		Settings.isDailyRun = false;
		Settings.isTrial = false;
		Settings.isDemo = false;
	}

	private static Texture makeTexture(String path, float scale) {
		// https://stackoverflow.com/questions/16886228/java-libgdx-how-do-i-resize-my-textures-in-libgdx
		Pixmap original = new Pixmap(Gdx.files.internal(path));
		int width = original.getWidth();
		int height = original.getHeight();
		Pixmap scaled = new Pixmap((int) (width * scale), (int) (height * scale), original.getFormat());
		scaled.drawPixmap(original, 0, 0, original.getWidth(), original.getHeight(),
				0, 0, scaled.getWidth(), scaled.getHeight());
		Texture tex = new Texture(scaled);
		original.dispose();
		scaled.dispose();
		return tex;
	}
	
    private static final float HB_SHRINK = 14.0f;
	public static final float HEADER_X = 380.0f, HEADER_Y = 730.0f;
	public static final float CHAR_SELECT_X_START = 380.0f;
	public static final float CHAR_SELECT_Y = 620.0f;
	public static final float CHAR_SELECT_X_DELTA = 100.0f;
	public static final float SEED_X = 1450.0f;
	public static final float SEED_Y = 680.0f;
	public static final float CHAOTIC_RANDOM_X = 1350.0f;
	public static final float CHAOTIC_RANDOM_Y = 680.0f;
	public static final float BALANCED_RANDOM_X = 1250.0f;
	public static final float BALANCED_RANDOM_Y = 680.0f;
	public static final String CONFIRM_BUTTON_TEXT = "Embark";
	public static final String HEADER_TEXT = "Custom Climb";
	public static final float POSSIBLE_MODS_HEADER_X = 1000.0f;
	public static final float POSSIBLE_MODS_HEADER_Y = 730.0f;
	public static final String POSSIBLE_MODS_HEADER_TEXT = "Mods List";
	public static final float POSSIBLE_MODS_LIST_X = 1000.0f;
	public static final float POSSIBLE_MODS_LIST_Y = 690.0f;
	public static final float POSSIBLE_MODS_LIST_Y_DELTA = 60.0f;
	public static final float POSSIBLE_MODS_LIST_X_DELTA_DESC = 64.0f;
	public static final float POSSIBLE_MODS_LIST_X_BUTTON = 1450.0f;
	public static final float APPLIED_MODS_HEADER_X = 380.0f;
	public static final float APPLIED_MODS_HEADER_Y = 580.0f;
	public static final String APPLIED_MODS_HEADER_TEXT = "Applied Mods";
	public static final float APPLIED_MODS_LIST_X = 380.0f;
	public static final float APPLIED_MODS_LIST_Y = 540.0f;
	public static final float APPLIED_MODS_LIST_Y_DELTA = 60.0f;
	public static final float APPLIED_MODS_LIST_X_DELTA_DESC = 64.0f;
	public static final float APPLIED_MODS_LIST_X_BUTTON = 870.0f;
	public static final float MOD_LIST_LINE_WIDTH = 400.0f;
	public static final float MOD_LIST_LINE_SPACING = 30.0f;
	public static final float MOD_LIST_MIN_DELTA_Y = 80.0f;
	
	public static final float POSSIBLE_MODS_LEFT_ARROW_X = 1000.0f;
	public static final float POSSIBLE_MODS_LEFT_ARROW_Y = 190.0f;
	public static final float POSSIBLE_MODS_RIGHT_ARROW_X = 1160.0f;
	public static final float POSSIBLE_MODS_RIGHT_ARROW_Y = 190.0f;
	
	public static final float APPLIED_MODS_LEFT_ARROW_X = 380.0f;
	public static final float APPLIED_MODS_LEFT_ARROW_Y = 190.0f;
	public static final float APPLIED_MODS_RIGHT_ARROW_X = 540.0f;
	public static final float APPLIED_MODS_RIGHT_ARROW_Y = 190.0f;
	
	public static final String PAGE_TEXT = "Page";
	public static final float POSSIBLE_PAGE_TEXT_X = 1055.0f;
	public static final float POSSIBLE_PAGE_TEXT_Y = 200.0f;
	
	public static final float APPLIED_PAGE_TEXT_X = 435.0f;
	public static final float APPLIED_PAGE_TEXT_Y = 200.0f;
	
	public static final int POSSIBLE_PER_PAGE = 4;
	public static final int APPLIED_PER_PAGE = 2;
	
	public ConfirmButton confirmButton;
	private Object[] keys;
	private boolean madeUI = false;
	private int selected = -1;
	private Texture charOptHighlight = null;
	private Texture plusTexture;
	private Texture minusTexture;
	private boolean setSeed = false;
	private static final long DEFAULT_SEED = 0;
	private long seed = DEFAULT_SEED;
	private ModPanel climbPanel;
	private int modAmount = DEFAULT_MOD_AMOUNT;
	
	private ArrayList<AbstractDailyMod> possibleMods;
	private ArrayList<AbstractDailyMod> appliedMods = new ArrayList<>();
	private HashMap<AbstractDailyMod, Boolean> reallyPositive = new HashMap<>();
	private ArrayList<Hitbox> possibleHB = new ArrayList<>();
	private ArrayList<Hitbox> appliedHB = new ArrayList<>();
	private int possiblePage = 0;
	private int appliedPage = 0;
	private int maxPossiblePage;
	private int maxAppliedPage;
	private ModLabel possiblePageLabel, appliedPageLabel;
	
	private int getPossiblePageIndex(int index) {
		return possiblePage * POSSIBLE_PER_PAGE + index;
	}
	
	private AbstractDailyMod getPossibleModAtPageIndex(int index) {
		int realIndex = possiblePage * POSSIBLE_PER_PAGE + index;
		return possibleMods.get(realIndex);
	}
	
	private boolean hasPossibleModAtPageIndex(int index) {
		int realIndex = possiblePage * POSSIBLE_PER_PAGE + index;
		return (realIndex < possibleMods.size());
	}
	
	private int getAppliedPageIndex(int index) {
		return appliedPage * APPLIED_PER_PAGE + index;
	}
	
	private AbstractDailyMod getAppliedModAtPageIndex(int index) {
		int realIndex = appliedPage * APPLIED_PER_PAGE + index;
		return appliedMods.get(realIndex);
	}
	
	private boolean hasAppliedModAtPageIndex(int index) {
		int realIndex = appliedPage * APPLIED_PER_PAGE + index;
		return (realIndex < appliedMods.size());
	}
	
	private void setupHitboxes() {
		float y = POSSIBLE_MODS_LIST_Y * Settings.scale;
		
		for (int i = 0; i < POSSIBLE_PER_PAGE; i++) {
			if (!hasPossibleModAtPageIndex(i)) {
				continue;
			}
			
			possibleHB.add(new Hitbox(POSSIBLE_MODS_LIST_X_BUTTON * Settings.scale + (HB_SHRINK * Settings.scale)
					, y - 94.0f * Settings.scale + (HB_SHRINK * Settings.scale),
					plusTexture.getWidth() * Settings.scale - (2 * HB_SHRINK * Settings.scale),
					plusTexture.getHeight() * Settings.scale - (2 * HB_SHRINK * Settings.scale)));
			
			AbstractDailyMod mod = getPossibleModAtPageIndex(i);
			
			StringBuilder builder = new StringBuilder();
			
			if (mod.positive) {
				builder.append(FontHelper.colorString(mod.name, "g"));
			} else {
				builder.append(FontHelper.colorString(mod.name, "r"));
			}
			
			builder.append(": ");
			builder.append(mod.description);
			
			y += calcSpacing(builder.toString());
		}
		
		y = APPLIED_MODS_LIST_Y * Settings.scale;
		
		for (int i = 0; i < APPLIED_PER_PAGE; i++) {
			if (!hasAppliedModAtPageIndex(i)) {
				continue;
			}
			
			appliedHB.add(new Hitbox(APPLIED_MODS_LIST_X_BUTTON * Settings.scale + (HB_SHRINK * Settings.scale)
					, y - 94.0f * Settings.scale + (HB_SHRINK * Settings.scale),
					minusTexture.getWidth() * Settings.scale - (2 * HB_SHRINK * Settings.scale),
					minusTexture.getHeight() * Settings.scale - (2 * HB_SHRINK * Settings.scale)));
			
			AbstractDailyMod mod = getAppliedModAtPageIndex(i);
			
			StringBuilder builder = new StringBuilder();
			
			if (mod.positive) {
				builder.append(FontHelper.colorString(mod.name, "g"));
			} else {
				builder.append(FontHelper.colorString(mod.name, "r"));
			}
			
			builder.append(": ");
			builder.append(mod.description);
			
			y += calcSpacing(builder.toString());
		}
	}
	
	private void recalcHitboxes() {
		float y = POSSIBLE_MODS_LIST_Y * Settings.scale;
		
		for (int i = 0; i < POSSIBLE_PER_PAGE; i++) {
			if (!hasPossibleModAtPageIndex(i)) {
				continue;
			}
			
			possibleHB.get(i).y = y - 94.0f * Settings.scale + (HB_SHRINK * Settings.scale);
			
			AbstractDailyMod mod = getPossibleModAtPageIndex(i);
			
			StringBuilder builder = new StringBuilder();
			
			if (mod.positive) {
				builder.append(FontHelper.colorString(mod.name, "g"));
			} else {
				builder.append(FontHelper.colorString(mod.name, "r"));
			}
			
			builder.append(": ");
			builder.append(mod.description);
			
			y += calcSpacing(builder.toString());
		}
		
		y = APPLIED_MODS_LIST_Y * Settings.scale;
		
		for (int i = 0; i < APPLIED_PER_PAGE; i++) {
			if (!hasAppliedModAtPageIndex(i)) {
				continue;
			}
			
			appliedHB.get(i).y = y - 94.0f * Settings.scale + (HB_SHRINK * Settings.scale);
			
			AbstractDailyMod mod = getAppliedModAtPageIndex(i);
			
			StringBuilder builder = new StringBuilder();
			
			if (mod.positive) {
				builder.append(FontHelper.colorString(mod.name, "g"));
			} else {
				builder.append(FontHelper.colorString(mod.name, "r"));
			}
			
			builder.append(": ");
			builder.append(mod.description);
			
			y += calcSpacing(builder.toString());
		}
	}
	
	private void calcPageMax() {
		maxPossiblePage = (int) Math.ceil(((float) possibleMods.size()) / POSSIBLE_PER_PAGE);
		maxAppliedPage = (int) Math.ceil(((float) appliedMods.size()) / APPLIED_PER_PAGE);
		logger.info("recalculated max page amounts to possible: " + maxPossiblePage + " applied: " + maxAppliedPage);
	}
	
	@SuppressWarnings("unchecked")
	private void setupModLists() {
		possibleMods = new ArrayList<>();
		logger.info("begin setting up mods list");
		
		try {
			Field modsField = ModHelper.class.getDeclaredField("negativeMods");
			modsField.setAccessible(true);
			
			for (Map.Entry<String, AbstractDailyMod> m :
				((Map<String, AbstractDailyMod>) modsField.get(null)).entrySet()) {
				AbstractDailyMod mod = m.getValue();
				reallyPositive.put(mod, false);
				possibleMods.add(mod);
			}
			
			Field positiveModsField;
			positiveModsField = ModHelper.class.getDeclaredField("cardMods");
			positiveModsField.setAccessible(true);
			
			for (Map.Entry<String, AbstractDailyMod> m :
				((Map<String, AbstractDailyMod>) positiveModsField.get(null)).entrySet()) {
				AbstractDailyMod mod = m.getValue();
				reallyPositive.put(mod, true);
				possibleMods.add(mod);
			}
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			logger.error("could not read daily mod pool - leaving empty");
		}
		
		logger.info("added " + possibleMods.size() + " mods to the possible pool");
		appliedMods.add(possibleMods.remove(0));
		appliedMods.add(possibleMods.remove(0));
		appliedMods.add(possibleMods.remove(0));
		calcPageMax();
	}
	
	private void doSelect(int selected) {
		this.selected = selected;
		confirmButton.isDisabled = false;
		confirmButton.show();
	}
	
	private String buildPossiblePageText() {
		if (maxPossiblePage == 0) {
			return PAGE_TEXT + ": " + "0/0";
		}
		return PAGE_TEXT + ": " + (possiblePage + 1) + "/" + maxPossiblePage;
	}
	
	private String buildAppliedPageText() {
		if (maxAppliedPage == 0) {
			return PAGE_TEXT + ": " + "0/0";
		}
		return PAGE_TEXT + ": " + (appliedPage + 1) + "/" + maxAppliedPage;
	}
	
	private ModButton makeCharacterButton(int index, Object playerKey, ModPanel me) {
		String buttonPath = BaseMod.playerSelectButtonMap.get((String) playerKey);
		return new ModButton(
				CHAR_SELECT_X_START + CHAR_SELECT_X_DELTA * index,
				CHAR_SELECT_Y, makeTexture(buttonPath, 0.5f),
				me, (button) -> {doSelect(index);});
	}
	
	private void buildUI() {
		if (!madeUI) {
			// setup mod lists
			setupModLists();
			
			// load textures
			Texture badgeTexture = new Texture(makePath(BADGE_IMG));
			plusTexture = new Texture(makePath(PLUS_IMG));
			minusTexture = new Texture(makePath(MINUS_IMG));
			charOptHighlight = makeTexture("images/ui/charSelect/highlightButton2.png", 0.5f);
			
			// setup embark button
			confirmButton = new ConfirmButton(CONFIRM_BUTTON_TEXT);
			confirmButton.isDisabled = true;
			confirmButton.hide();
			
			// build the custom climb panel
			climbPanel = new ModPanel((me) -> {
				// character select can only be created after all mods have finished with receivePostInitialize is done
				ModButton ironclad = new ModButton(CHAR_SELECT_X_START, CHAR_SELECT_Y,
						makeTexture("images/ui/charSelect/ironcladButton.png", 0.5f),
						me, (button) -> {doSelect(0);});
				me.addUIElement(ironclad);
				
				ModButton silent = new ModButton(CHAR_SELECT_X_START + CHAR_SELECT_X_DELTA, CHAR_SELECT_Y,
						makeTexture("images/ui/charSelect/silentButton.png", 0.5f),
						me, (button) -> {doSelect(1);});
				me.addUIElement(silent);
				
				// custom characters
				int index = 2; // 0 for ironclad, 1 for silent, 2+ for custom chars
				keys = BaseMod.playerClassMap.keySet().toArray();
				for (Object playerKey : keys) {
					ModButton customCharacterButton = makeCharacterButton(index, playerKey, me);
					me.addUIElement(customCharacterButton);
					index++;
				}
				
				// setup hitboxes for mod list add and remove buttons
				setupHitboxes();
			}) {
				@Override
				public void render(SpriteBatch sb) {
					super.render(sb);
					renderMods(sb);
					confirmButton.render(sb);
				}
				
				@Override
				public void renderButtons(SpriteBatch sb) {
					if (selected >= 0) {
						sb.setColor(new Color(1.0F, 0.8F, 0.2F, 0.25F + 
								(MathUtils.cosDeg((float)
										(System.currentTimeMillis() / 4L % 360L)) + 1.25F) / 3.5F));
						sb.draw(charOptHighlight,
								(CHAR_SELECT_X_START + CHAR_SELECT_X_DELTA * selected) * Settings.scale,
								CHAR_SELECT_Y * Settings.scale,
								charOptHighlight.getWidth() * Settings.scale,
								charOptHighlight.getHeight() * Settings.scale);
					}
					super.renderButtons(sb);
				}
				
				@Override
				public void update() {
					super.update();
					confirmButton.update();
					recalcHitboxes();
					updateHitboxes();
					if (confirmButton.hb.clicked) {
						startRun();
					}
				}
			};
			
			// header
			ModLabel header = new ModLabel(HEADER_TEXT, HEADER_X, HEADER_Y,
					Settings.GOLD_COLOR, FontHelper.charTitleFont, climbPanel, (me) -> {});
			climbPanel.addUIElement(header);
			
			// possible mods header
			ModLabel possibleModsHeader = new ModLabel(POSSIBLE_MODS_HEADER_TEXT,
					POSSIBLE_MODS_HEADER_X, POSSIBLE_MODS_HEADER_Y, Settings.GOLD_COLOR,
					FontHelper.charDescFont, climbPanel, (me) -> {});
			climbPanel.addUIElement(possibleModsHeader);
			
			// applied mods header
			ModLabel appliedModsHeader = new ModLabel(APPLIED_MODS_HEADER_TEXT,
					APPLIED_MODS_HEADER_X, APPLIED_MODS_HEADER_Y, Settings.GOLD_COLOR,
					FontHelper.charDescFont, climbPanel, (me) -> {});
			climbPanel.addUIElement(appliedModsHeader);
			
			// page labels
			possiblePageLabel = new ModLabel(buildPossiblePageText(), POSSIBLE_PAGE_TEXT_X,
					POSSIBLE_PAGE_TEXT_Y, Settings.GOLD_COLOR, FontHelper.charDescFont,
					climbPanel, (me) -> {});
			climbPanel.addUIElement(possiblePageLabel);
			appliedPageLabel = new ModLabel(buildAppliedPageText(), APPLIED_PAGE_TEXT_X,
					APPLIED_PAGE_TEXT_Y, Settings.GOLD_COLOR, FontHelper.charDescFont,
					climbPanel, (me) -> {});
			climbPanel.addUIElement(appliedPageLabel);
			
			// page select arrow
			ModButton arrowLeftPossible = new ModButton(
					POSSIBLE_MODS_LEFT_ARROW_X,
					POSSIBLE_MODS_LEFT_ARROW_Y,
					ImageMaster.CF_LEFT_ARROW, climbPanel,
					(me) -> {
						possiblePage--;
						if (possiblePage < 0) {
							possiblePage = 0;
						}
						possiblePageLabel.text = buildPossiblePageText();
					});
			climbPanel.addUIElement(arrowLeftPossible);
			ModButton arrowRightPossible = new ModButton(
					POSSIBLE_MODS_RIGHT_ARROW_X,
					POSSIBLE_MODS_RIGHT_ARROW_Y,
					ImageMaster.CF_RIGHT_ARROW, climbPanel,
					(me) -> {
						possiblePage++;
						if (possiblePage >= maxPossiblePage) {
							possiblePage = maxPossiblePage - 1;
						}
						if (possiblePage < 0) {
							possiblePage = 0;
						}
						possiblePageLabel.text = buildPossiblePageText();
					});
			climbPanel.addUIElement(arrowRightPossible);
			ModButton arrowLeftApplied= new ModButton(
					APPLIED_MODS_LEFT_ARROW_X,
					APPLIED_MODS_LEFT_ARROW_Y,
					ImageMaster.CF_LEFT_ARROW, climbPanel,
					(me) -> {
						appliedPage--;
						if (appliedPage < 0) {
							appliedPage = 0;
						}
						appliedPageLabel.text = buildAppliedPageText();
					});
			climbPanel.addUIElement(arrowLeftApplied);
			ModButton arrowRightApplied = new ModButton(
					APPLIED_MODS_RIGHT_ARROW_X,
					APPLIED_MODS_RIGHT_ARROW_Y,
					ImageMaster.CF_RIGHT_ARROW, climbPanel,
					(me) -> {
						appliedPage++;
						if (appliedPage >= maxAppliedPage) {
							appliedPage = maxAppliedPage - 1;
						}
						if (appliedPage < 0) {
							appliedPage = 0;
						}
						appliedPageLabel.text = buildAppliedPageText();
					});
			climbPanel.addUIElement(arrowRightApplied);
			
			// seed select button
			ModButton seedButton = new ModButton(
					SEED_X, SEED_Y,
					new Texture(makePath(SEED_IMG)), climbPanel,
					(me) -> {
						BaseMod.openTextPanel(climbPanel, "Set the seed for the run", Long.toString(seed), Long.toString(DEFAULT_SEED), "Set the seed for the run", (panel) -> {
							// do nothing - was cancelled
						}, (panel) -> {
							this.seed = Long.parseLong(ModTextPanel.textField);
							this.setSeed = true;
							System.out.println("seed was set to " + this.seed);
						});
					});
			climbPanel.addUIElement(seedButton);
			
			// chaotic random button
			ModButton chaoticRandomButton = new ModButton(
					CHAOTIC_RANDOM_X, CHAOTIC_RANDOM_Y,
					new Texture(makePath(CHAOTIC_RANDOM_IMG)), climbPanel,
					(me) -> {
						BaseMod.openTextPanel(climbPanel, "Chaos!\n How many modifiers?", Integer.toString(modAmount), Integer.toString(DEFAULT_MOD_AMOUNT), "Chaos!\n How many modifiers?", (panel) -> {
							// do nothing - was cancelled
						}, (panel) -> {
							this.modAmount = Integer.parseInt(ModTextPanel.textField);
							randomizeMods(modAmount, false);
						});
					});
			climbPanel.addUIElement(chaoticRandomButton);
			
			// balanced random button
			ModButton balancedRandomButton = new ModButton(
					BALANCED_RANDOM_X, BALANCED_RANDOM_Y,
					new Texture(makePath(BALANCED_RANDOM_IMG)), climbPanel,
					(me) -> {
						BaseMod.openTextPanel(climbPanel, "Balance!\n How many modifiers?", Integer.toString(modAmount), Integer.toString(DEFAULT_MOD_AMOUNT), "Balance!\n How many modifiers?", (panel) -> {
							// do nothing - was cancelled
						}, (panel) -> {
							this.modAmount = Integer.parseInt(ModTextPanel.textField);
							randomizeMods(modAmount, true);
						});
					});
			climbPanel.addUIElement(balancedRandomButton);
			
			// register mod badge
			BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, climbPanel);
			madeUI = true;
		}
	}
	
	private void fixPages() {
		calcPageMax();
		if (possiblePage >= maxPossiblePage && possiblePage > 0) {
			possiblePage = maxPossiblePage - 1;
		}
		if (appliedPage >= maxAppliedPage && appliedPage > 0) {
			appliedPage = maxAppliedPage - 1;
		}
		possiblePageLabel.text = buildPossiblePageText();
		appliedPageLabel.text = buildAppliedPageText();
	}
	
	private void updateHitboxes() {
		int index = 0;
		boolean changed = false;
		for (Hitbox hb : possibleHB) {
			if (index >= (possibleMods.size() - possiblePage * POSSIBLE_PER_PAGE )) continue;
			
			hb.update();
			if (hb.justHovered) {
	            CardCrawlGame.sound.playV("UI_HOVER", 0.75f);
	        }
			
	        if (hb.hovered) {
	            if (InputHelper.justClickedLeft) {
	            	hb.clickStarted = true;
	            }
	        }
			if (hb.clicked) {
				logger.info("possible mods " + index + " clicked");
				hb.clicked = false;
				appliedMods.add(possibleMods.remove(getPossiblePageIndex(index)));
				changed = true;
			}
			index++;
		}
		index = 0;
		for (Hitbox hb : appliedHB) {
			if (index >= (appliedMods.size() - appliedPage * APPLIED_PER_PAGE )) continue;
			
			hb.update();
			if (hb.justHovered) {
	            CardCrawlGame.sound.playV("UI_HOVER", 0.75f);
	        }
			
	        if (hb.hovered) {
	            if (InputHelper.justClickedLeft) {
	            	hb.clickStarted = true;
	            }
	        }
			if (hb.clicked) {
				logger.info("applied mods " + index + " clicked");
				hb.clicked = false;
				possibleMods.add(appliedMods.remove(getAppliedPageIndex(index)));
				changed = true;
			}
			index++;
		}
		if (changed) {
			fixPages();
		}
	}
	
	public static final int DEFAULT_MOD_AMOUNT = 4;
	public static final float BALANCED_POSITIVE = 0.25f;
	
	private ArrayList<AbstractDailyMod> positives;
	private ArrayList<AbstractDailyMod> negatives;
	
	private void setPositivesAndNegatives() {
		positives = null;
		negatives = null;
		positives = new ArrayList<>();
		negatives = new ArrayList<>();
		for (int i = 0; i < possibleMods.size(); i++) {
			AbstractDailyMod mod = possibleMods.get(i);
			if (mod.positive) {
				positives.add(mod);
			} else {
				negatives.add(mod);
			}
		}
	}
	
	private void randomizeMods(int amount, boolean balanced) {
		Random random = new Random();
		
		while (appliedMods.size() > 0) {
			possibleMods.add(0, appliedMods.remove(0));
		}
		if (!balanced) {
			for (int i = 0; i < amount; i++) {
				if (possibleMods.size() > 0) {
					int listPos = random.nextInt(possibleMods.size());
					appliedMods.add(0, possibleMods.remove(listPos));			
				}
			}
		} else {
			int positiveAmount = (int) (BALANCED_POSITIVE * amount);
			if (positiveAmount < 1 && amount >= 1) {
				positiveAmount = 1;
			}
			int negativeAmount = amount - positiveAmount;
			setPositivesAndNegatives();
			for (int i = 0; i < positiveAmount; i++) {
				if (positives.size() > 0) {
					int listPos = random.nextInt(positives.size());
					AbstractDailyMod positiveMod = positives.remove(listPos);
					appliedMods.add(positiveMod);
					possibleMods.remove(positiveMod);
				}
			}
			for (int i = 0; i < negativeAmount; i++) {
				if (negatives.size() > 0) {
					int listPos = random.nextInt(negatives.size());
					AbstractDailyMod negativeMod = negatives.remove(listPos);
					appliedMods.add(negativeMod);
					possibleMods.remove(negativeMod);
				}
			}
		}
		fixPages();
	}
	
	private float calcSpacing(String desc) {
		float deltaY = FontHelper.getSmartHeight(FontHelper.charDescFont, desc,
				MOD_LIST_LINE_WIDTH * Settings.scale,
				MOD_LIST_LINE_SPACING * Settings.scale) -
				APPLIED_MODS_LIST_Y_DELTA * Settings.scale;
		if (deltaY > -1.0f * MOD_LIST_MIN_DELTA_Y * Settings.scale) {
			deltaY = -1.0f * MOD_LIST_MIN_DELTA_Y * Settings.scale;
		}
		return deltaY;
	}
	
	private void renderMods(SpriteBatch sb) {
		float y = POSSIBLE_MODS_LIST_Y * Settings.scale;
		
		for (int i = 0; i < POSSIBLE_PER_PAGE; i++) {
			if (i + possiblePage * POSSIBLE_PER_PAGE >= possibleMods.size()) continue;
			AbstractDailyMod mod = possibleMods.get(i + possiblePage * POSSIBLE_PER_PAGE);
			
			StringBuilder builder = new StringBuilder();
			
			if (mod.positive) {
				builder.append(FontHelper.colorString(mod.name, "g"));
			} else {
				builder.append(FontHelper.colorString(mod.name, "r"));
			}
			
			builder.append(": ");
			builder.append(mod.description);
			
			FontHelper.renderSmartText(sb, FontHelper.charDescFont, builder.toString(),
					(POSSIBLE_MODS_LIST_X + POSSIBLE_MODS_LIST_X_DELTA_DESC) * Settings.scale,
					y, MOD_LIST_LINE_WIDTH * Settings.scale,
					MOD_LIST_LINE_SPACING * Settings.scale, Settings.CREAM_COLOR);
			
			sb.draw(mod.img, POSSIBLE_MODS_LIST_X * Settings.scale, y - 44.0f * Settings.scale,
					64.0f * Settings.scale,
					64.0f * Settings.scale);
			
			sb.draw(plusTexture, POSSIBLE_MODS_LIST_X_BUTTON * Settings.scale, y - 94.0f * Settings.scale,
					plusTexture.getWidth() * Settings.scale, plusTexture.getHeight() * Settings.scale);
			
			y += calcSpacing(builder.toString());
		}
		
		y = APPLIED_MODS_LIST_Y * Settings.scale;
		
		for (int i = 0; i < APPLIED_PER_PAGE; i++) {
			if (i + appliedPage * APPLIED_PER_PAGE >= appliedMods.size()) continue;
			AbstractDailyMod mod = appliedMods.get(i + appliedPage * APPLIED_PER_PAGE);
				
			StringBuilder builder = new StringBuilder();
			
			if (mod.positive) {
				builder.append(FontHelper.colorString(mod.name, "g"));
			} else {
				builder.append(FontHelper.colorString(mod.name, "r"));
			}
			
			builder.append(": ");
			builder.append(mod.description);
			
			FontHelper.renderSmartText(sb, FontHelper.charDescFont, builder.toString(),
					(APPLIED_MODS_LIST_X + APPLIED_MODS_LIST_X_DELTA_DESC) * Settings.scale,
					y, MOD_LIST_LINE_WIDTH * Settings.scale,
					MOD_LIST_LINE_SPACING * Settings.scale, Settings.CREAM_COLOR);
			
			sb.draw(mod.img, APPLIED_MODS_LIST_X * Settings.scale, y - 44.0f * Settings.scale,
					64.0f * Settings.scale,
					64.0f * Settings.scale);
			
			sb.draw(minusTexture, APPLIED_MODS_LIST_X_BUTTON * Settings.scale, y - 94.0f * Settings.scale,
					minusTexture.getWidth() * Settings.scale, minusTexture.getHeight() * Settings.scale);
			
			
			y += calcSpacing(builder.toString());
		}
	}
	
	private void buildDailyMods() {
		DailyMods.setModsFalse();
		DailyMods.enabledMods.clear();
		DailyMods.enabledMods.addAll(appliedMods);
		for (AbstractDailyMod m : appliedMods) {
			if (reallyPositive.get(m)) {
				DailyMods.cardMods.put(m.modID, true);
			} else {
				DailyMods.negativeMods.put(m.modID, true);
			}
		}
	}
	
	private void startRun() {
		confirmButton.hb.clicked = false;
		confirmButton.isDisabled = true;
		confirmButton.hide();
		
		if (setSeed) {
			Settings.seed = seed;
		} else {
			Settings.seed = Long.valueOf(new Random().nextLong());
		}
		System.out.println("starting game with seed: " + Settings.seed);
		AbstractDungeon.generateSeeds();
		
		Settings.isDailyRun = true; // enable daily mods
		AbstractPlayer.PlayerClass chosen = AbstractPlayer.PlayerClass.IRONCLAD; // default to ironclad
		if (selected == 0) {
			chosen = AbstractPlayer.PlayerClass.IRONCLAD;
		} else if (selected == 1) {
			chosen = AbstractPlayer.PlayerClass.THE_SILENT;
		} else {
			int index = 2;
			boolean found = false;
			for (Object playerKey : keys) {
				if (!found && index == selected) {
					chosen = AbstractPlayer.PlayerClass.valueOf(playerKey.toString());
					found = true;
				}
				index++;
			}
			if (!found) {
				logger.error("could not find character - running as the ironclad");
				chosen = AbstractPlayer.PlayerClass.IRONCLAD;
			}
		}
		
		buildDailyMods();
		
		climbPanel.isUp = false;
		climbPanel.waitingOnEvent = false;
		BaseMod.modSettingsUp = false;
        Gdx.input.setInputProcessor(climbPanel.oldInputProcessor);
		CardCrawlGame.mainMenuScreen.lighten();
        CardCrawlGame.cancelButton.hideInstantly();
		CardCrawlGame.chosenCharacter = chosen;
		CardCrawlGame.mainMenuScreen.isFadingOut = true;
		CardCrawlGame.mainMenuScreen.fadeOutMusic();
	}

}
