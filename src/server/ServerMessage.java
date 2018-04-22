package server;

import org.jboss.netty.channel.Channel;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/*
Статический класс для чтения и упаковки сообщений.
 */

public class ServerMessage {

    private static String className = "ServerMessage";

    public static void read(String message, Channel userChannel){
        Logger.log("Читаю сообщение", className);
        JSONObject incomingMessage = (JSONObject) JSONValue.parse(message);
        String type = incomingMessage.get("type").toString();
        switch (type){
            case "version":
                Logger.log("Получил сообщение о версии", className);
                ServerMethods.versionReceived(
                        userChannel,
                        incomingMessage.get("version").toString()
                );
                break;
            case "loginAttempt":
                Logger.log("Получил попытку логина", className);
                ServerMethods.loginAttemptReceived(
                        userChannel,
                        incomingMessage.get("login").toString(),
                        incomingMessage.get("password").toString()
                );
                break;
            case "message":
                Logger.log("Получил обычное сообщение", className);
                ServerMethods.messageReceived(
                        userChannel,
                        incomingMessage.get("text").toString()
                );
                break;
            case "disconnect":
                Logger.log("Получил сообщение об отключении", className);
                ServerMethods.disconnectReceived(
                        userChannel
                );
                break;
            case "joinRoom":
                Logger.log("Получил запрос на смену комнаты", className);
                ServerMethods.joinRoomReceived(
                        userChannel,
                        incomingMessage.get("roomId").toString()
                );
                break;
            case "ping":
                Logger.log("Получил пинг-запрос", className);
                ServerMethods.pingReceived(
                        userChannel
                );
                break;
            default:
                Logger.logError("Неизвестный тип сообщения: " + type, className);
                break;
        }

    }

    //Все статичные методы описаны в документации
    public static String clientVersionRequest(){
        JSONObject object = new JSONObject();
        object.put("type", "clientVersionRequest");
        return object.toJSONString();
    }

    public static String loginWrongError(){
        JSONObject object = new JSONObject();
        object.put("type", "loginWrongError");
        return object.toJSONString();
    }

    public static String loginAlreadyError(){
        JSONObject object = new JSONObject();
        object.put("type", "loginAlreadyError");
        return object.toJSONString();
    }

    public static String loginSuccess(){
        JSONObject object = new JSONObject();
        object.put("type", "loginSuccess");
        return object.toJSONString();
    }

    public static String userRegistrationSuccess(){
        JSONObject object = new JSONObject();
        object.put("type", "userRegistrationSuccess");
        return object.toJSONString();
    }
    public static String userColor(String login, String color){
        JSONObject object = new JSONObject();
        object.put("type", "userColor");
        object.put("login", login);
        object.put("color", color);
        return object.toJSONString();
    }
    public static String userMessage(String login, String message, String color){
        JSONObject object = new JSONObject();
        object.put("type", "userMessage");
        object.put("login", login);
        object.put("text", message);
        object.put("color", color);
        return object.toJSONString();
    }
    public static String userAction(String login, String action){
        JSONObject object = new JSONObject();
        object.put("type", "userAction");
        object.put("login", login);
        object.put("action", action);
        return object.toJSONString();
    }
    public static String serverMessage(String message){
        JSONObject object = new JSONObject();
        object.put("type", "serverMessage");
        object.put("text", message);
        return object.toJSONString();
    }
    public static String serverEvent(String event){
        JSONObject object = new JSONObject();
        object.put("type", "serverEvent");
        object.put("event", event);
        return object.toJSONString();
    }
    public static String serverUserKicked(String login, String reason){
        JSONObject object = new JSONObject();
        object.put("type", "serverUserKicked");
        object.put("login", login);
        object.put("reason", reason);
        return object.toJSONString();
    }
    public static String userDisconnect(String reason){
        JSONObject object = new JSONObject();
        object.put("type", "userDisconnect");
        object.put("reason", reason);
        return object.toJSONString();
    }
    public static String serverUserLogin(String login){
        JSONObject object = new JSONObject();
        object.put("type", "serverUserLogin");
        object.put("login", login);
        return object.toJSONString();
    }
    public static String serverUserDisconnect(String login){
        JSONObject object = new JSONObject();
        object.put("type", "serverUserDisconnect");
        object.put("login", login);
        return object.toJSONString();
    }

    public static String serverPing(){
        JSONObject object = new JSONObject();
        object.put("type", "ping");
        return object.toJSONString();
    }

    public static String serverPong(){
        JSONObject object = new JSONObject();
        object.put("type", "pong");
        return object.toJSONString();
    }

    public static String userRole(String login, String role){
        JSONObject object = new JSONObject();
        object.put("type", "userColor");
        object.put("login", login);
        object.put("role", role);
        return object.toJSONString();
    }

    public static String userChangedRoom(String roomId, String roomName){
        JSONObject object = new JSONObject();
        object.put("type", "userChangedRoom");
        object.put("roomId", roomId);
        object.put("roomName", roomName);
        return object.toJSONString();
    }

    public static String serverValidVersion() {
        JSONObject object = new JSONObject();
        object.put("type", "validVersion");
        return object.toJSONString();
    }

    public static String serverInvalidVersion() {
        JSONObject object = new JSONObject();
        object.put("type", "invalidVersion");
        return object.toJSONString();
    }
}
