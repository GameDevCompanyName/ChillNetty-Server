package server;

import org.jboss.netty.channel.Channel;

public class User {

    private static String className = "User";
    private String login;
    private Channel userChannel;
    private String color;
    private String role;
    private String roomId;

    public User(Channel userChannel, String login, String color, String role){
        Logger.log("Создаю нового Юзера, Login: " + login, className);
        this.userChannel = userChannel;
        this.login = login;
        this.color = color;
        this.role = role;
        roomId = "0";
    }

    public User(String login){
        this.login = login;
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
