package study.core.model;

public class Message {

    public enum Type {
        entrance,
        exit,
        chat,
    }

    private Type type;

    private String message;

    private String name;

    private Integer room;

    public Type getType() {
        return this.type;
    }

    public String getMessage() {
        return this.message;
    }

    public Integer getRoom() {
        return this.room;
    }

    public String getName() {
        return name;
    }

    public static Message Chat(String name, String message) {
        Message instance = new Message();
        instance.name = name;
        instance.type = Type.chat;
        instance.message = message;
        return instance;
    }

    public static Message Exit(String name) {
        Message instance = new Message();
        instance.type = Type.exit;
        instance.message = name + " has exited this room";
        return instance;
    }

    public static Message Exit() {
        return Exit(null);
    }

    public static Message Entrance(String name, int roomId) {
        Message instance = new Message();
        instance.type = Type.entrance;
        instance.name = name;
        instance.room = roomId;
        return instance;
    }

    public static Message Entrance(int roomId) {
        return Entrance(null, roomId);
    }
}
