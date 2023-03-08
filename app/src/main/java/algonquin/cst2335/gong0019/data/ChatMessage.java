package algonquin.cst2335.gong0019.data;
public class ChatMessage {
    String message;
    String timeSent;
    boolean isSentButton;

    void ChatRoom(String m, String t, boolean sent)
    {
        message = m;
        timeSent = t;
        isSentButton = sent;
    }
}
