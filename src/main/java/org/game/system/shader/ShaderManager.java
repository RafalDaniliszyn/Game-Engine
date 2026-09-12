package org.game.system.shader;

import org.game.isometric.shader.OrthoShader;
import org.game.ui.system.UiShader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShaderManager {
    private final Map<ShaderEnum, ShaderProgram> shaderProgramMap;

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
        DefaultShader defaultShader = new DefaultShader(
                "/vertex.glsl",
                "/alphaFragment.glsl");
        defaultShader.create();
        shaderProgramMap.put(ShaderEnum.DEFAULT, defaultShader);

        WindShader windShader = new WindShader(
                "/vertexWind.glsl",
                "/fragmentWind.glsl");
        windShader.create();
        shaderProgramMap.put(ShaderEnum.WIND, windShader);

        WaterShader waterShader = new WaterShader(
                "/vertexWater.glsl",
                "/fragmentWater.glsl");
        waterShader.create();
        shaderProgramMap.put(ShaderEnum.WATER, waterShader);

        UiShader uiShader = new UiShader(
                "/vertexUI.glsl",
                "/fragmentUI.glsl");
        uiShader.create();
        shaderProgramMap.put(ShaderEnum.UI, uiShader);

        OrthoShader orthoShader = new OrthoShader(
                "/vertexOrtho.glsl",
                "/fragmentOrtho.glsl");
        orthoShader.create();
        shaderProgramMap.put(ShaderEnum.ORTHO, orthoShader);

        OrthoShader finalShader = new OrthoShader(
                "/finalVertexOrtho.glsl",
                "/finalFragmentOrtho.glsl");
        finalShader.create();
        shaderProgramMap.put(ShaderEnum.FINAL_ORTHO, finalShader);

        OrthoShader lightOrthoShader = new OrthoShader(
                "/lightVertexOrtho.glsl",
                "/lightFragmentOrtho.glsl");
        lightOrthoShader.create();
        shaderProgramMap.put(ShaderEnum.LIGHT_ORTHO, lightOrthoShader);

        OrthoShader windShader2D = new OrthoShader(
                "/vertexOrthoWind.glsl",
                "/fragmentOrtho.glsl");
        windShader2D.create();
        shaderProgramMap.put(ShaderEnum.WIND_ORTHO, windShader2D);
    }

}
