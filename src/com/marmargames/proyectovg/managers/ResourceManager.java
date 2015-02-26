package com.marmargames.proyectovg.managers;

import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.graphics.Typeface;

public class ResourceManager extends Object {

	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final ResourceManager INSTANCE = new ResourceManager();

	// ====================================================
	// VARIABLES
	// ====================================================
	// We include these objects in the resource manager for
	// easy accessibility across our project.
	public Engine engine;
	public Context context;
	public float cameraWidth;
	public float cameraHeight;
	public float cameraScaleFactorX;
	public float cameraScaleFactorY;
	public BoundCamera camera;

	// The resource variables listed should be kept public, allowing us easy
	// access
	// to them when creating new Sprite and Text objects and to play sound
	// files.
	// ======================== Game Resources ================= //
	public static ITextureRegion gameBackgroundTextureRegion;

	// ======================== Menu Resources ================= //
	public static ITextureRegion menuBackgroundTextureRegion;

	// =================== Shared Game and Menu Resources ====== //
	public static ITiledTextureRegion buttonTiledTextureRegion;
	// public static ITextureRegion cloudTextureRegion;
	public static Sound clickSound;
	public static Font fontDefault32Bold;
	public static Font fontDefault32BoldWhite;
	public static Font fontDefault72Bold;

	// public Font font;

	// ---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	// ---------------------------------------------

	public ITextureRegion splash_region;
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITextureRegion options_region;

	public ITiledTextureRegion arrowLeftTextureRegion;
	public ITiledTextureRegion arrowRightTextureRegion;
	public ITiledTextureRegion arrowUpTextureRegion;
	public ITiledTextureRegion guyTextureRegion;
	public ITiledTextureRegion buttonSimpleTextureRegion;
	public ITiledTextureRegion frameTextureRegion;

	// Game Texture
	public BuildableBitmapTextureAtlas gameTextureAtlas;

	// Game Texture Regions
	public ITextureRegion platform1_region;
	public ITextureRegion platform2_region;
	public ITextureRegion platform3_region;
	public ITextureRegion coin_region;
	public ITiledTextureRegion player_region;

	private BitmapTextureAtlas splashTextureAtlas;
	private BuildableBitmapTextureAtlas menuTextureAtlas;

	// Level Complete Window
	public ITextureRegion complete_window_region;
	public ITiledTextureRegion complete_stars_region;

	// This variable will be used to revert the TextureFactory's default path
	// when we change it.
	private String mPreviousAssetBasePath = "";

	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	private ResourceManager() {
	}

	// ====================================================
	// GETTERS & SETTERS
	// ====================================================
	// Retrieves a global instance of the ResourceManager
	public static ResourceManager getInstance() {
		return INSTANCE;
	}

	// ====================================================
	// PUBLIC METHODS
	// ====================================================
	// Setup the ResourceManager
	public void setup(final Engine pEngine, final Context pContext,
			final float pCameraWidth, final float pCameraHeight,
			final float pCameraScaleX, final float pCameraScaleY, BoundCamera c) {
		engine = pEngine;
		context = pContext;
		cameraWidth = pCameraWidth;
		cameraHeight = pCameraHeight;
		cameraScaleFactorX = pCameraScaleX;
		cameraScaleFactorY = pCameraScaleY;
		camera = c;
	}

	// Loads all game resources.
	public static void loadGameResources() {
		getInstance().loadGameTextures();
		getInstance().loadSharedResources();
		getInstance().loadGameGraphics();
	}

	// Loads all menu resources
	public static void loadMenuResources() {
		getInstance().loadMenuTextures();
		getInstance().loadSharedResources();
	}

	// Unloads all game resources.
	public static void unloadGameResources() {
		getInstance().unloadGameTextures();
	}

	// Unloads all menu resources
	public static void unloadMenuResources() {
		getInstance().unloadMenuTextures();
	}

	// Unloads all shared resources
	public static void unloadSharedResources() {
		getInstance().unloadSharedTextures();
		getInstance().unloadSounds();
		getInstance().unloadFonts();
	}

	// ====================================================
	// PRIVATE METHODS
	// ====================================================
	// Loads resources used by both the game scenes and menu scenes
	private void loadSharedResources() {
		loadSharedTextures();
		loadSounds();
		loadFonts();
	}

	// ============================ LOAD TEXTURES (GAME) ================= //
	private void loadGameTextures() {
		// Store the current asset base path to apply it after we've loaded our
		// textures
		mPreviousAssetBasePath = BitmapTextureAtlasTextureRegionFactory
				.getAssetBasePath();
		// Set our game assets folder to "assets/gfx/game/"
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		// background texture - only load it if we need to:
		if (gameBackgroundTextureRegion == null) {
			BuildableBitmapTextureAtlas texture = new BuildableBitmapTextureAtlas(
					engine.getTextureManager(), 1024, 1024);
			// gameBackgroundTextureRegion =
			// BitmapTextureAtlasTextureRegionFactory
			// .createFromAsset(texture, context, "background.png");
			arrowLeftTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(texture, context, "arrow_left.png",2,1);
			arrowRightTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(texture, context, "arrow_right.png",2,1);
			arrowUpTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(texture, context, "arrow_up.png",2,1);
			guyTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(texture, context, "guy.png", 3, 1);
//			buttonSimpleTextureRegion = BitmapTextureAtlasTextureRegionFactory
//					.createFromAsset(texture, context, "button_simple.png");
			frameTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(texture, context, "frame.png",2,1);
			try {
				texture.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
						0, 0, 0));
				texture.load();
			} catch (TextureAtlasBuilderException e) {
				Debug.e(e);
			}
		}

		// Revert the Asset Path.
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath(mPreviousAssetBasePath);
	}

	private void loadGameGraphics() {
		// FontFactory.setAssetBasePath("font/");
		// final ITexture mainFontTexture = new BitmapTextureAtlas(
		// engine.getTextureManager(), 256, 256,
		// TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		//
		// font = FontFactory.createStrokeFromAsset(engine.getFontManager(),
		// mainFontTexture, context.getAssets(), "font.ttf", 50f, true,
		// 3, 2, 3);
		// font.load();

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		gameTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

		platform1_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, context, "platform1.png");
		platform2_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, context, "platform2.png");
		platform3_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, context, "platform3.png");
		coin_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameTextureAtlas, context, "coin.png");
//		player_region = BitmapTextureAtlasTextureRegionFactory
//				.createTiledFromAsset(gameTextureAtlas, context, "player.png",
//						3, 1);

		complete_window_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, context,
						"levelCompleteWindow.png");
		complete_stars_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameTextureAtlas, context, "star.png", 2,
						1);

		try {
			this.gameTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.gameTextureAtlas.load();
		} catch (final Exception e) {
			System.out.println("Loading game textures failed");
			Debug.e(e);
		}
	}

	// ============================ UNLOAD TEXTURES (GAME) =============== //
	private void unloadGameTextures() {
		// background texture - only unload it if it is loaded:
		if (gameBackgroundTextureRegion != null) {
			if (gameBackgroundTextureRegion.getTexture().isLoadedToHardware()) {
				gameBackgroundTextureRegion.getTexture().unload();
				gameBackgroundTextureRegion = null;
			}
		}
	}

	// ============================ LOAD TEXTURES (MENU) ================= //
	private void loadMenuTextures() {
		// Store the current asset base path to apply it after we've loaded our
		// textures
		mPreviousAssetBasePath = BitmapTextureAtlasTextureRegionFactory
				.getAssetBasePath();
		// Set our menu assets folder to "assets/gfx/menu/"
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");

		// background texture:
		if (menuBackgroundTextureRegion == null) {
			BuildableBitmapTextureAtlas texture = new BuildableBitmapTextureAtlas(
					engine.getTextureManager(), 800, 480);
			menuBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(texture, context, "room_mock.png");
			try {
				texture.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
						0, 0, 0));
				texture.load();
			} catch (TextureAtlasBuilderException e) {
				Debug.e(e);
			}
		}

		// Revert the Asset Path.
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath(mPreviousAssetBasePath);
	}

	// ============================ UNLOAD TEXTURES (MENU) =============== //
	private void unloadMenuTextures() {
		// background texture:
		if (menuBackgroundTextureRegion != null) {
			if (menuBackgroundTextureRegion.getTexture().isLoadedToHardware()) {
				menuBackgroundTextureRegion.getTexture().unload();
				menuBackgroundTextureRegion = null;
			}
		}
	}

	// ============================ LOAD TEXTURES (SHARED) ================= //
	private void loadSharedTextures() {
		// Store the current asset base path to apply it after we've loaded our
		// textures
		mPreviousAssetBasePath = BitmapTextureAtlasTextureRegionFactory
				.getAssetBasePath();
		// Set our shared assets folder to "assets/gfx/"
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		// button texture:
		if (buttonTiledTextureRegion == null) {
			BuildableBitmapTextureAtlas texture = new BuildableBitmapTextureAtlas(
					engine.getTextureManager(), 522, 74);
			buttonTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(texture, context, "button_simple.png", 2,
							1);
			try {
				texture.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
						0, 0, 0));
				texture.load();
			} catch (TextureAtlasBuilderException e) {
				Debug.e(e);
			}
		}

		// // cloud texture:
		// if (cloudTextureRegion == null) {
		// BuildableBitmapTextureAtlas texture = new
		// BuildableBitmapTextureAtlas(
		// engine.getTextureManager(), 266, 138);
		// cloudTextureRegion = BitmapTextureAtlasTextureRegionFactory
		// .createFromAsset(texture, context, "cloud.png");
		// try {
		// texture.build(new
		// BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,
		// BitmapTextureAtlas>(
		// 0, 0, 0));
		// texture.load();
		// } catch (TextureAtlasBuilderException e) {
		// Debug.e(e);
		// }
		// }

		// Revert the Asset Path.
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath(mPreviousAssetBasePath);
	}

	// ============================ UNLOAD TEXTURES (SHARED) ============= //
	private void unloadSharedTextures() {
		// button texture:
		if (buttonTiledTextureRegion != null) {
			if (buttonTiledTextureRegion.getTexture().isLoadedToHardware()) {
				buttonTiledTextureRegion.getTexture().unload();
				buttonTiledTextureRegion = null;
			}
		}
		// // cloud texture:
		// if (cloudTextureRegion != null) {
		// if (cloudTextureRegion.getTexture().isLoadedToHardware()) {
		// cloudTextureRegion.getTexture().unload();
		// cloudTextureRegion = null;
		// }
		// }
	}

	// =========================== LOAD SOUNDS ======================== //
	private void loadSounds() {
		SoundFactory.setAssetBasePath("sounds/");
		if (clickSound == null) {
			try {
				// Create the clickSound object via the SoundFactory class
				clickSound = SoundFactory.createSoundFromAsset(
						engine.getSoundManager(), context, "click.mp3");
			} catch (final IOException e) {
				// Log.v("Sounds Load", "Exception:" + e.getMessage());
			}
		}
	}

	// =========================== UNLOAD SOUNDS ====================== //
	private void unloadSounds() {
		if (clickSound != null)
			if (clickSound.isLoaded()) {
				// Unload the clickSound object. Make sure to stop it first.
				clickSound.stop();
				engine.getSoundManager().remove(clickSound);
				clickSound = null;
			}
	}

	// ============================ LOAD FONTS ========================== //
	private void loadFonts() {
		// Create the Font objects via FontFactory class
		if (fontDefault32Bold == null) {
			fontDefault32Bold = FontFactory.create(engine.getFontManager(),
					engine.getTextureManager(), 256, 256,
					Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32f,
					true, Color.BLACK_ABGR_PACKED_INT);
			fontDefault32Bold.load();
		}
		if (fontDefault32BoldWhite == null) {
			fontDefault32BoldWhite = FontFactory.create(engine.getFontManager(),
					engine.getTextureManager(), 256, 256,
					Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32f,
					true, Color.WHITE_ABGR_PACKED_INT);
			fontDefault32BoldWhite.load();
		}
		if (fontDefault72Bold == null) {
			fontDefault72Bold = FontFactory.create(engine.getFontManager(),
					engine.getTextureManager(), 512, 512,
					Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 72f,
					true, Color.BLACK_ABGR_PACKED_INT);
			fontDefault72Bold.load();
		}
	}

	// ============================ UNLOAD FONTS ======================== //
	private void unloadFonts() {
		// Unload the fonts
		if (fontDefault32Bold != null) {
			fontDefault32Bold.unload();
			fontDefault32Bold = null;
		}
		if (fontDefault72Bold != null) {
			fontDefault72Bold.unload();
			fontDefault72Bold = null;
		}
	}
}