package com.edgarmtz.engine.physics;

import com.edgarmtz.engine.entities.Camera;
import com.edgarmtz.engine.entities.GameObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
    private final Matrix4f projectionMatrix;
    private final Matrix4f modelViewMatrix;
    private final Matrix4f viewMatrix;

    public Transformation(){
        modelViewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
    }

    public Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }

    public Matrix4f getModelViewMatrix(GameObject gameObject, Matrix4f viewMatrix) {
        Vector3f rotation = gameObject.getRotation();
        modelViewMatrix.identity().translate(gameObject.getPosition()).
                rotateX((float)Math.toRadians(-rotation.x)).
                rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).
                scale(gameObject.getScale());
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(modelViewMatrix);
    }

    public Matrix4f getViewMatrix(Camera camera){
        Vector3f cameraPosition = camera.getPosition();
        Vector3f cameraRotation = camera.getRotation();
        viewMatrix.identity().
                rotate((float)Math.toRadians(cameraRotation.x), new Vector3f(1, 0, 0)).
                rotate((float)Math.toRadians(cameraRotation.y), new Vector3f(0, 1, 0)).
                translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
        return viewMatrix;
    }
}
