package org.game.network.client.incomingDataHandler.fileTransfer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageMapper {

    public static BufferedImage toBufferedImage(String imageBase64) throws IOException {
        byte[] bytes = Base64.getDecoder().decode(imageBase64);
        return ImageIO.read(new ByteArrayInputStream(bytes));
    }
}
