package org.lwjglb.game;

import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.ShaderProgram;
import org.lwjglb.engine.Utils;

import java.nio.FloatBuffer;


import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Renderer {
    private static final float FOV  = (float)Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private Matrix4f projectionMatrix;
    private ShaderProgram shaderProgram;
    public Renderer(){
    }
    public void init(Window window) throws Exception{
        //create the shader
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("C:\\Users\\Nati\\IdeaProjects\\game\\src\\resources\\vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("C:\\Users\\Nati\\IdeaProjects\\game\\src\\resources\\fragment.fs"));
        shaderProgram.link();

        //create a projection matrix
        float aspectRatio = (float) window.getWidth()/window.getHeight();
        projectionMatrix = new Matrix4f().setPerspective(Renderer.FOV,aspectRatio,Renderer.Z_NEAR,Renderer.Z_FAR);
        shaderProgram.createUniform("projectionMatrix");
    }
    public void clear(){
        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
    }
    public void render(Window window, Mesh mesh){
        clear();
        if(window.isResized()){
            glViewport(0,0,window.getWidth(),window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();
        shaderProgram.setUniform("projectionMatrix",projectionMatrix);

        glBindVertexArray(mesh.getVaoId());
        glDrawElements(GL_TRIANGLES,mesh.getVertexCount(),GL_UNSIGNED_INT,0);

        glBindVertexArray(0);
        shaderProgram.unbind();
    }
    public void cleanup(){
        if(shaderProgram!=null){
            shaderProgram.cleanup();
        }
    }
}
