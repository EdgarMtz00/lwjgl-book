package com.edgarmtz.engine.graphics;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

public class Mesh {
    private static final Vector3f DEFAULT_COLOR = new Vector3f(1.0f, 1.0f, 1.0f);

    private int vertexCount;
    private int vaoId;
    private List<Integer> vboIdList;
    private Vector3f color;
    private Texture texture;

    public Mesh(float[] positions, float[]textureCoord, float[] normals, int[]indices){
        int vboId;
        vboIdList = new ArrayList<>();

        FloatBuffer verticesBuffer = null;
        FloatBuffer textureCoordBuffer = null;
        FloatBuffer vecNormalsBuffer = null;
        IntBuffer indexBuffer = null;


        try{
            color = DEFAULT_COLOR;
            vertexCount = indices.length;

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            //Positions
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
            verticesBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            //Color
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            textureCoordBuffer = MemoryUtil.memAllocFloat(textureCoord.length);
            textureCoordBuffer.put(textureCoord).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textureCoordBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            // Normals VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
            vecNormalsBuffer.put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

            //Index
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            indexBuffer = MemoryUtil.memAllocInt(indices.length);
            indexBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if(verticesBuffer != null)
                MemoryUtil.memFree(verticesBuffer);
            if(textureCoordBuffer != null)
                MemoryUtil.memFree(textureCoordBuffer);
            if(vecNormalsBuffer != null)
                MemoryUtil.memFree(vecNormalsBuffer);
            if(indexBuffer != null)
                MemoryUtil.memFree(indexBuffer);
        }
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void cleanup(){
        glDisableVertexAttribArray(0);

        //delete VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId:vboIdList) {
            glDeleteBuffers(vboId);
        }

        if (isTextured())
            texture.cleanup();

        //delete VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public void render(){
        // Activate first texture unit
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, texture.getId());
        //bind al VAO
        glBindVertexArray(this.getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        //Dibujar vertices
        glDrawElements(GL_TRIANGLES,  this.getVertexCount(), GL_UNSIGNED_INT, 0);

        //recuperar estado
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean isTextured () {
        return this.texture != null;
    }
}
