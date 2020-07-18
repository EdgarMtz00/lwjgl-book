package com.edgarmtz.engine.entities;

import com.edgarmtz.engine.graphics.Mesh;
import org.joml.Vector3f;

/**
 * Represents any object in the game with it's model, position and size
 */
public class GameObject {
    private final Mesh mesh;
    private  final Vector3f position;
    private float scale;
    private  final Vector3f rotation;

    /**
     * Associates object with it's model and sets a default position and size
     * @param mesh Model's mesh
     */
    public GameObject(Mesh mesh){
        this.mesh = mesh;
        position = new Vector3f(0,0,0);
        scale = 1;
        rotation = new Vector3f(0,0,0);
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z){
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z){
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    /**
     * Deletes any temporary data stored in systems memory
     */
    public void cleanup(){
        mesh.cleanup();
    }
}
