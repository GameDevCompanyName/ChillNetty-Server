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

    private static void sendMessage(Channel userChannel, String message) {
        //TODO
        Logger.log("Пишу сообщение в канал: " + message, className);
        userChannel.write(message);
    }

    private static User initUser(Channel userChannel, String login) {

        Logger.log("Инициализирую нового пользователя: " + login, className);
        String userColor = DBConnector.getUserColor(login);
        String userRole = DBConnector.getUserRole(login);
        User newUser = new User(userChannel, login, userColor, userRole);

        Logger.log("Пользователь проинициализирован", className);
        return newUser;

    }

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
                Broadcaster.userLoggedIn(userChannel, newUser);
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
            Broadcaster.userLoggedIn(userChannel, newUser);
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

    private static void disconnectChannel(Channel userChannel) {

        //TODO Разобраться с отключением
        Logger.log("Отключаю канал от сервера", className);
        userChannel.close();

    }

    public static void joinRoomReceived(Channel userChannel, String roomId) {

        int id = Integer.parseInt(roomId);

        Logger.log("Проверяю залогинен ли канал, пытающийся сменить комнату", className);

        boolean userIsLogged = Broadcaster.checkIfChannelLogged(userChannel);

        if (userIsLogged){
            Logger.log("Канал залогинен, передаю запрос в Broadcaster", className);
            Broadcaster.roomChangeRequestRecieved(userChannel, id);
        } else {
            Logger.log("Канал НЕ залогинен и не может сменить комнату", className);
        }

    }

    public static void pingReceived(Channel userChannel) {

        Logger.log("Пришёл запрос пинга", className);
        sendPongAnswer(userChannel);

    }

    private static void sendValidVersionMessage(Channel userChannel) {

        Logger.log("Отправляю сообщение о совместимости версий", className);
        sendMessage(userChannel, ServerMessage.serverValidVersion());

    }

    private static void sendInvalidVerisonMessage(Channel userChannel) {

        Logger.log("Отправляю сообщение о несовместимой версии", className);
        sendMessage(userChannel, ServerMessage.serverInvalidVersion());

    }

    private static void sendUserAlreadyOnlineMessage(Channel userChannel) {

        Logger.log("Отправляю сообщение о том, что такой пользователь уже залогинен", className);
        sendMessage(userChannel, ServerMessage.loginAlreadyError());

    }

    private static void sendUserLoginSuccessMessage(Channel userChannel) {

        Logger.log("Отправляю сообщение об удачном логине", className);
        sendMessage(userChannel, ServerMessage.loginSuccess());

    }

    private static void sendWrongPasswordMessage(Channel userChannel) {

        Logger.log("Отправляю сообщение о неверном пароле", className);
        sendMessage(userChannel, ServerMessage.loginWrongError());

    }

    private static void sendPongAnswer(Channel userChannel) {

        Logger.log("Отправляю ответ на эхо-запрос", className);
        sendMessage(userChannel, ServerMessage.serverPong());

    }

}
