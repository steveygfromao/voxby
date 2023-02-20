package com.voxby.game;

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glVertex3f;

import org.lwjgl.opengl.GL11;

import toxi.math.noise.SimplexNoise;

import com.voxby.game.Block.BlockType;

public class Chunk implements ChunkProtocol {

	private int chunkList;
	
	private int tx = 0; 
	private int ty = 0;
	private int tz = 0;
	
	// chunk size will be 16x16x16
	public static final int CHUNK_SIZE = 16;
	
	private Block[][][] Blocks; 

	// Offsets for where in world this chunk is
	private int xOffset = 0;
	private int yOffset = 0;
	private int zOffset = 0;
	
	public Block GetBlock(int x, int y, int z) {
		return Blocks[x][y][z];
	}

	public int rebuildChunk() {
		chunkList = glGenLists(1);
		glNewList(chunkList, GL_COMPILE);
		int count = createChunkList();
		glEndList();
		return count;
	}
	
	private int createChunkList() {

		tx = 0; ty = 0; tz = 0;
		int iHidden = 0;
		int iBlockCount = 0;
		boolean bLeft = !false;
		boolean bRight = !false;
		boolean bAbove = !false;
		boolean bBelow = !false;
		boolean bFront = !false;
		boolean bBack = !false;

		boolean bDefault = true;
		for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
			for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
				for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
					if(!GetBlock(x,y,z).IsActive())  // 
					{
						continue;
					}
					bLeft = bDefault;
					if(x > 0)
					{
						bLeft = Blocks[x-1][y][z].IsActive();
					} else bLeft = false;
					bRight = bDefault;
					if(x < Chunk.CHUNK_SIZE - 1)
					{
						bRight = Blocks[x+1][y][z].IsActive();
					} else bRight = false;
					bAbove = bDefault;
					if(y > 0)
					{
						bAbove = Blocks[x][y-1][z].IsActive();
					} else bAbove = false;
					bBelow = bDefault;
					if(y < Chunk.CHUNK_SIZE - 1)
					{
						bBelow = Blocks[x][y+1][z].IsActive();
					} else bBelow = false;
					bFront = bDefault;
					if(z > 0)
					{
						bFront = Blocks[x][y][z-1].IsActive();
					} else bFront = false;
					bBack = bDefault;
					if(z < Chunk.CHUNK_SIZE - 1)
					{
						bBack = Blocks[x][y][z+1].IsActive();
					} else bBack = false;
					
					boolean bResult = bLeft & bRight & bAbove &
							          bBelow & bFront & bBack;
					
					if(!bResult) // Block is not hidden by neighbouring blocks
					{
						tx = ((xOffset * Chunk.CHUNK_SIZE) << 1) + (x << 1);
	                    ty = ((yOffset * Chunk.CHUNK_SIZE) << 1) + (y << 1);
	                    tz = ((zOffset * Chunk.CHUNK_SIZE) << 1) + (z << 1);
	            		glEnable(GL11.GL_CULL_FACE);
//	            		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);

						glBegin(GL11.GL_QUADS);
						   renderBlock();
						glEnd();
						iBlockCount++;   // total of blocks that can be seen
					}
					else
						iHidden++;   // amount of blocks that are surrounded
				}
			}
		}
		System.out.println("Block amount hidden by neighbours:" + iHidden +
				" and Block total rendered:" + iBlockCount);
		return iBlockCount;

	}

	private void renderBlock() {

	         glColor3f(0.0f, 1.0f, 0.0f);
	         off_glVertex3f(1.0f, 1.0f, -1.0f);
	         off_glVertex3f(-1.0f, 1.0f, -1.0f);
	         off_glVertex3f(-1.0f, 1.0f, 1.0f);
	         off_glVertex3f(1.0f, 1.0f, 1.0f);
	         
	         glColor3f(0.0f, 0.4f, 0.0f);
	         off_glVertex3f(1.0f, -1.0f, 1.0f);
	         off_glVertex3f(-1.0f, -1.0f, 1.0f);
	         off_glVertex3f(-1.0f, -1.0f, -1.0f);
	         off_glVertex3f(1.0f, -1.0f, -1.0f);
	         
	         glColor3f(0.0f, 0.8f, 0.0f);
	         off_glVertex3f(1.0f, 1.0f, 1.0f);
	         off_glVertex3f(-1.0f, 1.0f, 1.0f);
	         off_glVertex3f(-1.0f, -1.0f, 1.0f);
	         off_glVertex3f(1.0f, -1.0f, 1.0f);
	         
	         glColor3f(0.0f, 0.7f, 0.0f);
	         off_glVertex3f(1.0f, -1.0f, -1.0f);
	         off_glVertex3f(-1.0f, -1.0f, -1.0f);
	         off_glVertex3f(-1.0f, 1.0f, -1.0f);
	         off_glVertex3f(1.0f, 1.0f, -1.0f);
	         
	         glColor3f(0.0f, 0.6f, 0.0f);
	         off_glVertex3f(-1.0f, 1.0f, 1.0f);
	         off_glVertex3f(-1.0f, 1.0f, -1.0f);
	         off_glVertex3f(-1.0f, -1.0f, -1.0f);
	         off_glVertex3f(-1.0f, -1.0f, 1.0f);
	         
	         glColor3f(0.0f, 0.5f, 0.0f);
	         off_glVertex3f(1.0f, 1.0f, -1.0f);
	         off_glVertex3f(1.0f, 1.0f, 1.0f);
	         off_glVertex3f(1.0f, -1.0f, 1.0f);
	         off_glVertex3f(1.0f, -1.0f, -1.0f);
	   }

	// Draw a vertex with a offset.
	private void off_glVertex3f(float x,float y,float z){
	         glVertex3f(tx+x,ty+y,tz+z);
	}
	
	public void Update() {

	}

	public void render()
	{
		GL11.glCallList(chunkList);
	}
	
	public int SetupLandscape()
	{
		float r = 1 + (float)Math.random()*16;
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int z = 0; z < CHUNK_SIZE; z++) {
				int height = (int) (SimplexNoise.noise(x / 16f, z / 16f) * 16f);
//				height += 10;
				if(height<=0) height = 1;//+(int)Math.random()*5;
				for(int y = 0; y < height; y++) // set these blocks on
				{
					Blocks[x][y][z].SetActive(true);
					Blocks[x][y][z].SetType(BlockType.BlockType_Grass);
				}
			}
		}
		return this.rebuildChunk();
	}
	
	public int SetUpCube()
	{
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_SIZE; y++) {
				for(int z = 0; z < CHUNK_SIZE; z++) // set these blocks on
				{
					Blocks[x][y][z].SetActive(true);
					Blocks[x][y][z].SetType(BlockType.BlockType_Grass);
				}
			}
		}
		return this.rebuildChunk();
	}
	
	
	public int SetUpSphere() {
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_SIZE; y++) {
				for (int z = 0; z < CHUNK_SIZE; z++) {
					int height = (int) (SimplexNoise.noise(x / 16F, z / 16F) * 16f);
					
					if (Math.sqrt((float) (x-CHUNK_SIZE/2)*(x-CHUNK_SIZE/2) + (y-CHUNK_SIZE/2)*(y-CHUNK_SIZE/2) + (z-CHUNK_SIZE/2)*(z-CHUNK_SIZE/2)) <= CHUNK_SIZE/2)
	                {
	                   Blocks[x][y][z].SetActive(true);
	                   Blocks[x][y][z].SetType(BlockType.BlockType_Grass);
	                }										
				}
			}
		}	
		return this.rebuildChunk();
	}
	
	public Chunk(int xOffset, int yOffset, int zOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.zOffset = zOffset;
		Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_SIZE; y++) {
				for (int z = 0; z < CHUNK_SIZE; z++) {
					Blocks[x][y][z] = new Block(BlockType.BlockType_Dirt);
					Blocks[x][y][z].SetActive(false);
				}
			}
		}
	}

}