package org.lwjglb.game;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.IGameLogic;
import org.lwjglb.engine.MouseInput;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.graph.Camera;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.OBJLoader;
import org.lwjglb.engine.graph.Texture;

public class DummyGame implements IGameLogic{
    private static final float MOUSE_SENSETIVITY = 0.2f;
    //check git
    private final Vector3f cameraInc;

    private final Renderer renderer;
    private final Camera camera;
    private GameItem[] gameItems;
    private static final float CAMERA_POS_STEP = 0.05f;
    public DummyGame(){
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f();
    }

    @Override
    public void init(Window window) throws Exception{
        renderer.init(window);
        Mesh mesh = OBJLoader.loadMesh("C:\\Users\\Nati\\IdeaProjects\\game\\src\\resources\\models\\cube.obj");
        System.out.println(mesh.toString());
        Texture texture = new Texture("textures/grassblock.png");
        mesh.setTexture(texture);
        GameItem gameItem = new GameItem(mesh);
        gameItem.setScale(0.5f);
        gameItem.setPosition(0,0,-2);
        gameItems = new GameItem[]{gameItem};
    }

    @Override
    public void input(Window window,MouseInput mouseInput){
        cameraInc.set(0,0,0);
        if(window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        }else if(window.isKeyPressed(GLFW_KEY_S)){
            cameraInc.z = 1;
        }
        if(window.isKeyPressed(GLFW_KEY_A)){
            cameraInc.x = -1;
        }else if(window.isKeyPressed(GLFW_KEY_D)){
            cameraInc.x = 1;
        }
        if(window.isKeyPressed(GLFW_KEY_Z)){
            cameraInc.y = -1;
        }else if(window.isKeyPressed((GLFW_KEY_X))){
            cameraInc.y = 1;
        }

    }

    @Override
    public void update(float interval, MouseInput mouseInput){
        camera.movePosition(
                cameraInc.x*CAMERA_POS_STEP,
                cameraInc.y*CAMERA_POS_STEP,
                cameraInc.z*CAMERA_POS_STEP
        );
        if(mouseInput.isLeftButtonPressed()){
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(
                    rotVec.x*MOUSE_SENSETIVITY,
                    rotVec.y*MOUSE_SENSETIVITY,
                    0
            );
        }

    }
    @Override
    public void render(Window window){
        renderer.render(window,camera,gameItems);
    }
    @Override
    public void cleanup(){
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}
