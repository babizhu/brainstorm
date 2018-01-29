package com.bbz.education.brainstorm.netty

import com.bbz.education.brainstorm.WebSocketChannelInitializer
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel

class BrainStormServer {

}

fun main(args: Array<String>) {
    val bossGroup = NioEventLoopGroup()
    val workerGroup = NioEventLoopGroup()


    try {
        val serverBootstrap = ServerBootstrap()
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java)
//                .handler(LoggingHandler(LogLevel.INFO))
                .childHandler(WebSocketChannelInitializer())

        val channelFuture = serverBootstrap.bind(8989).sync()
        channelFuture.channel().closeFuture().sync()
    } finally {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}