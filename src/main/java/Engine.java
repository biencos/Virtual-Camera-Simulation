import managers.FileManager;
import models.Camera;
import models.Line;
import models.View;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Hashtable;


public class Engine extends JFrame implements KeyListener {
    public Hashtable<Integer, Boolean> pressedKeys;

    // OPTIONS
    static final String APP_NAME = "Virtual Camera";
    static final int SCREEN_WIDTH = 900, SCREEN_HEIGHT = 700, TIME_DELAY = 5;
    static final int MOVEMENT_VELOCITY = 2, ROTATION_VELOCITY = 1, ZOOM_ON_START = 410, ZOOM_SENSITIVITY = 5;


    public Engine() {
        pressedKeys = new Hashtable<>();

        this.setVisible(true);
        this.setTitle(APP_NAME);
        this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        addKeyListener(this);
    }


    public void start(String fileName) {
        View view = new View();
        FileManager fileManager = new FileManager();
        ArrayList<Line> lines = fileManager.readJSONFile(fileName);
        view.setLines(lines);

        Camera camera = new Camera(view, ZOOM_ON_START);
        Canvas canvas = new Canvas();
        Engine.KeyManager keyManager = new Engine.KeyManager(MOVEMENT_VELOCITY, ROTATION_VELOCITY, ZOOM_SENSITIVITY, ZOOM_SENSITIVITY);
        Timer handleKeysTimer = new Timer(TIME_DELAY, actionEvent -> keyManager.handleKeys(this, camera, canvas));
        getContentPane().add(canvas);
        paintCanvas(canvas, camera);
        handleKeysTimer.start();
    }

    public static void paintCanvas(Canvas canvas, Camera camera) {
        ArrayList<Line> updatedLines = camera.project();
        canvas.setLines(updatedLines);
        canvas.paint(canvas.getGraphics());
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        this.pressedKeys.put(keyEvent.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        this.pressedKeys.remove(keyEvent.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    static class KeyManager {
        public int movementVelocity, rotationVelocity, xFocalSensitivity, yFocalSensitivity;

        public KeyManager(int movementVelocity, int rotationVelocity, int xFocalSensitivity, int yFocalSensitivity) {
            this.movementVelocity = movementVelocity;
            this.rotationVelocity = rotationVelocity;
            this.xFocalSensitivity = xFocalSensitivity;
            this.yFocalSensitivity = yFocalSensitivity;
        }

        public void handleKeys(Engine engine, Camera camera, Canvas canvas) {
            for (Integer code : engine.pressedKeys.keySet()) {
                switch (code) {
                    case KeyEvent.VK_A:
                        camera.move(movementVelocity, 0, 0);
                        break;
                    case KeyEvent.VK_D:
                        camera.move(-movementVelocity, 0, 0);
                        break;
                    case KeyEvent.VK_SPACE:
                        camera.move(0, movementVelocity, 0);
                        break;
                    case KeyEvent.VK_CONTROL:
                        camera.move(0, -movementVelocity, 0);
                        break;
                    case KeyEvent.VK_S:
                        camera.move(0, 0, movementVelocity);
                        break;
                    case KeyEvent.VK_W:
                        camera.move(0, 0, -movementVelocity);
                        break;

                    case KeyEvent.VK_DOWN:
                        camera.rotate(rotationVelocity, 0, 0);
                        break;
                    case KeyEvent.VK_UP:
                        camera.rotate(-rotationVelocity, 0, 0);
                        break;
                    case KeyEvent.VK_LEFT:
                        camera.rotate(0, rotationVelocity, 0);
                        break;
                    case KeyEvent.VK_RIGHT:
                        camera.rotate(0, -rotationVelocity, 0);
                        break;
                    case KeyEvent.VK_Q:
                        camera.rotate(0, 0, rotationVelocity);
                        break;
                    case KeyEvent.VK_E:
                        camera.rotate(0, 0, -rotationVelocity);
                        break;

                    case KeyEvent.VK_Z:
                        camera.zoom(xFocalSensitivity, yFocalSensitivity);
                        break;
                    case KeyEvent.VK_X:
                        camera.zoom(-xFocalSensitivity, -yFocalSensitivity);
                        break;
                }
            }
            if (engine.pressedKeys.size() > 0)
                repaintCanvas(canvas, camera);
        }

        private void repaintCanvas(Canvas canvas, Camera camera) {
            ArrayList<Line> updatedLines = camera.project();
            canvas.setLines(updatedLines);
            canvas.repaint();
        }
    }
}
