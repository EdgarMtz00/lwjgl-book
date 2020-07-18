package com.edgarmtz.engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

/**
 * Manages all shaders programs loaded into gpu
 */
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

    /**
     * Loads a shader program code and assigns it an id
     * @param shaderCode Code from shader program
     * @param shaderType Indicates if program is a fragment shader or vertex shader
     * @return Shader Program associated id
     * @throws Exception if failed to assign an id or to compile shader code
     */
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

    /**
     * Use {@link #createShader(String, int)} to specifically create a vertex shader
     * @param shaderCode Code from shader program
     * @throws Exception if failed to assign an id or to compile shader code
     */
    public void createVertexShader(String shaderCode) throws  Exception{
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    /**
     * Use {@link #createShader(String, int)} to specifically create a fragment shader
     * @param shaderCode Code from shader program
     * @throws Exception if failed to assign an id or to compile shader code
     */
    public void createFragmentShader(String shaderCode) throws Exception{
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    /**
     * Links all programs with uniforms and validates them
     * @throws Exception if failed to link or linked an invalid program
     */
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

    /**
     * Installs the program as the current rendering program
     */
    public void bind(){
        glUseProgram(programId);
    }

    /**
     * Removes the current rendering program
     */
    public void unbind(){
        glUseProgram(0);
    }

    /**
     * Deletes any temporary data stored in systems memory
     */
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

    public void setUniform(String name, Vector3f value){
        glUniform3f(uniforms.get(name), value.x, value.y, value.z);
    }
}
