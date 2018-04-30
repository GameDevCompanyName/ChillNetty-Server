package server;

import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private static String className = "Room";

    private int roomId;
    private String roomName;
    private List<User> users;

    public Room(int roomId, String roomName){
        this.roomId = roomId;
        this.roomName = roomName;
        this.users = new ArrayList<>();
    }

    public int getId() {
        return roomId;
    }

    public void addUser(User newUser) {
        if (users.contains(newUser)){
            Logger.logError("Пользователь " + newUser.getLogin() + " уже находится в комнате " + roomName + "(" + roomId + ")", className);
        } else {
            users.add(newUser);
            broadcastServerMessage(TextFormer.userConnected(newUser.getLogin()));
            Logger.log("Пользователь " + newUser.getLogin() + " добавлен в комнату " + roomName + "(" + roomId + ")", className);
        }
    }

    public void removeUser(User userToDelete) {
        if (!users.contains(userToDelete)){
            Logger.logError("Пользователя " + userToDelete.getLogin() + " нет в комнате " + roomName + "(" + roomId + ")", className);
        } else {
            users.remove(userToDelete);
            broadcastServerMessage(TextFormer.userDisconnected(userToDelete.getLogin()));
            Logger.log("Пользователь " + userToDelete.getLogin() + " удалён из комнаты " + roomName + "(" + roomId + ")", className);
        }
    }

    private void broadcastServerMessage(String text) {
        Logger.log("Отправляю всем пользователям в комнате " + roomName + " сообщение сервера", className);
        for (User user: users) {
            user.sendMessage(ServerMessage.serverMessage(text));
        }
    }

    public void broadcastUserMessage(User sender, String text) {
        Logger.log("Отправляю всем пользователям в комнате " + roomName + " сообщение пользователя", className);
        for (User user: users) {
            user.sendMessage(ServerMessage.userMessage(
                    sender.getLogin(),
                    text,
                    sender.getColor()
            ));
        }
    }

    public Object getName() {
        return roomName;
    }

    public int getPeopleQuantity() {
        return users.size();
    }
}
