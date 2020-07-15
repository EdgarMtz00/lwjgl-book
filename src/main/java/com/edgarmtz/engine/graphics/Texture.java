package com.edgarmtz.engine.graphics;

import com.edgarmtz.engine.utils.Resources;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.ARBInternalformatQuery2.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.*;

public class Texture {
    private int id;

    private Texture(int id){
        this.id = id;
    }

    public Texture(String  TextureName) throws Exception{
        this(loadTexture(TextureName));
    }

    public static int loadTexture(String textureName) throws Exception{
        // Get texture Image
        PNGDecoder pngDecoder = new PNGDecoder(Resources.loadResource(textureName));
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * pngDecoder.getHeight() * pngDecoder.getWidth());
        pngDecoder.decode(buffer, pngDecoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        buffer.flip();

        //Load texture into graphic card
        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, pngDecoder.getWidth(), pngDecoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        return textureId;
    }

    public int getId() {
        return id;
    }

    public void cleanup() {
        glDeleteTextures(id);
    }
}
