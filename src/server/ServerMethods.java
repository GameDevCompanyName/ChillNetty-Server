package server;

/*
Класс с методами, вызываемыми клиентами.
 */

import org.jboss.netty.channel.Channel;
import sun.rmi.runtime.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static server.Constants.VERSION_ANDROID;
import static server.Constants.VERSION_DESKTOP;

public class ServerMethods {

    private static String className = "ServerMethods";

    public static void versionReceived(Channel userChannel, String version) {

        Logger.log("Проверяю версию клиента", className);
        if (version.equals(VERSION_ANDROID) || version.equals(VERSION_DESKTOP)){
            Logger.log("Версия корректна", className);
            sendValidVersionMessage(userChannel);
        } else {
            Logger.log("Несовместимая версия", className);
            sendInvalidVerisonMessage(userChannel);
        }

    }

    public static void loginAttemptReceived(Channel userChannel, String login, String password) {

        Logger.log("Обрабатываю попытку залогиниться по логину: " + login, className);

        boolean userOnline = Broadcaster.checkIfUserOnline(login);

        if (userOnline){
            Logger.log("Юзер с таким именем уже онлайн: " + login, className);
            sendUserAlreadyOnlineMessage(userChannel);
            return;
        }

        boolean userExists = DBConnector.searchForUser(login);

        if (userExists){
            Logger.log("Такой пользователь уже есть в базе, проверяю пароль для: " + login, className);
            boolean passwordIsCorrect = DBConnector.checkLoginAttempt(login, password);
            if (passwordIsCorrect){
                Logger.log("Верный пароль для: " + login, className);
                User newUser = initUser(userChannel, login);
                Broadcaster.userLoggedIn(newUser, userChannel);
                sendUserLoginSuccessMessage(userChannel);
            } else {
                Logger.log("Неверный пароль для: " + login, className);
                sendWrongPasswordMessage(userChannel);
            }
        }

        if (!userExists){
            Logger.log("Такого пользователя в базе ещё нет, создаю нового для: " + login, className);
            DBConnector.insertNewUser(login, password);
            Logger.log("Пользователь создан: " + login, className);
            User newUser = initUser(userChannel, login);
            Broadcaster.userLoggedIn(newUser, userChannel);
            sendUserLoginSuccessMessage(userChannel);
        }

    }

    public static void messageReceived(Channel userChannel, String text) {

        Logger.log("Проверяю залогинен ли канал, пытающийся отправить сообщение", className);

        boolean userIsLogged = Broadcaster.checkIfChannelLogged(userChannel);

        if (userIsLogged){
            Logger.log("Канал залогинен, передаю сообщение в Broadcaster", className);
            Broadcaster.messageRecieved(userChannel, text);
        } else {
            Logger.log("Канал НЕ залогинен и не может отправлять сообщения", className);
        }

    }

    public static void disconnectReceived(Channel userChannel) {

        Logger.log("Канал отключается", className);
        Broadcaster.userDisconnected(userChannel);
        disconnectChannel(userChannel);
        Logger.log("Сообщение об отключении обработано", className);

    }

    public static void joinRoomReceived(Channel userChannel, String roomId) {

        Logger.log("Проверяю залогинен ли канал, пытающийся сменить комнату", className);

        boolean userIsLogged = Broadcaster.checkIfChannelLogged(userChannel);

        if (userIsLogged){
            Logger.log("Канал залогинен, передаю запрос в Broadcaster", className);
            Broadcaster.roomChangeRequestRecieved(userChannel, roomId);
        } else {
            Logger.log("Канал НЕ залогинен и не может сменить комнату", className);
        }

    }

    public static void pingReceived(Channel userChannel) {

        sendPongAnswer(userChannel);

    }


    //Все методы в документации
//    public static String versionReceived(String version){
//        String result = "false";
//        switch (version){
//            case "JC0.1":
//                result = version;
//                break;
//            case "AC0.1":
//                result = version;
//                break;
//            default:
//                result="false";
//                break;
//        }
//        return result;
//    }
//
//    public static String loginAttemptReceived(String login, String password){
//        if(dbConnector.checkLoginAttempt(login, password)){
//            return login;
//        }
//        return "false";
//    }
//
//    public static void messageReceived(String message, String login, String userColor){
//        Pattern pattern = Pattern.compile("^/[a-zа-яА-ЯA-Z0-9_\\s]+$");
//        Matcher m;
//        m = pattern.matcher(message);
//        //Если команда, то пытаемся вызвать ее
//        if(m.matches()) {
//            message = message.substring(1);
//            String[] command = message.split(" ");
//            Commands.invoke(command, login);
//        }
//        //Иначе отправляем как сообщение в чат
//        else {
//            broadcaster.broadcastMessage(broadcaster.getRoomIdByUser(login), ServerMessage.userMessageSend(login, message, userColor));
//            java.util.Date date = new java.util.Date();
//            System.out.println("[" + date + "]" + login + ": " + message);
//        }
//    }
//
//    public static void disconnectReceived(Connection connection, String reason){
//        System.out.println(Utilities.getStartText("ServerMethods")+connection.getUserName()+" отключился: "+reason);
//        broadcaster.disconnectClient(connection.getUserName());
//        connection.disconnect(reason);
//    }
//
//    public static void joinRoomReceived(String userName, String roomId){
//        broadcaster.removeClientFromRoom(userName);
//        broadcaster.addClientToRoom(userName, roomId);
//    }
//
//    public static void pingReceived(Connection connection){
//        connection.sendMessage(ServerMessage.serverPongSend());
//    }
}
