package me.ele.hackathon.example.pacman;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import me.ele.hackathon.pacman.ds.GameInfo;
import me.ele.hackathon.pacman.ds.GameState;
import me.ele.hackathon.pacman.engine.PlayerInput;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lanjiangang on 2018/12/23.
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg)  {
        byte buf[] = new byte[msg.content().readableBytes()];
        msg.content().readBytes(buf);
        if (logger.isDebugEnabled())
            logger.debug("http request {} {}, body {}", msg.getMethod(), msg.getUri(), new String(buf));
        try {
            if (msg.getUri().contains("init")) {

                ObjectMapper objectMapper = new ObjectMapper();
                GameInfo info = objectMapper.readValue(buf, GameInfo.class);
                logger.info("init game: {}", info);

                ByteBuf content = Unpooled.copiedBuffer("", CharsetUtil.UTF_8);
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
                response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/text");
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, content.readableBytes());
                ctx.write(response);

            } else if (msg.getUri().contains("state")) {
                ObjectMapper objectMapper = new ObjectMapper();
                GameState state = objectMapper.readValue(buf, GameState.class);
                logger.info("game state: {}", state);

                //Thread.sleep(2000);

                PlayerInput input = new PlayerInput();
                String str = objectMapper.writeValueAsString(input);

                ByteBuf content = Unpooled.copiedBuffer(str, CharsetUtil.UTF_8);
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
                response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/text");
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, content.readableBytes());
                ctx.write(response);
            }
        }catch (Exception e) {
            logger.error("", e);
            ByteBuf content = Unpooled.copiedBuffer("", CharsetUtil.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, content);
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/text");
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, content.readableBytes());
            ctx.write(response);

        }
    }

        @Override public void channelReadComplete (ChannelHandlerContext ctx)throws Exception {
            ctx.flush();
        }
    }
