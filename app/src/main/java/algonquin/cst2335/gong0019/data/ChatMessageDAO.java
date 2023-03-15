package algonquin.cst2335.gong0019.data;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatMessageDAO {

    @Insert
    public long insertMessage(ChatMessage m);

    @Delete
    public void deleteMessage(ChatMessage m);

    @Query("SELECT * FROM ChatMessage")
    public List<ChatMessage> getAllMessages();
}
