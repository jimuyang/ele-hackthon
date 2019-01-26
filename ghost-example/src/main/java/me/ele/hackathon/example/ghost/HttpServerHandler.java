package me.ele.hackathon.example.ghost;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import me.ele.hackathon.pacman.ds.Coordinate;
import me.ele.hackathon.pacman.ds.Direction;
import me.ele.hackathon.pacman.ds.GameInfo;
import me.ele.hackathon.pacman.ds.GameState;
import me.ele.hackathon.pacman.engine.PlayerInput;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lanjiangang on 2018/12/23.
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);
    private GameInfo info;

    Direction getDir(int curX, int curY, Node nextPos) {
        if (nextPos.getX() > curX) {
            return Direction.RIGHT;
        } else if (nextPos.getX() < curX) {
            return Direction.LEFT;
        } else if (nextPos.getY() > curY) {
            return Direction.UP;
        } else {
            return Direction.DOWN;
        }
    }

    private Direction getDir(Coordinate coordinate, Node nextPos) {
        return getDir(coordinate.getX(), coordinate.getY(), nextPos);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        byte buf[] = new byte[msg.content().readableBytes()];
        msg.content().readBytes(buf);
        if (logger.isDebugEnabled()) {
            //logger.debug("http request {} {}, body {}", msg.getMethod(), msg.getUri(), new String(buf));
        }

        try {
            if (msg.getUri().contains("init")) {

                ObjectMapper objectMapper = new ObjectMapper();
                info = objectMapper.readValue(buf, GameInfo.class);
                logger.info("init game: {}", info);

                ByteBuf content = Unpooled.copiedBuffer("", CharsetUtil.UTF_8);
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
                response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/text");
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, content.readableBytes());
                ctx.write(response);

            } else if (msg.getUri().contains("state")) {
                ObjectMapper objectMapper = new ObjectMapper();
                GameState state = objectMapper.readValue(buf, GameState.class);
                //logger.info("game state: {}", state);
                state.getPacman().forEach((index, pos) -> logger.info("{} at {}", index, pos));

                Map<Integer, Direction> dirs = new LinkedHashMap<>();

                /**
                 * 针对每个pacman使用astar算法进行追逐
                 */
                state.getPacman().forEach((index, pos) -> {
                    if (state.getGhosts().containsKey(index)) {
                        AStar star = new AStar(info.getMap().getPixels(), info.getMap().getPixels().length, info.getMap().getPixels()[0].length);
                        List<Node> path = star
                                .search(state.getGhosts().get(index),
                                        state.getPacman().get(index));
                        if (path.size() > 1) {
                            Node nextPos = path.get(1);
                            logger.info("next pos of {} is {}, cur pos is ({})", index, nextPos, state.getGhosts().get(index));
                            dirs.put(index, getDir(state.getGhosts().get(index), nextPos));
                            logger.info("next dir of {} is {}", index, dirs.get(index));
                        }
                    }
                });

                PlayerInput input = new PlayerInput(dirs);
                String str = objectMapper.writeValueAsString(input);

                ByteBuf content = Unpooled.copiedBuffer(str, CharsetUtil.UTF_8);
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
                response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/text");
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, content.readableBytes());
                ctx.write(response);
            }
        } catch (Exception e) {
            logger.error("", e);
            ByteBuf content = Unpooled.copiedBuffer("", CharsetUtil.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, content);
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/text");
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, content.readableBytes());
            ctx.write(response);

        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
