package server;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import static server.Constants.*;

public class Server {

    private static String className = "Server";

    public static void main(String[] args) {

        Logger.log("Сервер запускается", className);

        Logger.log("ChannelFactory...", className);
        ChannelFactory factory = new NioServerSocketChannelFactory(
                Executors.newFixedThreadPool(1),
                Executors.newFixedThreadPool(4)
        );
        Logger.log("ChannelFactory DONE", className);

        Logger.log("Bootstrap...", className);
        ServerBootstrap bootstrap = new ServerBootstrap(factory);
        bootstrap.setPipelineFactory(() -> Channels.pipeline(new ServerHandler()));
        Logger.log("Bootstrap DONE", className);

        Logger.log("Binding Channel...", className);
        Channel channel = bootstrap.bind(new InetSocketAddress(PORT));
        Logger.log("Binding Channel DONE", className);

    }

}
