package org.barneys.server.modelHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.barneys.blockLoader.BlocksReader;
import org.barneys.debug.Panel;
import org.barneys.server.ConfigManager;
import org.barneys.server.modelHandler.fileTransfer.ImageMapper;
import org.barneys.server.modelHandler.fileTransfer.PngTransferModel;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class VersionUpdateHandler extends SimpleChannelInboundHandler<DownloadUpdateModel> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ctx.writeAndFlush(new VersionInfoModel(1, 2, 5, ConfigManager.config.WORLD_SIZE));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DownloadUpdateModel msg) throws Exception {
        System.out.println("VersionUpdateHandler channelRead0(DownloadUpdateModel msg): version=" + msg.getMajor() + "." + msg.getMinor() + "." + msg.getRelease());
        // TODO: 6/14/2025 send new client version
        // block.json file and textures

        String json = getBlockJson();
        ctx.writeAndFlush(new JsonFileModel(json));


        //testowo textura
        String label = "UPDATE_TEST_2D";
        if (Panel.imageMap.containsKey(label)) {
            BufferedImage image = Panel.imageMap.get(label);
            PngTransferModel model = new PngTransferModel(ImageMapper.toBase64(image), label);
            ctx.writeAndFlush(model);
            ctx.fireUserEventTriggered(new VersionUpdateFinishedEvent());
        }
    }

    private String getBlockJson() {
        try (InputStream resourceAsStream = BlocksReader.class.getResourceAsStream("/blocks/block.json")) {
            if (resourceAsStream == null) {
                throw new RuntimeException("Not found: block.json");
            }
            return new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed: block.json", e);
        }
    }
}
