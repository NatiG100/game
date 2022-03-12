package org.lwjglb.engine.graph;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import	static	org.lwjgl.opengl.GL11.*;
import	static	org.lwjgl.opengl.GL15.*;
import	static	org.lwjgl.opengl.GL20.*;
import	static	org.lwjgl.opengl.GL30.*;

public class Mesh {
    private static final Vector3f DEFAULT_COLOR = new Vector3f(1.0f,1.0f,1.0f);

    private final int vaoId;

    private final List<Integer> vboIdList;

    private final int vertexCount;

    private Texture texture;
    private Vector3f color;

    public Mesh(float[] positions, float[] textCoords,float[] normals, int[] indices){
        FloatBuffer posBuffer = null;
        FloatBuffer texCoordsBuffer = null;
        FloatBuffer vecNormalsBuffer = null;
        IntBuffer indicesBuffer = null;
        try{
            color = Mesh.DEFAULT_COLOR;
            vertexCount =indices.length;
            vboIdList = new ArrayList<>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            //position vbo
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);

            //Texture cordinates VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            texCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            texCoordsBuffer.put(textCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER,vboId);
            glBufferData(GL_ARRAY_BUFFER,texCoordsBuffer,GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1,2,GL_FLOAT,false,0,0);


            //Vertex normals vbo
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
            vecNormalsBuffer.put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2,3, GL_FLOAT, false,0,0);

            //index vbo
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER,indicesBuffer,GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER,0);
            glBindVertexArray(0);

        }finally {
            if(posBuffer!=null){
                MemoryUtil.memFree(posBuffer);
            }if(texCoordsBuffer!=null){
                MemoryUtil.memFree(texCoordsBuffer);
            }
            if(vecNormalsBuffer!=null){
                MemoryUtil.memFree(vecNormalsBuffer);
            }
            if(indicesBuffer!=null){
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }
    public boolean isTextured(){
        return this.texture!=null;
    }
    public Texture getTexture(){
        return this.texture;
    }
    public void setTexture(Texture texture){
        this.texture = texture;
    }
    public void setColor(Vector3f color){
        this.color = color;
    }
    public Vector3f getColor(){
        return this.color;
    }

    public int getVaoId(){
        return vaoId;
    }
    public int getVertexCount(){
        return vertexCount;
    }

    public void render(){
        if(texture !=null){
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }
        glBindVertexArray(getVaoId());
        glDrawElements(GL_TRIANGLES,getVertexCount(),GL_UNSIGNED_INT,0);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D,0);
    }
    public void cleanUp(){
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER,0);
        for(int vboId: vboIdList){
            glDeleteBuffers(vboId);
        }
        if(texture!=null){
            texture.cleanup();
        }
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}
