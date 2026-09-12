package org.game.network.client.incomingDataHandler.update;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.game.network.client.model.JsonFileModel;
import org.game.network.updater.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
public class JsonFileHandler extends SimpleChannelInboundHandler<JsonFileModel> {


    private static final Logger log = LoggerFactory.getLogger(JsonFileHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JsonFileModel msg) throws Exception {
        System.out.println(msg.getJsonFile());
        Path propertiesDir = Paths.get("data/properties");
        Path properties = propertiesDir.resolve("block.json");

        FileUtils.initFiles("data/properties", "block.json", null);

        List<String> lines = msg.getJsonFile().lines().toList();
        Path path = Files.write(properties, lines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        log.info("saved file path: {}", path);
    }
}
