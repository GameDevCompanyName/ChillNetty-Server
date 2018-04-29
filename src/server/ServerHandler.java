package server;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;

import java.io.UnsupportedEncodingException;

public class ServerHandler extends SimpleChannelHandler {

    private static String className = "ServerHandler";

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

        //TODO сделать так чтобы всё работало с пакетами типа чтобы сообщение не разрывалось на несколько частей как оно скорее всего и будет

        String message = getStringFromBuffer((ChannelBuffer) e.getMessage());
        Logger.log("Получено сообщение: " + message, className);

        String[] parsedMessages = message.split("/d/");
        for (String string: parsedMessages) {
            if (!string.equals(""))
                ServerMessage.read(string, e.getChannel());
        }

    }

    private String getStringFromBuffer(ChannelBuffer buffer) throws UnsupportedEncodingException {
        int bufSize = buffer.readableBytes();
        byte[] byteBuffer = new byte[bufSize];
        buffer.readBytes(byteBuffer);
        return new String(byteBuffer, "UTF-8");
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelClosed(ctx, e);
        Logger.log("Канал закрылся", className);
        ServerMethods.disconnectReceived(e.getChannel());
    }

    @Override
    public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e) throws Exception {
        super.writeComplete(ctx, e);
    }

    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        //  /d/ - выступает в качестве разделителя между сообщениями
        String message = (String) e.getMessage() + "/d/";
        Channels.write(
                ctx,
                e.getFuture(),
                ChannelBuffers.wrappedBuffer(message.getBytes("UTF-8")),
                e.getRemoteAddress()
        );
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        Logger.log("Словил Exception", className);
        e.getCause().printStackTrace();
        //TODO УДАЛЕНИЕ ОТОВСЮДУ
        ServerMethods.disconnectException(e.getChannel());
        e.getChannel().close();
    }
}
