package org.game.debugWindow;

import org.game.isometric.texture2D.TextureEnum2D;
import org.game.isometric.texture2D.TextureManager2D;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import java.awt.event.ItemEvent;
import java.util.Map;
import java.util.Objects;

public class Frame extends JFrame {

    private static JComboBox<ComboModel> comboBox1;
    static {
        comboBox1 = new JComboBox<>();
        comboBox1.setBounds(100, 100, 400, 60);
    }

    public Frame() {
        Panel panel = new Panel();
        this.setSize(600, 600);
        this.setVisible(true);
        this.add(panel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        comboBox1.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                SelectedItem.id = ((ComboModel) Objects.requireNonNull(comboBox1.getSelectedItem())).id;
                SelectedItem.label = ((ComboModel) Objects.requireNonNull(comboBox1.getSelectedItem())).label;
                SelectedItem.image = ((ComboModel) Objects.requireNonNull(comboBox1.getSelectedItem())).image;
            }
        });
        panel.add(comboBox1);
    }

    public static void addTexturesToComboBox() {
        Map<TextureEnum2D, Integer> textures = TextureManager2D.getTextures();
        textures.forEach((textureEnum2D, id) -> {
            comboBox1.addItem(new ComboModel(textureEnum2D.getLabel(), id, textureEnum2D.getPath()));
        });
    }

    public static void addTextureToComboBox(ComboModel comboModel) {
        comboBox1.addItem(comboModel);
    }

}
