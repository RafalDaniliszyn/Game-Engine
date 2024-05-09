package org.game.system.shader;

import org.game.isometric.shader.OrthoShader;
import org.game.ui.system.UiShader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShaderManager {
    private Map<ShaderEnum, ShaderProgram> shaderProgramMap;

    public ShaderManager() {
        shaderProgramMap = new HashMap<>();
        loadShaders();
    }

    public void useShader(ShaderEnum shaderType) {
       shaderProgramMap.get(shaderType).use();
    }

    public ShaderProgram getShader(ShaderEnum shaderType) {
        return shaderProgramMap.get(shaderType);
    }

    public void remove() {
        List<ShaderProgram> shaderProgram = shaderProgramMap.values().stream().toList();
        for (ShaderProgram program : shaderProgram) {
            program.stop();
            program.delete();
        }
    }

    private void loadShaders() {
        // TODO: 5/9/2024 Change this test paths
        DefaultShader defaultShader = new DefaultShader(
                "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\vertex.glsl",
                "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\alphaFragment.glsl");
        defaultShader.create();
        shaderProgramMap.put(ShaderEnum.DEFAULT, defaultShader);

        WindShader windShader = new WindShader(
                "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\vertexWind.glsl",
                "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\fragmentWind.glsl");
        windShader.create();
        shaderProgramMap.put(ShaderEnum.WIND, windShader);

        WaterShader waterShader = new WaterShader(
                "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\vertexWater.glsl",
                "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\fragmentWater.glsl");
        waterShader.create();
        shaderProgramMap.put(ShaderEnum.WATER, waterShader);

        UiShader uiShader = new UiShader(
                "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\vertexUI.glsl",
                "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\fragmentUI.glsl");
        uiShader.create();
        shaderProgramMap.put(ShaderEnum.UI, uiShader);

        OrthoShader orthoShader = new OrthoShader(
                "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\vertexOrtho.glsl",
                "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\fragmentOrtho.glsl");
        orthoShader.create();
        shaderProgramMap.put(ShaderEnum.ORTHO, orthoShader);
    }

}
