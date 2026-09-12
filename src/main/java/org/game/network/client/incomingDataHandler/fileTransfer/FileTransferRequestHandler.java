package org.game.network.client.incomingDataHandler.fileTransfer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class FileTransferRequestHandler extends SimpleChannelInboundHandler<FileTransferRequestModel> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FileTransferRequestModel msg) throws Exception {
        System.out.println("FILE_TRANSFER" + msg.getLabel());
    }
}
