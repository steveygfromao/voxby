package com.voxby.game;

import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ENABLE_BIT;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glNewList;

import static org.lwjgl.opengl.GL11.glRotatef;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

class SkyBox {

	static Vector3f position, size;

	static int cubeList;

	public static void initSkyBox(Vector3f position, Vector3f size) {
		SkyBox.position = position;
		SkyBox.size = size;

		cubeList = glGenLists(1);
		glNewList(cubeList, GL_COMPILE);
		drawCube();
		glEndList();
	}

	private static void drawCube() {
		
		glDisable(GL11.GL_CULL_FACE);
		glBegin(GL_QUADS); // FRONT
		glColor3f(50f/255, 171f/255, 222f/255);
		glVertex3f(-SkyBoxConstants.CUBEWIDTH, SkyBoxConstants.CUBEHEIGHT,
				SkyBoxConstants.CUBEHEIGHT);
		glVertex3f(SkyBoxConstants.CUBEWIDTH, SkyBoxConstants.CUBEHEIGHT,
				SkyBoxConstants.CUBEHEIGHT);
		glVertex3f(SkyBoxConstants.CUBEWIDTH, -SkyBoxConstants.CUBEHEIGHT,
				SkyBoxConstants.CUBEHEIGHT);
		glVertex3f(-SkyBoxConstants.CUBEWIDTH, -SkyBoxConstants.CUBEHEIGHT,
				SkyBoxConstants.CUBEHEIGHT);
		glEnd();

		glBegin(GL_QUADS);
		glColor3f(60f/255, 171f/255, 222f/255);
		glVertex3f(-SkyBoxConstants.CUBEWIDTH, SkyBoxConstants.CUBEHEIGHT,
				-SkyBoxConstants.CUBEHEIGHT);
		glVertex3f(SkyBoxConstants.CUBEWIDTH, SkyBoxConstants.CUBEHEIGHT,
				-SkyBoxConstants.CUBEHEIGHT);
		glVertex3f(SkyBoxConstants.CUBEWIDTH, -SkyBoxConstants.CUBEHEIGHT,
				-SkyBoxConstants.CUBEHEIGHT);
		glVertex3f(-SkyBoxConstants.CUBEWIDTH, -SkyBoxConstants.CUBEHEIGHT,
				-SkyBoxConstants.CUBEHEIGHT);
		glEnd();

		glBegin(GL_QUADS); // TOP
		glColor3f(70f/255, 171f/255, 222f/255);
		glVertex3f(-SkyBoxConstants.CUBEWIDTH, SkyBoxConstants.CUBEHEIGHT,
				SkyBoxConstants.CUBEHEIGHT);
		glVertex3f(-SkyBoxConstants.CUBEWIDTH, SkyBoxConstants.CUBEHEIGHT,
				-SkyBoxConstants.CUBEHEIGHT);
		glVertex3f(SkyBoxConstants.CUBEWIDTH, SkyBoxConstants.CUBEHEIGHT,
				-SkyBoxConstants.CUBEHEIGHT);
		glVertex3f(SkyBoxConstants.CUBEWIDTH, SkyBoxConstants.CUBEHEIGHT,
				SkyBoxConstants.CUBEHEIGHT);
		glEnd();

		glBegin(GL_QUADS); // BOTTOM
		glColor3f(80f/255, 171f/255, 222f/255);
		glVertex3f(-SkyBoxConstants.CUBEWIDTH, -SkyBoxConstants.CUBEHEIGHT,
				SkyBoxConstants.CUBEHEIGHT);
		glVertex3f(SkyBoxConstants.CUBEWIDTH, -SkyBoxConstants.CUBEHEIGHT,
				SkyBoxConstants.CUBEHEIGHT);
		glVertex3f(SkyBoxConstants.CUBEWIDTH, -SkyBoxConstants.CUBEHEIGHT,
				-SkyBoxConstants.CUBEHEIGHT);
		glVertex3f(-SkyBoxConstants.CUBEWIDTH, -SkyBoxConstants.CUBEHEIGHT,
				-SkyBoxConstants.CUBEHEIGHT);
		glEnd();

		glBegin(GL_QUADS); // RIGHT SIDE
		glColor3f(90f/255, 171f/255, 222f/255);
		glVertex3f(SkyBoxConstants.CUBEWIDTH, SkyBoxConstants.CUBEHEIGHT,
				SkyBoxConstants.CUBEHEIGHT);
		glVertex3f(SkyBoxConstants.CUBEWIDTH, -SkyBoxConstants.CUBEHEIGHT,
				SkyBoxConstants.CUBEHEIGHT);
		glVertex3f(SkyBoxConstants.CUBEWIDTH, -SkyBoxConstants.CUBEHEIGHT,
				-SkyBoxConstants.CUBEHEIGHT);
		glVertex3f(SkyBoxConstants.CUBEWIDTH, SkyBoxConstants.CUBEHEIGHT,
				-SkyBoxConstants.CUBEHEIGHT);
		glEnd();

		glBegin(GL_QUADS); // LEFT SIDE
		glColor3f(100f/255, 171f/255, 222f/255);
		glVertex3f(-SkyBoxConstants.CUBEWIDTH, SkyBoxConstants.CUBEHEIGHT,
				SkyBoxConstants.CUBEHEIGHT);
		glVertex3f(-SkyBoxConstants.CUBEWIDTH, -SkyBoxConstants.CUBEHEIGHT,
				SkyBoxConstants.CUBEHEIGHT);
		glVertex3f(-SkyBoxConstants.CUBEWIDTH, -SkyBoxConstants.CUBEHEIGHT,
				-SkyBoxConstants.CUBEHEIGHT);
		glVertex3f(-SkyBoxConstants.CUBEWIDTH, SkyBoxConstants.CUBEHEIGHT,
				-SkyBoxConstants.CUBEHEIGHT);
		glEnd();
	}

	public static void renderSkyBox(float xRot, float yRot, float zRot) {

		glPushMatrix();
		glLoadIdentity();

		glTranslatef(SkyBox.position.x, SkyBox.position.y, SkyBox.position.z);

		// rotate to camera
		glRotatef(yRot, 0.0f, 1.0f, 0.0f);
		glRotatef(xRot, 1.0f, 0.0f, 0.0f);

		glScalef(size.x, size.y, size.z);

		glPushAttrib(GL_ENABLE_BIT);
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);
		glDisable(GL_BLEND);

		glCallList(cubeList);
		
		glPopAttrib();
		glPopMatrix();

	}
}
