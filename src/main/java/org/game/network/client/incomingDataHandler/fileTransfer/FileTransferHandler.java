package org.game.network.client.incomingDataHandler.fileTransfer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileTransferHandler extends SimpleChannelInboundHandler<PngTransferModel> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PngTransferModel msg) throws Exception {
        System.out.println("Client received PngTransferModel: " + msg);
        BufferedImage image = ImageMapper.toBufferedImage(msg.getImageBase64());
        String label = msg.getLabel();
        long rotation = 0L;
        if (!label.endsWith("D")) {
            rotation = Long.parseLong(label.substring(label.length() - 1));
        }

        String path = label + ".png";
        saveImage(image, path, "baseLabel", rotation);
    }

    private static void saveImage(BufferedImage image, String path, String entityLabel, Long rotation) {
        try {
            Path imageDir = Paths.get("data/textures");
            if (!Files.exists(imageDir)) {
                Files.createDirectories(imageDir);
            }
            File outputFile = imageDir.resolve(path).toFile();
            // TODO: 6/14/2025 publishGlContext potrzebne do załadowania tekstury
            //EventPublisher.getInstance().publishGlContext(new LoadTextureEvent(outputFile.getPath(), entityLabel, rotation));
            ImageIO.write(image, "png", outputFile);

            System.out.println("image saved");
        } catch (IOException e) {
            System.out.println("Save Image Failed");
            throw new RuntimeException(e);
        }
    }
}
