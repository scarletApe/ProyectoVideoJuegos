package com.xdgames.adventube.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.xdgames.adventube.base.BaseScene;
import com.xdgames.adventube.constants.GameConstants;
import com.xdgames.adventube.manager.ResourcesManager;
import com.xdgames.adventube.manager.SceneManager;
import com.xdgames.adventube.manager.SceneManager.SceneType;

public class LevelSelectorScene extends BaseScene {

	private LevelItem levelItems[];

	@Override
	public void createScene() {
//		ResourcesManager.getInstance().loadGameResources();
		// TODO change the background image
		attachChild(new Sprite(0, 0, GameConstants.CAMERA_WIDTH,
				GameConstants.CAMERA_HEIGHT,
				resourcesManager.tv_background_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});

		levelItems = new LevelItem[6];

		float tx = 30;
		float ty = 30;
		float sw = 50;
		float sh = 50;
		levelItems[0] = new LevelItem( tx,  ty, sw, sh,
				ResourcesManager.getInstance().coin_region,
				engine.getVertexBufferObjectManager(), false, 0, "Desierto", 0);

		levelItems[1] = new LevelItem( tx+ 100,  ty, sw, sh,
				ResourcesManager.getInstance().coin_region,
				engine.getVertexBufferObjectManager(), false, 0, "Ciudad", 1);

		levelItems[2] = new LevelItem( tx,  ty+70, sw, sh,
				ResourcesManager.getInstance().coin_region,
				engine.getVertexBufferObjectManager(), false, 0, "Panteon", 2);

		levelItems[3] = new LevelItem( tx+100,  ty+70, sw, sh,
				ResourcesManager.getInstance().coin_region,
				engine.getVertexBufferObjectManager(), false, 0, "Fabrica", 3);
		
		levelItems[4] = new LevelItem( tx+200,  ty, sw, sh,
				ResourcesManager.getInstance().coin_region,
				engine.getVertexBufferObjectManager(), false, 0, "Bosque", 4);
		
		levelItems[5] = new LevelItem( tx+200,  ty+70, sw, sh,
				ResourcesManager.getInstance().coin_region,
				engine.getVertexBufferObjectManager(), false, 0, "Fairy Land", 5);

		// dont forget to attach them
		/* Register & Attach the levelTile object to the LevelSelector */
		// mScene.registerTouchArea(levelTile);
		for (int i = 0; i < levelItems.length; i++) {
			this.registerTouchArea(levelItems[i]);
			this.attachChild(levelItems[i]);
		}
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_LEVEL_SELECTOR;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub

	}
}

class LevelItem extends Sprite {

	private boolean isPassed;
	private int score;
	private String text;
	private int levelNumber;
	private Text mTileText;

	public LevelItem(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion, VertexBufferObjectManager pVBOM,
			boolean isPassed, int score, String text, int levelNum) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVBOM);
		// TODO Auto-generated constructor stub

		this.isPassed = isPassed;
		this.score = score;
		this.text = text;
		this.levelNumber = levelNum;

		changeText();
	}

	public void changeText() {
		int textPositionX = 0;
		int textPositionY = 45;
		String tempText = text ;//+ " " + score;
		/* Create the tile's text in the center of the tile */
		this.mTileText = new Text(textPositionX, textPositionY,
				ResourcesManager.getInstance().font_20px, tempText,
				tempText.length(),
				ResourcesManager.getInstance().engine
						.getVertexBufferObjectManager());

		/* Attach the Text to the LevelTile */
		this.attachChild(mTileText);
	}

	public boolean isPassed() {
		return isPassed;
	}

	public void setPassed(boolean isPassed) {
		this.isPassed = isPassed;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {

		/* If a level tile is initially pressed down on */
		if (pSceneTouchEvent.isActionDown()) {
   
			/**
			 * Example level loading: LevelSelector.this.hide();
			 * SceneManager.loadLevel(this.mLevelNumber);  
			 */  
			switch (levelNumber) {
			case 0: SceneManager.getInstance().loadGameScene(ResourcesManager.getInstance().engine, "grass_palm.tmx");
				break;
			case 1: SceneManager.getInstance().loadGameScene(ResourcesManager.getInstance().engine, "city.tmx");
				break;
			case 2: SceneManager.getInstance().loadGameScene(ResourcesManager.getInstance().engine, "panteon.tmx");
				break;
			case 3: SceneManager.getInstance().loadGameScene(ResourcesManager.getInstance().engine, "metal.tmx");
				break;
			case 4:SceneManager.getInstance().loadGameScene(ResourcesManager.getInstance().engine, "forest.tmx");
				break;
			case 5:
				SceneManager.getInstance().loadGameScene(ResourcesManager.getInstance().engine, "nice.tmx");
				break;
			default: SceneManager.getInstance().loadGameScene(ResourcesManager.getInstance().engine, "nice.tmx");
				break;
//				System.out.println("monkey");
			}

			return true;
		}
		

		return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX,
				pTouchAreaLocalY);
	}

}
