package com.xdgames.adventube.manager;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
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
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import com.xdgames.adventube.Game;

import android.graphics.Color;

/**
 * @author Mateusz Mysliwiec
 * @author www.matim-dev.com
 * @version 1.0
 */
public class ResourcesManager {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private static final ResourcesManager INSTANCE = new ResourcesManager();

	public Engine engine;
	public Game activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;

	public Font font;
	public Font font_20px;

	// ---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	// ---------------------------------------------

//	public ITextureRegion splash_region;
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITextureRegion options_region;
	public ITextureRegion xd_games_region;
	public ITextureRegion tv_background_region;
	public ITextureRegion level_passed_region;
	public ITextureRegion exit_item_region;

	// Game Texture
	public BuildableBitmapTextureAtlas gameTextureAtlas;
	public BuildableBitmapTextureAtlas otherTextureAtlas;

	// Game Texture Regions
//	public ITextureRegion platform1_region;
//	public ITextureRegion platform2_region;
//	public ITextureRegion platform3_region;
	public ITextureRegion coin_region;
	public ITiledTextureRegion player_region;

	private BitmapTextureAtlas splashTextureAtlas;
	private BuildableBitmapTextureAtlas xdgamesTextureAtlas;

	// Level Complete Window
//	public ITextureRegion complete_window_region;
//	public ITiledTextureRegion complete_stars_region;

	public TextureRegion tiledTextureleftarrow;

	public TextureRegion tiledTexturerightarrow;

	public TextureRegion tiledTextureJump;

	public TextureRegion tiledTextureshoot;

	public TiledTextureRegion bullet_region;

	public TiledTextureRegion enemy_region;

	public Music music;
	
	public Music music_desierto;
	
	public Music music_panteon;
	
	public Music music_fabrica;

	public Sound jumpSound;

	

	

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuAudio();
		loadMenuFonts();
	}

	public void loadGameResources() {
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}
	
	public void loadAllOtherResources(){
		loadAllOtherGraphics();
		loadAllOtherFonts();
		loadAllOtherAudio();
	}

	private void loadMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		xdgamesTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 2000, 2000,
				TextureOptions.BILINEAR);
		menu_background_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(xdgamesTextureAtlas, activity, "menu_back.png");
		xd_games_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(xdgamesTextureAtlas, activity, "xd_games_logo.png");
		play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				xdgamesTextureAtlas, activity, "play.png");

		try {
			this.xdgamesTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.xdgamesTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	private void loadMenuAudio() {

	}

	private void loadMenuFonts() {
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(
				activity.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createStrokeFromAsset(activity.getFontManager(),
				mainFontTexture, activity.getAssets(), "font.ttf", 50, true,
				Color.WHITE, 2, Color.BLACK);
		font.load();
	}

	private void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/graphics/");
		gameTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		/* leftarrow button */
	
		this.tiledTextureleftarrow = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, activity,
						"_leftarrow.png");

		
		/* rightarrow button */
		
		this.tiledTexturerightarrow = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, activity,
						"_rightarrow.png");

		

		/* jump button */
		
		this.tiledTextureJump = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, activity,
						"jumpbutton.png");


		/* shoot button */
		
		this.tiledTextureshoot = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, activity,
						"shootbutton.png");

		
		
		player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "nino_sheet.png", 10, 2);
		
		
		bullet_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "fireball.png", 8, 1);
		
		enemy_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "mario_dog_sprite.png", 3, 1);
		
		
		
		
		try {
			this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.gameTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
		
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        
//       	platform1_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform1.png");
//       	platform2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform2.png");
//       	platform3_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform3.png");
        coin_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin.png");
        
//        complete_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "levelCompleteWindow.png");
//        complete_stars_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "star.png", 2, 1);

    	try 
    	{
			this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.gameTextureAtlas.load();
		} 
    	catch (final TextureAtlasBuilderException e)
    	{
			Debug.e(e);
		}
	}

	private void loadGameFonts() {

	}

	private void loadGameAudio() {
		MusicFactory.setAssetBasePath("audio/");
		SoundFactory.setAssetBasePath("audio/");
		
		try {
			this.music = MusicFactory.createMusicFromAsset(this.engine.getMusicManager(), activity, "dambient.mp3");
			this.music_desierto = MusicFactory.createMusicFromAsset(this.engine.getMusicManager(), activity, "celtic.wav");
			this.music_fabrica = MusicFactory.createMusicFromAsset(this.engine.getMusicManager(), activity, "dirtyjews.wav");
			this.music_panteon = MusicFactory.createMusicFromAsset(this.engine.getMusicManager(), activity, "panteon_music.wav");
			this.jumpSound = SoundFactory.createSoundFromAsset(this.engine.getSoundManager(), activity, "salto_devans.wav");
			this.music.setLooping(true);
		} catch (final IOException e) {
			Debug.e(e);
		}


	}

	public void unloadGameTextures() {
		gameTextureAtlas.unload();
	}

	public void loadSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), 180, 198, TextureOptions.BILINEAR);
//		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
//				splashTextureAtlas, activity, "graphics/logo.png", 0,
//				0);
		splashTextureAtlas.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		xdgamesTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 800, 480,
				TextureOptions.BILINEAR);
		xd_games_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(xdgamesTextureAtlas, activity, "xd_games_logo.png");
		try {
			this.xdgamesTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.xdgamesTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
//		splash_region = null;
	}

	public void unloadMenuTextures() {
		xdgamesTextureAtlas.unload();
	}

	public void loadMenuTextures() {
		xdgamesTextureAtlas.load();
	}

	/**
	 * @param engine
	 * @param activity
	 * @param camera
	 * @param vbom
	 * <br>
	 * <br>
	 *            We use this method at beginning of game loading, to prepare
	 *            Resources Manager properly, setting all needed parameters, so
	 *            we can latter access them from different classes (eg. scenes)
	 */
	public static void prepareManager(Engine engine, Game activity,
			BoundCamera camera, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
	}
	
	public void loadAllOtherGraphics(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		otherTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 2000, 2000,
				TextureOptions.BILINEAR);
		tv_background_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(otherTextureAtlas, activity, "tv_back.png");
		
		level_passed_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(otherTextureAtlas, activity, "menucomplete.png");
		
		exit_item_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(otherTextureAtlas, activity, "control.png");
		
		try {
			this.otherTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.otherTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
		
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(
				activity.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font_20px = FontFactory.createStrokeFromAsset(activity.getFontManager(),
				mainFontTexture, activity.getAssets(), "font.ttf", 20, true,
				Color.WHITE, 2, Color.BLACK);
		font_20px.load();
	}
	public void loadAllOtherFonts(){
		
	}
	public void loadAllOtherAudio(){
		
	}

	// ---------------------------------------------
	// GETTERS AND SETTERS
	// ---------------------------------------------

	public static ResourcesManager getInstance() {
		return INSTANCE;
	}
}