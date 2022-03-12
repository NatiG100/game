package org.lwjglb.game;

import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.graph.Camera;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.ShaderProgram;
import org.lwjglb.engine.Utils;

import java.nio.FloatBuffer;


import org.lwjgl.system.MemoryUtil;
import org.lwjglb.engine.graph.Transformation;

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
    private final Transformation transformation;
    private ShaderProgram shaderProgram;
    public Renderer(){
        transformation = new Transformation();
    }
    public void init(Window window) throws Exception{
        //create the shader
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("C:\\Users\\Nati\\IdeaProjects\\game\\src\\resources\\vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("C:\\Users\\Nati\\IdeaProjects\\game\\src\\resources\\fragment.fs"));
        shaderProgram.link();

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");
//        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        shaderProgram.createUniform("colour");
        shaderProgram.createUniform("useColour");
    }
    public void clear(){
        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
    }
    public void render(Window window, Camera camera, GameItem[] gameItems){
        clear();
        if(window.isResized()){
            glViewport(0,0,window.getWidth(),window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV,window.getWidth(),window.getHeight(),Z_NEAR,Z_FAR);
        shaderProgram.setUniform("projectionMatrix" ,projectionMatrix);
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);
        shaderProgram.setUniform("texture_sampler", 0);
        for(GameItem gameItem:gameItems){
            Mesh mesh = gameItem.getMesh();
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem,viewMatrix);
            shaderProgram.setUniform("modelViewMatrix",modelViewMatrix);

            shaderProgram.setUniform("colour",mesh.getColor());
            shaderProgram.setUniform("useColour",mesh.isTextured()?0:1);
            gameItem.getMesh().render();
        }
        shaderProgram.unbind();
    }
    public void cleanup(){
        if(shaderProgram!=null){
            shaderProgram.cleanup();
        }
    }
}
