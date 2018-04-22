package server;

import org.jboss.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

public class Broadcaster {

    private static String className = "Broadcaster";
    
    private static Map<Channel, User> loggedChannels = new HashMap<>();
    private static Map<User, Channel> loggedUsers = new HashMap<>();

    private static Map<Integer, Room> rooms = new HashMap<>();

    public static void createDefaultRooms(){

        Room main = new Room(0, "Главная");
        rooms.put(main.getId(), main);

    }

    public static boolean checkIfUserOnline(String login) {

        boolean userOnline = false;

        User userToCheck = new User(login);

        Logger.log("Проверяю залогинен ли пользователь: " + login, className);
        if (loggedUsers.containsKey(userToCheck))
            userOnline = true;
        Logger.log("Проверил онлайн ли пользователь: " + login, className);

        return userOnline;

    }

    public static void userLoggedIn(Channel userChannel, User newUser) {

        Logger.log("Добавляю нового пользователя в список залогиненых: " + newUser.getLogin(), className);
        loggedChannels.put(userChannel, newUser);
        loggedUsers.put(newUser, userChannel);
        rooms.get(newUser.getRoomId()).addUser(newUser);

    }

    public static boolean checkIfChannelLogged(Channel userChannel) {

        boolean channelLogged = false;

        Logger.log("Проверяю залогинен ли канал", className);
        if (loggedChannels.containsKey(userChannel))
            channelLogged = true;

        return channelLogged;

    }

    public static void messageRecieved(Channel userChannel, String text) {

        User sender = loggedChannels.get(userChannel);

        Room senderRoom = rooms.get(sender.getRoomId());

        senderRoom.broadcastUserMessage(sender, text);

    }

    public static void userDisconnected(Channel userChannel) {

        boolean channelOnline = checkIfChannelLogged(userChannel);

        if (!channelOnline)
            return;

        User userToDelete = loggedChannels.get(userChannel);

        Logger.log("Удаляю пользователя из списка залогиненых: " + userToDelete.getLogin(), className);
        loggedChannels.remove(userChannel);
        loggedUsers.remove(userToDelete);
        rooms.get(userToDelete.getRoomId()).removeUser(userToDelete);

    }

    public static void roomChangeRequestRecieved(Channel userChannel, int roomId) {


        User user = loggedChannels.get(userChannel);
        Logger.log("Обрабатываю запрос на смену комнаты от: " + user.getLogin(), className);

        Room oldRoom = rooms.get(user.getRoomId());
        oldRoom.removeUser(user);

        Room newRoom = rooms.get(roomId);
        newRoom.addUser(user);

        Logger.log("Смена комнаты для: " + user.getLogin() + " - DONE", className);

    }

    public static void changeUserRole(String login, String role) {

        Logger.log("Пытаюсь сменить роль пользователя " + login + " на " + role, className);

        User user = getUserByLogin(login);

        if (user == null){
            Logger.logError("Такого пользователя не нашлось", className);
            return;
        }


        user.setRole(role);
        Logger.log("Смена роли для " + login + " - DONE", className);

    }

    private static User getUserByLogin(String login) {

        Logger.log("Пытаюсь найти юзера по логину: " + login, className);
        User user = new User(login);

        if (!loggedUsers.containsKey(user)){
            Logger.log("Юзера с таким логином не нашлось, возвращаю NULL", className);
            return null;
        }

        Logger.log("Пользователь нашёлся", className);
        Channel userChannel = loggedUsers.get(user);

        return loggedChannels.get(userChannel);

    }

    public static void changeUserColor(String login, String color) {

        Logger.log("Пытаюсь сменить цвет пользователя " + login + " на " + color, className);

        User user = getUserByLogin(login);

        if (user == null){
            Logger.logError("Такого пользователя не нашлось", className);
            return;
        }

        user.setColor(color);
        Logger.log("Смена цвета для " + login + " - DONE", className);

    }

}
