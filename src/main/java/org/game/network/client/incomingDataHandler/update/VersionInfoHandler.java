package org.game.network.client.incomingDataHandler.update;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.game.isometric.WorldSettings;
import org.game.network.client.model.DownloadUpdateModel;
import org.game.network.client.model.VersionInfoModel;
import org.game.network.updater.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionInfoHandler extends SimpleChannelInboundHandler<VersionInfoModel> {
    private static final Logger log = LoggerFactory.getLogger(VersionInfoHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, VersionInfoModel msg) throws Exception {
        log.info(msg.toString());
        WorldSettings.WORLD_SIZE = msg.getWorldSize();

        UpdateService service = new UpdateService();
        if (!service.isUpToDate(msg.getMajor(), msg.getMinor(), msg.getRelease())) {
            //service.updateVersion(msg.getMajor(), msg.getMinor(), msg.getRelease());
            //send download request
            System.out.println("VersionUpdateHandler send");
            ctx.writeAndFlush(new DownloadUpdateModel());
        }

    }
}
