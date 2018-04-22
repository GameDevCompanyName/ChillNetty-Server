package server;

import org.jboss.netty.channel.Channel;

import static server.Constants.START_ROOM_ID;

public class User {

    private static String className = "User";
    private String login;
    private Channel userChannel;
    private String color;
    private String role;
    private int roomId;

    public User(Channel userChannel, String login, String color, String role){
        Logger.log("Создаю нового Юзера, Login: " + login, className);
        this.userChannel = userChannel;
        this.login = login;
        this.color = color;
        this.role = role;
        roomId = START_ROOM_ID;
    }

    public User(String login){
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public Channel getUserChannel() {
        return userChannel;
    }

    public String getColor() {
        return color;
    }

    public String getRole() {
        return role;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void sendMessage(String message) {
        userChannel.write(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return login != null ? login.equals(user.login) : user.login == null;
    }

    @Override
    public int hashCode() {
        return login != null ? login.hashCode() : 0;
    }

}
