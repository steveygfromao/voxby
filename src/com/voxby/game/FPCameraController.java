package com.voxby.game;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
 
public class FPCameraController implements Camera {

	//3d vector to store the camera's position in
    private Vector3f    position    = null;
    //the rotation around the Y axis of the camera
    private float       yaw         = 0.0f;
    //the rotation around the X axis of the camera
    private float       pitch       = 0.0f;
    
  //Constructor that takes the starting x, y, z location of the camera
    public FPCameraController(float x, float y, float z)
    {
        position = new Vector3f(x, y, z);
    }

    public float getCameraYaw() { return yaw; }
    public float getCameraPitch() { return pitch; }
    
  //increment the camera's current yaw rotation
    public void yaw(float amount)
    {
        //increment the yaw by the amount param
    	if(yaw < 0.0) yaw += 360;
    	if(yaw > 360) yaw -= 360;
        yaw += amount;
    }
     
    //increment the camera's current pitch rotation
    public void pitch(float amount)
    {
        //increment the pitch by the amount param
    	if(pitch > 90) pitch = 90;
    	if(pitch < -90) pitch = -90;
        pitch += amount;
    }
    
    public void roll(float amount)
    {
    	// not used
    }
    
    
  //moves the camera forward relative to its current rotation (yaw)
    public void walkForward(float distance)
    {
        position.x -= distance * (float)Math.sin(Math.toRadians(yaw));
        position.z += distance * (float)Math.cos(Math.toRadians(yaw));
    }
     
    //moves the camera backward relative to its current rotation (yaw)
    public void walkBackwards(float distance)
    {
        position.x += distance * (float)Math.sin(Math.toRadians(yaw));
        position.z -= distance * (float)Math.cos(Math.toRadians(yaw));
    }
     
    //strafes the camera left relative to its current rotation (yaw)
    public void strafeLeft(float distance)
    {
        position.x -= distance * (float)Math.sin(Math.toRadians(yaw-90));
        position.z += distance * (float)Math.cos(Math.toRadians(yaw-90));
    }
     
    //strafes the camera right relative to its current rotation (yaw)
    public void strafeRight(float distance)
    {
        position.x -= distance * (float)Math.sin(Math.toRadians(yaw+90));
        position.z += distance * (float)Math.cos(Math.toRadians(yaw+90));
    }
    

    //translates and rotate the matrix so that it looks through the camera
    //this does basically what gluLookAt() does
    public void lookThrough()
    {
        //rotate the pitch around the X axis
        GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //rotate the yaw around the Y axis
        GL11.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        GL11.glTranslatef(position.x, position.y, position.z);
    }
    
}
