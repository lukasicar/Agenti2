package socket;

import com.google.gson.Gson;

public class WebSocketMessage{

    private Type type;

    private String data;

    private Boolean success;


    public WebSocketMessage() {
    }


    public WebSocketMessage(Type type, String data, Boolean success) {
        this.type = type;
        this.data = data;
        this.success = success;
    }

    public WebSocketMessage(Type type, Object data, Boolean success) {
        this(type, new Gson().toJson(data), success);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "WebSocketPacket{" +
                "type=" + type +
                ", data='" + data + '\'' +
                ", success=" + success +
                '}';
    }

    public enum Type {
        getClasses,
        getRunning,
        runAgent,
        stopAgent,
        sendMessage,
        getPerformatives
    }
}