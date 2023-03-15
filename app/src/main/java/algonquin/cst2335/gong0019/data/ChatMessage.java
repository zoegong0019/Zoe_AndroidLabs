package algonquin.cst2335.gong0019.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    public long id;
    @ColumnInfo(name = "Message")
    private String message;
    @ColumnInfo(name = "TimeSent")
    private String timeSent;

    @ColumnInfo(name = "IsSent")
    private boolean isSentButton;

    public ChatMessage(String message, String timeSent, boolean isSentButton) {
        this.message = message;
        this.timeSent =timeSent;
        this.isSentButton=isSentButton;
    }

        public String getMessage()
        {
            return this.message;
        }

        public String getTimeSent()
        {
            return this.timeSent;
        }

        public boolean getIsSentButton()
        {
            return this.isSentButton;
        }
    }

