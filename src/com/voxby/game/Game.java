package com.voxby.game;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_PROJECTION_MATRIX;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glGetFloat;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.util.glu.GLU.gluLookAt;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector3f;

import com.voxby.game.ChunkManager.ChunkType;

/**
 * SDG Voxel Engine V1.0 Date Started: 22/2/2013
 * 
 * @author Steve Green
 */
public class Game {

	Vector3f eye = new Vector3f(0, 0, 70);
	Vector3f lineOfSight = new Vector3f(0, 0, -100);
	
	private int tx = 0;
	private int ty = 0;
	private int tz = 0;
	
	/** time at last frame */
	long lastFrame;

	/** frames per second */
	int fps;

	/** last fps time */
	long lastFPS;

	int voxelList;  
	int chunkList;
	int sphereList, sphereList2;
	
	String title = "SDG Voxby V1.0 ";
	
	private float projectionMatrixArray[] = new float[16];
	
	
	public void start() throws LWJGLException {
		try {
			// Create window
			Display.setFullscreen(false);
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setTitle(title);
			Display.setVSyncEnabled(true);

			PixelFormat pixelFormat = new PixelFormat();
			ContextAttribs contextAtrributes = new ContextAttribs(4, 0);
			contextAtrributes.withForwardCompatible(true);
			contextAtrributes.withProfileCore(true);
			Display.create(pixelFormat, contextAtrributes);
			glViewport(0, 0, Display.getDisplayMode().getWidth(), Display
					.getDisplayMode().getHeight());

			if(GLContext.getCapabilities().OpenGL20) {
				// opengl 2.0 supported.
				System.out.println("OpenGL20 supported");
			}
			ChunkManager.getInstance().AddChunk(ChunkType.ChunkType_Default, 2, 0, 0);

			ChunkManager.getInstance().AddChunk(ChunkType.ChunkType_Land, 0, 0, 0);
			ChunkManager.getInstance().AddChunk(ChunkType.ChunkType_Sphere,1,0,0);

		//	ChunkManager.getInstance().AddChunk(ChunkType.ChunkType_Land,1,0,0);
		/*	ChunkManager.getInstance().AddChunk(ChunkType.ChunkType_Land,2,0,0);
			ChunkManager.getInstance().AddChunk(ChunkType.ChunkType_Land,3,0,0);

			ChunkManager.getInstance().AddChunk(ChunkType.ChunkType_Land,0,0,1);
			ChunkManager.getInstance().AddChunk(ChunkType.ChunkType_Land,1,0,1);
			ChunkManager.getInstance().AddChunk(ChunkType.ChunkType_Land, 2, 0, 1);
			ChunkManager.getInstance().AddChunk(ChunkType.ChunkType_Land,3,0,1);

			ChunkManager.getInstance().AddChunk(ChunkType.ChunkType_Land,0,0,2);
			ChunkManager.getInstance().AddChunk(ChunkType.ChunkType_Land,1,0,2);
			ChunkManager.getInstance().AddChunk(ChunkType.ChunkType_Land, 2, 0, 2);
			ChunkManager.getInstance().AddChunk(ChunkType.ChunkType_Land,3,0,2);

			ChunkManager.getInstance().AddChunk(ChunkType.ChunkType_Land,0,0,3);
			ChunkManager.getInstance().AddChunk(ChunkType.ChunkType_Land,1,0,3);
			ChunkManager.getInstance().AddChunk(ChunkType.ChunkType_Land, 2, 0, 3);
			ChunkManager.getInstance().AddChunk(ChunkType.ChunkType_Land,3,0,3);
			
			ChunkManager.getInstance().AddChunk(ChunkType.ChunkType_Sphere,2,2,2);
*/

			// Initialise OpenGL - camera etc
			InitGL();
	
			// Initialise SkyBox
			SkyBox.initSkyBox(new Vector3f(0, 0, 50), new Vector3f(2f, 2f, 2f));

			Run();

		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;
		return delta;
	}

	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle(title + " - [FPS: " + fps + "] [Blocks: " +
						     ChunkManager.getInstance().cubeCountDrawn + 
						     "] [Chunks: " + ChunkManager.getInstance().GetChunkCount() +
						     "]");
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}

	private void Run() {
		FPCameraController camera = new FPCameraController(0, 0, 0);
		float dx = 0.0f;
		float dy = 0.0f;
		float dt = 0.0f; // length of frame
		long lastTime = 0; // when the last frame was
		long time = 0;

		float mouseSensitivity = 0.05f;
		float movementSpeed = 20.0f; // move 10 units per second

		// hide the mouse
		Mouse.setGrabbed(true);
		getDelta(); // call once before loop to initialise lastFrame

		lastFPS = getTime(); // call before loop to initialise fps timer

		while (!Display.isCloseRequested()
				&& !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {

			updateFPS();

			time = Sys.getTime();
			dt = (time - lastTime) / 1000.0f;
			lastTime = time;

			// distance in mouse movement from the last getDX() call.
			dx = Mouse.getDX();
			// distance in mouse movement from the last getDY() call.
			dy = Mouse.getDY();

			// control camera yaw from x movement fromt the mouse
			camera.yaw(dx * mouseSensitivity);
			// control camera pitch from y movement fromt the mouse
			camera.pitch(dy * mouseSensitivity);

			if (Keyboard.isKeyDown(Keyboard.KEY_W))// move forward
			{
				camera.walkForward(movementSpeed * dt);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_S))// move backwards
			{
				camera.walkBackwards(movementSpeed * dt);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_A))// strafe left
			{
				camera.strafeLeft(movementSpeed * dt);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_D))// strafe right
			{
				camera.strafeRight(movementSpeed * dt);
			}

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			SkyBox.renderSkyBox(camera.getCameraPitch(), camera.getCameraYaw(),
					0);

			glLoadIdentity();

			// look through the camera before you draw anything
			camera.lookThrough();

			// you would draw your scene here.
			try {
				ChunkManager.getInstance().render();
			
				Display.update();
				Display.sync(60);
			} catch (Exception e) {
			}
		}
		
		Display.destroy();
	}
	
	// Get the view frustum - TO DO for later, will be needed
	// for frustum culling
	private void GetViewFrustum() {
		ByteBuffer projMatrixBuffer = ByteBuffer.allocateDirect(16 * 4);
		projMatrixBuffer.order(ByteOrder.nativeOrder());
		glGetFloat(GL_PROJECTION_MATRIX, projMatrixBuffer.asFloatBuffer());

		for (int i = 0; i < 16; i++) {
			projectionMatrixArray[i] = projMatrixBuffer.asFloatBuffer().get(i);
			System.out.println(projMatrixBuffer.get(i) + " i: " + i);
		}
	}

	// Initialise OpenGL - projection and model view, depth test
	private void InitGL() {
		glEnable(GL_TEXTURE_2D);
		glShadeModel(GL_SMOOTH);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClearDepth(1.0);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		gluPerspective(45.0f, 800 / 600, 1.0f, 500.0f);

		gluLookAt(eye.x, eye.y, eye.z, lineOfSight.x, lineOfSight.y,
				lineOfSight.z, 0.0f, 1.0f, 0.0f);

		glMatrixMode(GL_MODELVIEW);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
	}

	// Main entry point
	public static void main(String[] argv) throws LWJGLException {
		new Game().start();
	}
}