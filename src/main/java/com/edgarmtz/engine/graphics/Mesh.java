package com.edgarmtz.engine.graphics;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

public class Mesh {

    private int positionVboId;
    private int indexVboId;
    private int colorVboId;
    private int vaoId;
    private int vertexCount;
    private final Texture texture;

    public Mesh(float[] positions, float[]textureCoord, int[]indices, Texture texture){
        FloatBuffer verticesBuffer = null;
        FloatBuffer textureCoordBuffer = null;
        IntBuffer indexBuffer = null;
        try{
            this.texture = texture;
            vertexCount = indices.length;

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            //Positions
            positionVboId = glGenBuffers();
            verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
            verticesBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, positionVboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            //Color
            colorVboId = glGenBuffers();
            textureCoordBuffer = MemoryUtil.memAllocFloat(textureCoord.length);
            textureCoordBuffer.put(textureCoord).flip();
            glBindBuffer(GL_ARRAY_BUFFER, colorVboId);
            glBufferData(GL_ARRAY_BUFFER, textureCoordBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            //Index
            indexVboId = glGenBuffers();
            indexBuffer = MemoryUtil.memAllocInt(indices.length);
            indexBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if(verticesBuffer != null)
                MemoryUtil.memFree(verticesBuffer);
            if(textureCoordBuffer != null)
                MemoryUtil.memFree(textureCoordBuffer);
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
        glDeleteBuffers(positionVboId);
        glDeleteBuffers(indexVboId);
        glDeleteBuffers(colorVboId);

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

        //Dibujar vertices
        glDrawElements(GL_TRIANGLES,  this.getVertexCount(), GL_UNSIGNED_INT, 0);

        //recuperar estado
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

    }
}
