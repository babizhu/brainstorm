package com.bbz.education.brainstorm.network.netty

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel

class BrainStormServer {

    fun start(){
        val bossGroup = NioEventLoopGroup()
        val workerGroup = NioEventLoopGroup()

        try {
            val serverBootstrap = ServerBootstrap()
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel::class.java)
//                .handler(LoggingHandler(LogLevel.INFO))
                    .childHandler(BrainStormChannelInitializer())

            val channelFuture = serverBootstrap.bind(8989).sync()
            channelFuture.channel().closeFuture().sync()
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
