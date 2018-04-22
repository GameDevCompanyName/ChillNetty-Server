package server;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class ServerHandler extends SimpleChannelUpstreamHandler {

    private static String className = "ServerHandler";

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

        //TODO сделать так чтобы всё работало с пакетами типа чтобы сообщение не разрывалось на несколько частей как оно скорее всего и будет

        super.messageReceived(ctx, e);
        String message = (String) e.getMessage();
        Logger.log("Получено сообщение: " + message, className);
        ServerMessage.read(message, e.getChannel());

    }



//    private String getStringFromBuffer(ChannelBuffer channelBuffer) throws UnsupportedEncodingException {
//        int bufSize = channelBuffer.readableBytes();
//        byte[] byteBuffer = new byte[bufSize];
//        channelBuffer.readBytes(byteBuffer);
//        return new String(byteBuffer, "UTF-8");
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        super.exceptionCaught(ctx, e);
        Logger.log("Словил Exception", className);
        Logger.log(e.getCause().getMessage(), className);
        //TODO УДАЛЕНИЕ ОТОВСЮДУ
        e.getChannel().close();
    }
}
