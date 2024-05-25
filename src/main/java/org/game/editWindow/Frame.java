package org.game.editWindow;

import org.game.isometric.texture2D.TextureEnum2D;
import org.game.isometric.texture2D.TextureManager2D;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.util.Map;
import java.util.Objects;

public class Frame extends JFrame {

    private static JComboBox type;
    private static JComboBox components;
    private static JTextField label;
    private static JTextField quantity;
    private static JButton okButton;
    private static JCheckBox stackable;
    private static JCheckBox replaceableEdges;

    private static JComboBox<ComboModel> comboBox1;
    static {
        comboBox1 = new JComboBox<>();
        type = new JComboBox(new String[]{"terrain", "item"});
        components = new JComboBox(new String[]{"MeshComponent2D", "CollisionComponent2D"});
        okButton = new JButton("Create");
        stackable = new JCheckBox("Stackable");
        replaceableEdges = new JCheckBox("Replaceable edges");
        label = new JTextField();
        quantity = new JTextField();
    }

    public Frame() {
        Panel panel = new Panel();
        this.setSize(600, 600);
        this.setVisible(true);
        this.add(panel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        GridLayout gridLayout = new GridLayout(15, 3);
        Container container = new Container();
        gridLayout.layoutContainer(container);
        gridLayout.preferredLayoutSize(container);
        panel.setLayout(gridLayout);

        JLabel spacer = new JLabel();
        spacer.setPreferredSize(new Dimension(50, 300));
        JLabel spacer1 = new JLabel();
        spacer1.setPreferredSize(new Dimension(50, 300));
        JLabel spacer2 = new JLabel();
        spacer2.setPreferredSize(new Dimension(50, 300));
        panel.add(spacer);
        panel.add(spacer1);
        panel.add(spacer2);

        comboBox1.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                SelectedItem.id = ((ComboModel) Objects.requireNonNull(comboBox1.getSelectedItem())).id;
                SelectedItem.label = ((ComboModel) Objects.requireNonNull(comboBox1.getSelectedItem())).label;
                SelectedItem.image = ((ComboModel) Objects.requireNonNull(comboBox1.getSelectedItem())).image;
            }
        });
        panel.add(comboBox1);

        okButton.addActionListener(e -> {
            System.out.println(type.getSelectedItem());
            System.out.println(stackable.isSelected());
            System.out.println(label.getText());
        });
        panel.add(type);
        panel.add(components);
        panel.add(stackable);
        panel.add(replaceableEdges);
        panel.add(label);
        panel.add(quantity);
        panel.add(okButton);
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
