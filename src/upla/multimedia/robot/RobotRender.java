/** 
 * Robot for android 1.5
 *
 * @Copyright (C) Thibault Saunier 2009 <saunierthibault@gmail.com>
 *
 * Based on several tutorial under apache licence accessible at:
 * <url>http://developer.android.com/guide/samples</url>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @Authors: Thibault Saunier <saunierthibault@gmail.com>
 */

package upla.multimedia.robot;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.view.KeyEvent;
import android.view.MotionEvent;

/** Class permiting to render the Robot.
 *  It implement extends the GLSurfaceView so it can handle event and implement callbacks
 *  It implement Renderer so it can render OpenGL SE element
 **/
public class RobotRender extends GLSurfaceView implements Renderer {
	/* Robot instance */
	private Cuerpo cuerpo;

	/* Rotation value
	 * The robot can turn only around the Y axis
	 **/
	private float yrot;
	/* Boolean to set if the background is black or white */
	private boolean mTranslucentBackground;
	/* Rotation speed values */
	private float yspeed;
	private float z = -5.0f;
	/* Light handling */
	private boolean light = false;

	/*
	 * The initial light values for ambient and diffuse
	 */
	private float[] lightAmbient = {0.5f, 0.5f, 0.5f, 1.0f};
	private float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
	private float[] lightPosition = {0.0f, 0.0f, 2.0f, 1.0f};

	/* The buffers for our light values */
	private FloatBuffer lightAmbientBuffer;
	private FloatBuffer lightDiffuseBuffer;
	private FloatBuffer lightPositionBuffer;
	private float oldX;
	private float oldY;
	private final float TOUCH_SCALE = 0.2f;

	/** The Activity Context */
	private Context context;
	public RobotRender(Context context, boolean useTranslucentBackground)
{
		super(context);

		mTranslucentBackground = useTranslucentBackground;

		//Set this as Renderer
		this.setRenderer(this);
		//Request focus, otherwise buttons won't react
		this.requestFocus();
		this.setFocusableInTouchMode(true);

		this.context = context;		/* Sets the context */

		/*set the ambient light buffer */
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(lightAmbient.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightAmbientBuffer = byteBuf.asFloatBuffer();
		lightAmbientBuffer.put(lightAmbient);
		lightAmbientBuffer.position(0);

		/*set the diffuser light buffer */
		byteBuf = ByteBuffer.allocateDirect(lightDiffuse.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightDiffuseBuffer = byteBuf.asFloatBuffer();
		lightDiffuseBuffer.put(lightDiffuse);
		lightDiffuseBuffer.position(0);

		/*set the light position bufffer */
		byteBuf = ByteBuffer.allocateDirect(lightPosition.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightPositionBuffer = byteBuf.asFloatBuffer();
		lightPositionBuffer.put(lightPosition);
		lightPositionBuffer.position(0);

		cuerpo = new Cuerpo();
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
		    GL10.GL_FASTEST);

		if (mTranslucentBackground) {
			gl.glClearColor(0,0,0,0);
		} else {
			gl.glClearColor(1,1,1,1);
		}
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_DEPTH_TEST);

		/* Light handling */
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbientBuffer);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuseBuffer);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPositionBuffer);
		gl.glEnable(GL10.GL_LIGHT0);

		gl.glDisable(GL10.GL_DITHER);        /* Avoid to get ugly rendering when moving */
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glShadeModel(GL10.GL_SMOOTH);

		if (mTranslucentBackground) {
			gl.glClearColor(0,0,0,0);
		} else {
			gl.glClearColor(1,1,1,1);
		}

		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_DEPTH_TEST);

	}

	/**
	 * Here we do our drawing
	 */
	public void onDrawFrame(GL10 gl) {
		//Clear Screen And Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		//Check if the light flag has been set to enable or not
		if(light)
			gl.glEnable(GL10.GL_LIGHTING);
		else
			gl.glDisable(GL10.GL_LIGHTING);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		gl.glTranslatef(0.0f, 0.0f, z);       /* Zoom handling */
		gl.glScalef(0.8f, 0.8f, 0.8f);
		gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);

		cuerpo.draw(gl);					//Draw the Cuerpo

		yrot += yspeed; /* Handle the rotation speed with the arrow */
	}

	/**
	 * If the surface changes, reset the view
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0)
			height = 1;

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();


		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	/**
	 * Override the key listener to receive keyUp events.
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		//
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
			yspeed -= 0.1f;
		else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
			yspeed += 0.1f;
		return true;
	}

	/**
	 * Callbacks
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/* get event position */
		float x = event.getX();
		float y = event.getY();

	/* Separate screen in 2 areas  to get differnt behavior depending on where the
	 * action took place */
		int upperArea = this.getHeight() / 10;
		int lowerArea = this.getHeight() - upperArea;

		/* Screen movment */
		if(event.getAction() == MotionEvent.ACTION_MOVE) {
			// Calculate the change
			float dx = x - oldX;
			float dy = y - oldY;

			if (y < upperArea)
				z -= dx * TOUCH_SCALE / 2;
			else
				yrot += dx * TOUCH_SCALE;

		} else if(event.getAction() == MotionEvent.ACTION_UP) {
			if(y > lowerArea) {
				if(light) {
					light = false;
				} else {
					light = true;
				}
			}
		}

		/* Save values */
		oldX = x;
		oldY = y;

		//We handled the event
		return true;
	}
}
