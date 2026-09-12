package org.barneys.server.modelHandler.fileTransfer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.barneys.debug.Panel;
import java.awt.image.BufferedImage;

public class FileTransferRequestHandler extends SimpleChannelInboundHandler<FileTransferRequestModel> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FileTransferRequestModel msg) throws Exception {
        String label = msg.getLabel();
        if (Panel.imageMap.containsKey(label)) {
            BufferedImage image = Panel.imageMap.get(label);
            PngTransferModel model = new PngTransferModel(ImageMapper.toBase64(image), label);
            ctx.writeAndFlush(model);
        }
    }
}
