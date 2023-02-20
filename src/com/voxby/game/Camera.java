package com.voxby.game;

public interface Camera {
	
	public void yaw(float amount);
	public void pitch(float amount);
	void roll(float amount);
	public void walkForward(float distance);
	public void walkBackwards(float distance);
	public void strafeLeft(float distance);
	public void strafeRight(float distance);
}
