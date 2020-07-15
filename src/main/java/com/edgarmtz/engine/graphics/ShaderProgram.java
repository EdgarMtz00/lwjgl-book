package com.edgarmtz.engine.graphics;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;
    private final Map<String, Integer> uniforms;

    public ShaderProgram() throws Exception{
        uniforms = new HashMap<>();
        programId = glCreateProgram();
        if (programId == 0)
            throw new Exception("Couldn't create openGl program");
    }

    protected int createShader(String shaderCode, int shaderType) throws Exception{
        int shaderId = glCreateShader(shaderType);

        if(shaderId == 0)
            throw new Exception("Couldn't create shader" + shaderType);

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0)
            throw  new  Exception("Couldn't compile shader: " + shaderType);

        glAttachShader(programId, shaderId);
        return shaderId;
    }

    public void createVertexShader(String shaderCode) throws  Exception{
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception{
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    public  void link() throws  Exception{
        glLinkProgram(programId);

        if(glGetProgrami(programId, GL_LINK_STATUS) == 0)
            throw new Exception("Couldn't link program: " + glGetProgramInfoLog(programId, 1024));

        if(vertexShaderId != 0)
            glDetachShader(programId, vertexShaderId);

        if(fragmentShaderId != 0)
            glDetachShader(programId, fragmentShaderId);

        glValidateProgram(programId);
        if(glGetProgrami(programId, GL_VALIDATE_STATUS) == 0)
            throw new Exception("Warning validating program: " + glGetProgramInfoLog(programId, 1024));
    }

    public void bind(){
        glUseProgram(programId);
    }

    public void unbind(){
        glUseProgram(0);
    }

    public void cleanup(){
        unbind();
        if(programId != 0){
            glDeleteProgram(programId);
        }
    }

    public void createUniform(String name) throws Exception {
        int uniformLocation = glGetUniformLocation(programId, name);
        if(uniformLocation < 0)
            throw new Exception("Could not find uniform " + name);
        uniforms.put(name, uniformLocation);
    }

    public void setUniform(String name, Matrix4f value) {
        try(MemoryStack stack = MemoryStack.stackPush()){
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            glUniformMatrix4fv(uniforms.get(name), false, fb);
        }
    }

    public void setUniform(String name, int value){
        glUniform1i(uniforms.get(name), value);
    }
}