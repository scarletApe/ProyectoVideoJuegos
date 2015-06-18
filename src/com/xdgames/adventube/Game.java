package com.xdgames.adventube;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;


import com.xdgames.adventube.constants.GameConstants;
import com.xdgames.adventube.manager.ResourcesManager;
import com.xdgames.adventube.manager.SceneManager;

public class Game extends SimpleBaseGameActivity {
	

	private BoundCamera mBoundChaseCamera;
	
	// Variables that hold references to the Layout and AdView
		public static FrameLayout mFrameLayout;
		public static AdView adView;
		
		// A method to show the ads.
		public void showAds() {
			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					adView.setVisibility(View.VISIBLE);
					final AdRequest adRequest = new AdRequest();
			        adView.loadAd(adRequest);
				}});
		}
		
		// Create and set the AdView.
	    @Override
	    protected void onSetContentView() {
	        final FrameLayout frameLayout = new FrameLayout(this);
	        final FrameLayout.LayoutParams frameLayoutLayoutParams =
	                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
	      //TODO: Replace String of X's with your code from Admob
	        adView = new AdView(this, AdSize.BANNER, "ca-app-pub-9133715731211941/8204561110");
	        adView.refreshDrawableState();
	        adView.setVisibility(AdView.VISIBLE);
	        final FrameLayout.LayoutParams adViewLayoutParams =
	                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.BOTTOM);
	        this.mRenderSurfaceView = new RenderSurfaceView(this);
	        mRenderSurfaceView.setRenderer(mEngine, this);
	        final android.widget.FrameLayout.LayoutParams surfaceViewLayoutParams =
	                new FrameLayout.LayoutParams(super.createSurfaceViewLayoutParams());
	        frameLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
	        frameLayout.addView(adView, adViewLayoutParams);
	        this.setContentView(frameLayout, frameLayoutLayoutParams);
	        mFrameLayout = frameLayout;
	    }

	@Override
	public EngineOptions onCreateEngineOptions() {

		this.mBoundChaseCamera = new BoundCamera(0, 0,
				GameConstants.CAMERA_WIDTH, GameConstants.CAMERA_HEIGHT);
		final EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				this.mBoundChaseCamera);
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getAudioOptions().setNeedsSound(true);

		return engineOptions;
	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {

		return new LimitedFPSEngine(pEngineOptions, 60);
	}

	@Override
	public void onCreateResources() {

		ResourcesManager.prepareManager(mEngine, this, mBoundChaseCamera,
				getVertexBufferObjectManager());

	}

	@Override
	public Scene onCreateScene() {
		
//		SceneManager.getInstance().setScene(SceneManager.getInstance().createSplashScene("andengine"));
		
		mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
		{
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                SceneManager.getInstance().createMenuScene();
            }
		}));
		 
		return SceneManager.getInstance().createSplashScene();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	    	SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
	    }
	    return false; 
	}

	

}
