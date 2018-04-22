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


//    Читаем входное сообщение от клиента
//    public static String read(String input, Channel connection){
//        timeoutChecker.refreshCounter(connection.getUserName());
//        JSONObject incomingMessage = (JSONObject) JSONValue.parse(input);
//        String type = incomingMessage.get("type").toString();
//        switch (type){
//            case "version":
//                return "version:"+ServerMethods.versionReceived(incomingMessage.get("first").toString());
//            case "loginAttempt":
//                return "loginAttempt:"+ServerMethods.loginAttemptReceived(
//                        incomingMessage.get("first").toString(),
//                        incomingMessage.get("second").toString()
//                );
//            case "message":
//                ServerMethods.messageReceived(
//                        incomingMessage.get("first").toString(),
//                        connection.getUserName(),
//                        connection.getUserColor()
//                );
//                return "true";
//            case "disconnect":
//                ServerMethods.disconnectReceived(connection, "пользователь разорвал соединение");
//                return "disconnect";
//            case "joinRoom":
//                ServerMethods.joinRoomReceived(
//                        connection.getUserName(),
//                        incomingMessage.get("first").toString()
//                );
//                return "true";
//            case "ping":
//                ServerMethods.pingReceived(connection);
//                return "true";
//            default:
//                return "false";
//        }
//    }

    //Все статичные методы описаны в документации
//    public static String clientVersionRequestSend(){
//        JSONObject object = new JSONObject();
//        object.put("type", "clientVersionRequest");
//        return object.toJSONString();
//    }
//
//    public static String loginWrongErrorSend(){
//        JSONObject object = new JSONObject();
//        object.put("type", "loginWrongError");
//        return object.toJSONString();
//    }
//
//    public static String loginAlreadyErrorSend(){
//        JSONObject object = new JSONObject();
//        object.put("type", "loginAlreadyError");
//        return object.toJSONString();
//    }
//
//    public static String loginSuccessSend(){
//        JSONObject object = new JSONObject();
//        object.put("type", "loginSuccess");
//        return object.toJSONString();
//    }
//
//    public static String userRegistrationSuccessSend(){
//        JSONObject object = new JSONObject();
//        object.put("type", "userRegistrationSuccess");
//        return object.toJSONString();
//    }
//    public static String userColorSend(String login, String color){
//        JSONObject object = new JSONObject();
//        object.put("type", "userColor");
//        object.put("first", login);
//        object.put("second", color);
//        return object.toJSONString();
//    }
//    public static String userMessageSend(String login, String message, String color){
//        JSONObject object = new JSONObject();
//        object.put("type", "userMessage");
//        object.put("first", login);
//        object.put("second", message);
//        object.put("third", color);
//        return object.toJSONString();
//    }
//    public static String userActionSend (String login, String action){
//        JSONObject object = new JSONObject();
//        object.put("type", "userAction");
//        object.put("first", login);
//        object.put("second", action);
//        return object.toJSONString();
//    }
//    public static String serverMessageSend(String message){
//        JSONObject object = new JSONObject();
//        object.put("type", "serverMessage");
//        object.put("first", message);
//        return object.toJSONString();
//    }
//    public static String serverEventSend(String event){
//        JSONObject object = new JSONObject();
//        object.put("type", "serverEvent");
//        object.put("first", event);
//        return object.toJSONString();
//    }
//    public static String serverUserKickedSend(String login, String reason){
//        JSONObject object = new JSONObject();
//        object.put("type", "serverUserKicked");
//        object.put("first", login);
//        object.put("second", reason);
//        return object.toJSONString();
//    }
//    public static String userDisconnectSend(String reason){
//        JSONObject object = new JSONObject();
//        object.put("type", "userDisconnect");
//        object.put("first", reason);
//        return object.toJSONString();
//    }
//    public static String serverUserLoginSend(String login){
//        JSONObject object = new JSONObject();
//        object.put("type", "serverUserLogin");
//        object.put("first", login);
//        return object.toJSONString();
//    }
//    public static String serverUserDisconnectSend(String login){
//        JSONObject object = new JSONObject();
//        object.put("type", "serverUserDisconnect");
//        object.put("first", login);
//        return object.toJSONString();
//    }
//
//    public static String serverPingSend(){
//        JSONObject object = new JSONObject();
//        object.put("type", "ping");
//        return object.toJSONString();
//    }
//
//    public static String serverPongSend(){
//        JSONObject object = new JSONObject();
//        object.put("type", "pong");
//        return object.toJSONString();
//    }
//
//    public static String userRoleSend(String login, String role){
//        JSONObject object = new JSONObject();
//        object.put("type", "userColor");
//        object.put("first", login);
//        object.put("second", role);
//        return object.toJSONString();
//    }
//
//    public static String userChangedRoomSend(String roomId, String roomName){
//        JSONObject object = new JSONObject();
//        object.put("type", "userChangedRoom");
//        object.put("first", roomId);
//        object.put("second", roomName);
//        return object.toJSONString();
//    }


}
