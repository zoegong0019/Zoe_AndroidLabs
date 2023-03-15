package algonquin.cst2335.gong0019;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.gong0019.data.ChatMessage;
import algonquin.cst2335.gong0019.data.ChatMessageDAO;
import algonquin.cst2335.gong0019.data.ChatViewModel;
import algonquin.cst2335.gong0019.data.MessageDatabase;
import algonquin.cst2335.gong0019.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.gong0019.databinding.ReceiveMessageBinding;
import algonquin.cst2335.gong0019.databinding.SentMessageBinding;


public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    static ArrayList<ChatMessage> messages;
    private static RecyclerView.Adapter myAdapter;
    private static ChatMessageDAO mDAO;
    ChatViewModel chatModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "MyMessageDatabase").build();
        mDAO = db.cmDAO();

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());

        chatModel = new ViewModelProvider(this).get(ChatViewModel.class);
        messages = chatModel.messages.getValue();

        if(messages == null)
        {
            chatModel.messages.postValue(messages = new ArrayList<ChatMessage>());
        }

        setContentView(binding.getRoot());

        binding.button.setOnClickListener(click->{
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chm= new ChatMessage(binding.textInput.getText().toString(), currentDateandTime, true);
            messages.add(chm);
            myAdapter.notifyItemInserted(messages.size()-1);
            //clear the previous text:

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                long id = mDAO.insertMessage(chm);
                chm.id = id;
            });

            runOnUiThread(() ->{myAdapter.notifyItemInserted(messages.size() - 1);});

            binding.textInput.setText("");


        });
        binding.button1.setOnClickListener(click->{
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chm= new ChatMessage(binding.textInput.getText().toString(), currentDateandTime, false);
            messages.add(chm);
            myAdapter.notifyItemInserted(messages.size()-1);
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                long id = mDAO.insertMessage(chm);
                chm.id = id;
            });

            //you must update the screen, redraw the whole list
            runOnUiThread(() ->{myAdapter.notifyDataSetChanged();});

            //clear the previous text:
            binding.textInput.setText("");
        });

        binding.recyclerview.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if(viewType == 0) {
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }
                else
                {
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }
            }
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                String obj = messages.get(position).getMessage();
                holder.messageText.setText(obj);
                holder.timeText.setText(messages.get(position).getTimeSent());
            }
            @Override
            public int getItemCount() {
                return messages.size();
            }
            @Override
            public int getItemViewType(int position)
            {
                if(messages.get(position).getIsSentButton())
                {
                    return 0;
                }
                else
                {
                    return 1;
                }
            }

        });

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                ChatMessage clickedMessage = messages.get(position);


                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage("Do you want to delete the message:" + messageText.getText());
                builder.setTitle("Question:")
                        .setPositiveButton("Yes", (dialog, cl) -> {
                            ChatMessage m = messages.get(position);
                            Executors.newSingleThreadExecutor().execute(() -> {
                                mDAO.deleteMessage(clickedMessage);//delete the msg from database
                            });
                            messages.remove(position); //delete the msg from the msg list
                            // update the RecycleView list
                            myAdapter.notifyItemRemoved(position);
                            Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk2 -> {

                                        Executors.newSingleThreadExecutor().execute(() -> {
                                            mDAO.insertMessage(clickedMessage);
                                            messages.add(position, clickedMessage);
                                            runOnUiThread(() -> {myAdapter.notifyItemInserted(position);});
                                        });

                                    })
                                    .show();
                        })
                        .setNegativeButton("No", (dialog, cl) -> {})
                        .create().show();
            });
            messageText = itemView.findViewById(R.id.message);
            timeText =itemView.findViewById(R.id.time);

        }
    }
}

