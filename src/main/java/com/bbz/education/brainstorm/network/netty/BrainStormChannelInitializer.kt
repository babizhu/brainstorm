package com.bbz.education.brainstorm.network.netty

import com.bbz.education.brainstorm.network.netty.logic.handler.LoginHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler
import io.netty.handler.stream.ChunkedWriteHandler

class BrainStormChannelInitializer : ChannelInitializer<SocketChannel>() {
    override fun initChannel(ch: SocketChannel) {


        val pipeline = ch.pipeline()

        pipeline.addLast("httpServerCodec", HttpServerCodec());
//ChunkedWriteHandler分块写处理，文件过大会将内存撑爆
        pipeline.addLast("chunkedWriteHandler", ChunkedWriteHandler());
        /**
         * 作用是将一个Http的消息组装成一个完成的HttpRequest或者HttpResponse，那么具体的是什么
         * 取决于是请求还是响应, 该Handler必须放在HttpServerCodec后的后面
         */
        pipeline.addLast("httpObjectAggregator", HttpObjectAggregator(8192));
        pipeline.addLast("webSocketServerProtocolHandler", WebSocketServerProtocolHandler("/chat"))
//        pipeline.addLast("test", TestHandler())
//        pipeline.addLast("logicHandler", BrainStormHandler())
        pipeline.addLast("loginHandler", LoginHandler())
    }
}