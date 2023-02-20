package com.voxby.game;

import java.util.ArrayList;

public class ChunkManager {

	private static ChunkManager instance = null;
	
	public synchronized static ChunkManager getInstance()
	{
		if(instance == null)
		{
			instance = new ChunkManager();
		}
		return instance;	
	}
	
	static int cubeCountDrawn = 0;
	
	private ArrayList<Chunk> ChunkList = new ArrayList<Chunk>();
	
	public enum ChunkType {
		ChunkType_Default(0),
		ChunkType_Land(1),
		ChunkType_Sphere(2);
				
		private int ChunkID;

		ChunkType(int i) {
			ChunkID = i;
		}

		public int GetID() {
			return ChunkID;
		}
	}
	
	public void render()
	{
		for(Chunk c : ChunkList)
		{
			c.render();
		}
	}
	
	public ChunkManager()
	{
		
	}
	
	public int GetChunkCount()
	{
		return ChunkList.size();
	}
	
	public Chunk GetChunk(int index)
	{
		if(index <= ChunkList.size())
		{
			Chunk c = ChunkList.get(index);
			return c;
		}
		return null;
	}
	
	public void AddChunk(ChunkType ct, int xOffset, int yOffset, int zOffset)
	{
		Chunk c = new Chunk(xOffset, yOffset, zOffset);
		ChunkList.add(c);
		switch(ct)
		{
			case ChunkType_Land:
				cubeCountDrawn += c.SetupLandscape();
			break;
			case ChunkType_Sphere:
				cubeCountDrawn += c.SetUpSphere();
			break;
			case ChunkType_Default:
				cubeCountDrawn += c.SetUpCube();
			break;
			default:
		}
	}
}
