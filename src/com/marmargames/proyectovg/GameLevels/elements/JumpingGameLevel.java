package com.marmargames.proyectovg.GameLevels.elements;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.marmargames.proyectovg.managers.ResourceManager;
import com.marmargames.proyectovg.managers.SceneManager;

public class JumpingGameLevel extends ManagedGameScene implements
		IOnSceneTouchListener {
	private int score = 0;

	private HUD gameHUD;
	private Text scoreText;
	private PhysicsWorld physicsWorld;
	// private LevelCompleteWindow levelCompleteWindow;

	private static int level_width = 1400;// 1400;
	private static int level_height = 480;// 780;

	private Player player;

	private Text gameOverText;
	private boolean gameOverDisplayed = false;

	private boolean firstTouch = true;

	@Override
	public void onLoadScene() {
		ResourceManager.loadGameResources();

		initLevel();
		createHUD();
		// levelCompleteWindow = new LevelCompleteWindow(vbom);
		setOnSceneTouchListener(this);
	}

	private void initLevel() {
		// create the phisics World
		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0f,
				SensorManager.GRAVITY_EARTH * 2), false, 8, 3);
		physicsWorld.setContactListener(contactListener());
		registerUpdateHandler(physicsWorld);

		// set the background
		setBackground(new Background(Color.CYAN));

		// create the fixture definition
		final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0,
				0.01f, 0.5f);

		// here we set camera bounds
		ResourceManager.getInstance().camera.setBounds(0, 0, level_width,
				level_height);
		ResourceManager.getInstance().camera.setBoundsEnabled(true);

		// the ground where the character walks
		Rectangle rect = new Rectangle(0, level_height - 100, level_width, 100,
				ResourceManager.getInstance().engine
						.getVertexBufferObjectManager());
		Body bod = PhysicsFactory.createBoxBody(physicsWorld, rect,
				BodyType.StaticBody, FIXTURE_DEF);
		rect.setColor(Color.GREEN);
		this.attachChild(rect);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(rect, bod));

		// if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1))
		createPlatform(100, 300,
				ResourceManager.getInstance().platform1_region, "platform1",
				BodyType.StaticBody, FIXTURE_DEF);
		createPlatform(200, 250,
				ResourceManager.getInstance().platform1_region, "platform1",
				BodyType.StaticBody, FIXTURE_DEF);

		// else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2))
		createPlatform(300, 200,
				ResourceManager.getInstance().platform2_region, "platform2",
				BodyType.StaticBody, FIXTURE_DEF);
		createPlatform(400, 150,
				ResourceManager.getInstance().platform2_region, "platform2",
				BodyType.StaticBody, FIXTURE_DEF);
		createPlatform(500, 300,
				ResourceManager.getInstance().platform2_region, "platform2",
				BodyType.StaticBody, FIXTURE_DEF);

		// else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3))
		createPlatform(600, 250,
				ResourceManager.getInstance().platform3_region, "platform3",
				BodyType.StaticBody, FIXTURE_DEF);
		createPlatform(700, 150,
				ResourceManager.getInstance().platform3_region, "platform3",
				BodyType.StaticBody, FIXTURE_DEF);

		// else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN))
		creatCoin(800, 340);
		creatCoin(500, 340);
		creatCoin(300, 340);

		// else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER))
		player = new Player(20, 140,
				ResourceManager.getInstance().engine
						.getVertexBufferObjectManager(),
				ResourceManager.getInstance().camera, physicsWorld) {
			@Override
			public void onDie() {
				if (!gameOverDisplayed) {
					displayGameOverText();
				}
			}
		};
		player.setCullingEnabled(true);
		attachChild(player);

		// else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE))
		createFinish();

		// create the game over text
		gameOverText = new Text(0, 0,
				ResourceManager.getInstance().fontDefault32Bold, "Game Over!",
				ResourceManager.getInstance().engine
						.getVertexBufferObjectManager());

	}

	// @Override
	// public void onBackKeyPressed() {
	// SceneManager.getInstance().loadMenuScene(engine);
	// }
	//
	// @Override
	// public SceneType getSceneType() {
	// return SceneType.SCENE_GAME;
	// }
	//
	// @Override
	// public void disposeScene() {
	// camera.setHUD(null);
	// camera.setChaseEntity(null);
	// camera.setCenter(400, 240);
	//
	// // TODO code responsible for disposing scene
	// // removing all game scene objects.
	// }

	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
//		if (pSceneTouchEvent.isActionDown()) {
//			if (firstTouch) {
//				player.setRunning();
//				firstTouch = false;
//			} else {
//				player.jump();
//				// System.out.println("fuck!!");
//			}
//		}
		return false;
	}

	private void displayGameOverText() {
		ResourceManager.getInstance().camera.setChaseEntity(null);
		gameOverText.setPosition(
				ResourceManager.getInstance().camera.getCenterX(),
				ResourceManager.getInstance().camera.getCenterY());
		attachChild(gameOverText);
		gameOverDisplayed = true;
		SceneManager.getInstance().showMainMenu();
	}

	private void createHUD() {
		// gameHUD = new HUD();

		scoreText = new Text(10, 10, ResourceManager.fontDefault32Bold,
				"Score: 0123456789", new TextOptions(HorizontalAlign.LEFT),
				ResourceManager.getInstance().engine
						.getVertexBufferObjectManager());
		scoreText.setText("Score: 0");
		GameHud.attachChild(scoreText);
		// ResourceManager.getInstance().camera.setHUD(gameHUD);

		// // Setup the HUD Buttons and Button Texts.
		// // Take note of what happens when the buttons are clicked.
		// ButtonSprite MainMenuButton = new ButtonSprite(0f, 0f,
		// ResourceManager.buttonTiledTextureRegion.getTextureRegion(0),
		// ResourceManager.buttonTiledTextureRegion.getTextureRegion(1),
		// ResourceManager.getInstance().engine
		// .getVertexBufferObjectManager());
		// MainMenuButton.setScale(
		// 1 / ResourceManager.getInstance().cameraScaleFactorX,
		// 1 / ResourceManager.getInstance().cameraScaleFactorY);
		// MainMenuButton.setPosition(0, 0);
		// MainMenuButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(ButtonSprite pButtonSprite,
		// float pTouchAreaLocalX, float pTouchAreaLocalY) {
		// // Play the click sound and show the Main Menu.
		// ResourceManager.clickSound.play();
		// SceneManager.getInstance().showMainMenu();
		// }
		// });
		//
		// Text MainMenuButtonText = new Text(10, 10,
		// ResourceManager.fontDefault32Bold, "MENU",
		// ResourceManager.getInstance().engine
		// .getVertexBufferObjectManager());
		// MainMenuButton.attachChild(MainMenuButtonText);
		// GameHud.attachChild(MainMenuButton);
		// GameHud.registerTouchArea(MainMenuButton);
		//
		ButtonSprite OptionsButton = new ButtonSprite(0f, 0f,
				ResourceManager.buttonTiledTextureRegion.getTextureRegion(0),
				ResourceManager.buttonTiledTextureRegion.getTextureRegion(1),
				ResourceManager.getInstance().engine
						.getVertexBufferObjectManager());
		OptionsButton.setScale(
				1 / ResourceManager.getInstance().cameraScaleFactorX,
				1 / ResourceManager.getInstance().cameraScaleFactorY);
		OptionsButton.setPosition(800f - OptionsButton.getWidth(), 0);
		OptionsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// Play the click sound and show the Options Layer.
				ResourceManager.clickSound.play();
				SceneManager.getInstance().showOptionsLayer(true);
			}
		});

		Text OptionsButtonText = new Text(0, 0,
				ResourceManager.fontDefault32Bold, "OPTIONS",
				ResourceManager.getInstance().engine
						.getVertexBufferObjectManager());
		OptionsButtonText.setPosition(10, 10);
		OptionsButton.attachChild(OptionsButtonText);
		GameHud.attachChild(OptionsButton);
		GameHud.registerTouchArea(OptionsButton);

		// button for left arrow
		ButtonSprite arrowLeftButton = new ButtonSprite(0f, 0f,
				ResourceManager.getInstance().arrowLeftTextureRegion
						.getTextureRegion(0),
				ResourceManager.getInstance().arrowLeftTextureRegion
						.getTextureRegion(1),
				ResourceManager.getInstance().engine
						.getVertexBufferObjectManager());
		arrowLeftButton.setPosition(
				0,
				ResourceManager.getInstance().cameraHeight
						- arrowLeftButton.getHeight());
		arrowLeftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// make the player run left
				player.runLeft();
			}
		});
		GameHud.attachChild(arrowLeftButton);
		GameHud.registerTouchArea(arrowLeftButton);

		// button for right arrow
		ButtonSprite arrowRightButton = new ButtonSprite(0f, 0f,
				ResourceManager.getInstance().arrowRightTextureRegion
						.getTextureRegion(0),
				ResourceManager.getInstance().arrowRightTextureRegion
						.getTextureRegion(1),
				ResourceManager.getInstance().engine
						.getVertexBufferObjectManager());
		arrowRightButton.setPosition(
				arrowLeftButton.getWidth(),
				ResourceManager.getInstance().cameraHeight
						- arrowLeftButton.getHeight());
		arrowRightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// make the player run right
				player.runRight();
			}
		});
		GameHud.attachChild(arrowRightButton);
		GameHud.registerTouchArea(arrowRightButton);

		// button for up arrow
		ButtonSprite arrowUpButton = new ButtonSprite(0f, 0f,
				ResourceManager.getInstance().arrowUpTextureRegion
						.getTextureRegion(0),
				ResourceManager.getInstance().arrowUpTextureRegion
						.getTextureRegion(1),
				ResourceManager.getInstance().engine
						.getVertexBufferObjectManager());
		arrowUpButton.setPosition(
				ResourceManager.getInstance().cameraWidth
						- arrowUpButton.getWidth() * 2,
				ResourceManager.getInstance().cameraHeight
						- arrowUpButton.getHeight());
		arrowUpButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// make the player jump
				player.jump();
			}
		});
		GameHud.attachChild(arrowUpButton);
		GameHud.registerTouchArea(arrowUpButton);

		// button for the gun
		ButtonSprite gunButton = new ButtonSprite(0f, 0f,
				ResourceManager.getInstance().frameTextureRegion
						.getTextureRegion(0),
				ResourceManager.getInstance().frameTextureRegion
						.getTextureRegion(1),
				ResourceManager.getInstance().engine
						.getVertexBufferObjectManager());
		gunButton.setPosition(
				ResourceManager.getInstance().cameraWidth
						- gunButton.getWidth(),
				ResourceManager.getInstance().cameraHeight
						- gunButton.getHeight());
		gunButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// // Play the click sound and show the Options Layer.
				// ResourceManager.clickSound.play();
				// SceneManager.getInstance().showOptionsLayer(true);
			}
		});
		GameHud.attachChild(gunButton);
		GameHud.registerTouchArea(gunButton);
	}

	private void addToScore(int i) {
		score += i;
		scoreText.setText("Score: " + score);
	}

	private ContactListener contactListener() {
		ContactListener contactListener = new ContactListener() {
			public void beginContact(Contact contact) {
				System.out.println("Begin Contact");
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();

				if (x1.getBody().getUserData() != null
						&& x2.getBody().getUserData() != null) {
					if (x2.getBody().getUserData().equals("player")) {
						player.increaseFootContacts();
					}

					if (x1.getBody().getUserData().equals("platform2")
							&& x2.getBody().getUserData().equals("player")) {
						ResourceManager.getInstance().engine
								.registerUpdateHandler(new TimerHandler(0.2f,
										new ITimerCallback() {
											public void onTimePassed(
													final TimerHandler pTimerHandler) {
												pTimerHandler.reset();
												ResourceManager.getInstance().engine
														.unregisterUpdateHandler(pTimerHandler);
												x1.getBody().setType(
														BodyType.DynamicBody);
											}
										}));
					}

					if (x1.getBody().getUserData().equals("platform3")
							&& x2.getBody().getUserData().equals("player")) {
						x1.getBody().setType(BodyType.DynamicBody);
					}
				}
			}

			public void endContact(Contact contact) {
				System.out.println("End Contact");
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();

				if (x1.getBody().getUserData() != null
						&& x2.getBody().getUserData() != null) {
					if (x2.getBody().getUserData().equals("player")) {
						player.decreaseFootContacts();
					}
				}
			}

			public void preSolve(Contact contact, Manifold oldManifold) {

			}

			public void postSolve(Contact contact, ContactImpulse impulse) {

			}
		};
		return contactListener;
	}

	private void createPlatform(float px, float py, ITextureRegion tr,
			String userData, BodyType bodyType, FixtureDef fixDef) {
		Sprite levelObject = null;
		if (tr != null) {
			System.out.println("tr not null");
		} else {
			System.out.println("tr IS null");
		}
		if (ResourceManager.getInstance().engine.getVertexBufferObjectManager() != null) {
			System.out.println("vbom not null");
		}
		try {
			levelObject = new Sprite(px, py, tr,
					ResourceManager.getInstance().engine
							.getVertexBufferObjectManager());
		} catch (Exception e) {
			System.out.println(e);
		}
		Body bod = PhysicsFactory.createBoxBody(physicsWorld, levelObject,
				bodyType, fixDef);
		bod.setUserData(userData);
		levelObject.setCullingEnabled(true);
		this.attachChild(levelObject);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject,
				bod));
	}

	private void creatCoin(float px, float py) {
		Sprite levelObject = new Sprite(px, py,
				ResourceManager.getInstance().coin_region,
				ResourceManager.getInstance().engine
						.getVertexBufferObjectManager()) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (player.collidesWith(this)) {
					addToScore(10);
					this.setVisible(false);
					this.setIgnoreUpdate(true);
				}
			}
		};
		levelObject.registerEntityModifier(new LoopEntityModifier(
				new ScaleModifier(1, 1, 1.3f)));
		levelObject.setCullingEnabled(true);
		this.attachChild(levelObject);
	}

	private void createFinish() {
		Sprite levelObject = new Sprite(
				level_width
						- ResourceManager.getInstance().complete_stars_region
								.getWidth(),
				480 - 100 - ResourceManager.getInstance().complete_stars_region
						.getHeight(),
				ResourceManager.getInstance().complete_stars_region,
				ResourceManager.getInstance().engine
						.getVertexBufferObjectManager()) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (player.collidesWith(this)) {
					// levelCompleteWindow.display(StarsCount.TWO,
					// GameScene.this,
					// camera);
					this.setVisible(false);
					this.setIgnoreUpdate(true);
				}
			}
		};
		levelObject.registerEntityModifier(new LoopEntityModifier(
				new ScaleModifier(1, 1, 1.3f)));
		levelObject.setCullingEnabled(true);
		this.attachChild(levelObject);
	}
}

abstract class Player extends AnimatedSprite {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private Body body;

	private boolean canRun = false;

	private int footContacts = 0;

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public Player(float pX, float pY, VertexBufferObjectManager vbo,
			Camera camera, PhysicsWorld physicsWorld) {
		super(pX, pY, ResourceManager.getInstance().guyTextureRegion, vbo);
		createPhysics(camera, physicsWorld);
		camera.setChaseEntity(this);
	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) {
		body = PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		body.setUserData("player");
		body.setFixedRotation(true);

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body,
				true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				if (getY() >= 480) {
					onDie();
				}

				// if (canRun) {
				// body.setLinearVelocity(new Vector2(2, body
				// .getLinearVelocity().y));
				// }
			}
		});
	}

	public void runLeft() {
		body.setLinearVelocity(new Vector2(body.getLinearVelocity().x-2, body.getLinearVelocity().y));
		if(!canRun){
			setRunning();
		}
	}

	public void runRight() {
		body.setLinearVelocity(new Vector2(body.getLinearVelocity().x+2, body.getLinearVelocity().y));
		if(!canRun){
			setRunning();
		}
	}

	public void setRunning() {
		canRun = true;

		final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };

		animate(PLAYER_ANIMATE, 0, 2, true);
	}

	public void jump() {
		// if (footContacts < 1)
		// {
		// return;
		// }
		body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, body
				.getLinearVelocity().y - 11));
		System.out.println("Fucking ehh");
	}

	public void increaseFootContacts() {
		footContacts++;
	}

	public void decreaseFootContacts() {
		footContacts--;
	}

	public abstract void onDie();
}