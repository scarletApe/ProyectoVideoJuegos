package com.marmargames.proyectovg.menus;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import com.marmargames.proyectovg.GameLevels.elements.JumpingGameLevel;
import com.marmargames.proyectovg.GameLevels.elements.TMXGameLevel;
import com.marmargames.proyectovg.managers.ResourceManager;
import com.marmargames.proyectovg.managers.SceneManager;

public class MainMenu extends ManagedMenuScene {
	
	private static final MainMenu INSTANCE = new MainMenu();
	public static MainMenu getInstance(){
		return INSTANCE;
	}
	
	public MainMenu() {
		this.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		this.setTouchAreaBindingOnActionDownEnabled(true);
		this.setTouchAreaBindingOnActionMoveEnabled(true);
	}
	
	// No loading screen means no reason to use the following methods.
	@Override
	public Scene onLoadingScreenLoadAndShown() {
		return null;
	}
	@Override
	public void onLoadingScreenUnloadAndHidden() {
	}
	
	// The objects that will make up our Main Menu
	private Sprite BackgroundSprite;
	private ButtonSprite PlayButton;
	private Text PlayButtonText;
	private ButtonSprite OptionsButton;
	private Text OptionsButtonText;
	private Sprite[] CloudSprites;
	private Text TitleText;
	@Override
	public void onLoadScene() {
		// Load the menu resources
		ResourceManager.loadMenuResources();
		
		// Create the background
		BackgroundSprite = new Sprite(0,0,ResourceManager.menuBackgroundTextureRegion,ResourceManager.getInstance().engine.getVertexBufferObjectManager());
//		BackgroundSprite.setScaleX(ResourceManager.getInstance().cameraWidth);
//		BackgroundSprite.setScaleY(ResourceManager.getInstance().cameraHeight/480f);
		BackgroundSprite.setZIndex(-5000);
		this.attachChild(BackgroundSprite);
		
//		// Create clouds that move from one side of the screen to the other, and repeat.
//		CloudSprites = new Sprite[20];
//		for(Sprite curCloudSprite: CloudSprites){
//			curCloudSprite = new Sprite(
//					MathUtils.random(-(/*this.getWidth()*/800*this.getScaleX())/2,ResourceManager.getInstance().cameraWidth+(/*this.getWidth()*/800*this.getScaleX())/2),
//					MathUtils.random(-(/*this.getHeight()*/480*this.getScaleY())/2,ResourceManager.getInstance().cameraHeight + (/*this.getHeight()*/480*this.getScaleY())/2),
//					ResourceManager.cloudTextureRegion,
//					ResourceManager.getInstance().engine.getVertexBufferObjectManager()) {
//				private float XSpeed = MathUtils.random(0.2f, 2f);
//				private boolean initialized = false;
//				@Override
//				protected void onManagedUpdate(final float pSecondsElapsed) {
//					super.onManagedUpdate(pSecondsElapsed);
//					if(!initialized) {
//						initialized = true;
//						this.setScale(XSpeed/2);
//						this.setZIndex(-4000+Math.round(XSpeed*1000f));
//						MainMenu.getInstance().sortChildren();
//					}
//					if(this.getX()<-(this.getWidth()*this.getScaleX())/2) {
//						XSpeed = MathUtils.random(0.2f, 2f);
//						this.setScale(XSpeed/2);
//						this.setPosition(ResourceManager.getInstance().cameraWidth+(this.getWidth()*this.getScaleX())/2, MathUtils.random(-(this.getHeight()*this.getScaleY())/2,ResourceManager.getInstance().cameraHeight + (this.getHeight()*this.getScaleY())/2));
//						
//						this.setZIndex(-4000+Math.round(XSpeed*1000f));
//						MainMenu.getInstance().sortChildren();
//					}
//					this.setPosition(this.getX()-(XSpeed*(pSecondsElapsed/0.016666f)), this.getY());
//				}
//			};
//			this.attachChild(curCloudSprite);
//		}
		
		// Create a Play button. Notice that the Game scenes, unlike menus, are not referred to in a static way.
		PlayButton = new ButtonSprite(
				500 ,
				200, 
				ResourceManager.buttonTiledTextureRegion.getTextureRegion(0), 
				ResourceManager.buttonTiledTextureRegion.getTextureRegion(1), 
				ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		PlayButtonText = new Text(0, 0, ResourceManager.fontDefault32Bold, "PLAY", ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		PlayButtonText.setPosition(10, 10);
		PlayButton.attachChild(PlayButtonText);
		this.attachChild(PlayButton);
		PlayButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// Create a new GameLevel and show it using the SceneManager. And play a click.
//				SceneManager.getInstance().showScene(new GameLevel());
//				SceneManager.getInstance().showScene(new TMXGameLevel());
				SceneManager.getInstance().showScene(new JumpingGameLevel());
				ResourceManager.clickSound.play();
			}});
		this.registerTouchArea(PlayButton);
		
		// Create an Option button. Notice that the SceneManager is being told to not pause the scene while the OptionsLayer is open.
		OptionsButton = new ButtonSprite(
				500, 
				PlayButton.getY()+80,
				ResourceManager.buttonTiledTextureRegion.getTextureRegion(0), 
				ResourceManager.buttonTiledTextureRegion.getTextureRegion(1), 
				ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		OptionsButtonText = new Text(0,0,ResourceManager.fontDefault32Bold,"OPTIONS",ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		OptionsButtonText.setPosition(10, 10);
		OptionsButton.attachChild(OptionsButtonText);
		this.attachChild(OptionsButton);
		OptionsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// Show the OptionsLayer and play a click.
				SceneManager.getInstance().showOptionsLayer(false);
				ResourceManager.clickSound.play();
			}});
		this.registerTouchArea(OptionsButton);
		
		// Create a title
		TitleText = new Text(0, 0, ResourceManager.fontDefault72Bold, "HAPPY BROS", ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		TitleText.setPosition(320, 100);
		TitleText.setColor(Color.WHITE);
		this.attachChild(TitleText);
		
	}
	
	@Override
	public void onShowScene() {
	}
	@Override
	public void onHideScene() {
	}
	@Override
	public void onUnloadScene() {
	}
}