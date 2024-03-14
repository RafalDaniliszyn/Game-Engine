package org.game.entity;

import org.game.system.renderer.ShaderEnum;

public class EntityProperties {

    private ShaderEnum shaderType;

    public EntityProperties() {
        this.shaderType = ShaderEnum.DEFAULT;
    }

    public EntityProperties(ShaderEnum shaderType) {
        this.shaderType = shaderType;
    }

    public ShaderEnum getShaderType() {
        return shaderType;
    }

    public void setShaderType(ShaderEnum shaderType) {
        this.shaderType = shaderType;
    }
}
