package com.edgarmtz.engine.entities;

import org.joml.Vector3f;

/**
 * Calculates its position and rotation based on player movement so it can be used to move all objects to give the illusion of a moving camera
 */
public class Camera {
    private final Vector3f position;
    private final Vector3f rotation;

    /**
     * Initialize at center with no rotation
     */
    public Camera() {
        position = new Vector3f();
        rotation = new Vector3f();
    }

    /**
     * Initialize at a given position and rotation
     * @param position Camera starting position
     * @param rotation Camera starting rotation
     */
    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z){
        position.x = x;
        position.y = y;
        position.z = z;
    }

    /**
     * Change camera position according to offsets in each axis
     * @param offsetX Amount of movement in x axis
     * @param offsetY Amount of movement in y axis
     * @param offsetZ Amount of movement in z axis
     */
    public void movePosition(float offsetX, float offsetY, float offsetZ){
        if(offsetX != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }

        if(offsetZ != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }

        position.y += offsetY;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z){
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    /**
     * Change camera rotation according to offsets in each axis
     * @param offsetX Amount of rotation in x axis
     * @param offsetY Amount of rotation in y axis
     * @param offsetZ Amount of rotation in z axis
     */
    public void moveRotation(float offsetX, float offsetY, float offsetZ){
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }
}
