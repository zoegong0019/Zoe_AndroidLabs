package algonquin.cst2335.gong0019;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import algonquin.cst2335.gong0019.data.ChatViewModel;
import algonquin.cst2335.gong0019.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.gong0019.databinding.SentMessageBinding;


public class ChatRoom extends AppCompatActivity {
    ChatViewModel chatModel ;
    private RecyclerView.Adapter<MyRowHolder> myAdapter;
    private List<String> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityChatRoomBinding binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        chatModel = new ViewModelProvider(this).get(ChatViewModel.class);
        messageList = chatModel.messages.getValue();
        if(messageList == null)
        {
            chatModel.messages.postValue((ArrayList<String>) (messageList = new ArrayList<String>()));
        }

        setContentView(binding.getRoot());


        binding.recyclerview.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater(),
                        parent,false);
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
            String messageOnThisRow = messageList.get(position);
            holder.messageText.setText(messageOnThisRow);

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
                String currentDateandTime = sdf.format(new Date());

            holder.timeText.setText(currentDateandTime);
            }

            @Override
            public int getItemCount() {
                return messageList.size();
            }
        });
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.button.setOnClickListener(click ->{

            String txt = binding.textInput.getText().toString();
            messageList.add(txt);

            myAdapter.notifyDataSetChanged();

            binding.textInput.setText("");
        });

        binding.button1.setOnClickListener(click ->{

            String txt = binding.textInput.getText().toString();
            messageList.add(txt);

            myAdapter.notifyDataSetChanged();

            binding.textInput.setText("");
        });


    }
    class MyRowHolder extends RecyclerView.ViewHolder {
        public TextView messageText;
        public TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);

        }
    }
}