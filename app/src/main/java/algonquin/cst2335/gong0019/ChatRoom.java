package algonquin.cst2335.gong0019;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import algonquin.cst2335.gong0019.data.MessageDetailsFragment;
import algonquin.cst2335.gong0019.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.gong0019.databinding.ReceiveMessageBinding;
import algonquin.cst2335.gong0019.databinding.SentMessageBinding;


public class ChatRoom extends AppCompatActivity {
private static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages;
    private RecyclerView.Adapter myAdapter;
    private ChatMessageDAO mDAO;
    ChatViewModel chatModel;
    ChatMessage clickedMessage;
    TextView globalMessageText;
    int position;
    MessageDetailsFragment chatFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "MyMessageDatabase").build();
        mDAO = db.cmDAO();

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());

        chatModel = new ViewModelProvider(this).get(ChatViewModel.class);
        messages = chatModel.messages.getValue();

        if (messages == null) {
            messages = new ArrayList<ChatMessage>();


            Executors.newSingleThreadExecutor().execute(() -> {
                messages.addAll(mDAO.getAllMessages());//delete the msg from database
            });

            chatModel.messages.postValue(messages);
        }

        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarLayout.toolbar);

        binding.button.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chm = new ChatMessage(binding.textInput.getText().toString(), currentDateandTime, true);
            messages.add(chm);
            myAdapter.notifyItemInserted(messages.size() - 1);
            //clear the previous text:

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {

                long id = mDAO.insertMessage(chm);
                chm.id = id;
            });

            runOnUiThread(() -> {
                myAdapter.notifyItemInserted(messages.size() - 1);
            });

            binding.textInput.setText("");


        });
        binding.button1.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chm = new ChatMessage(binding.textInput.getText().toString(), currentDateandTime, false);
            messages.add(chm);
            myAdapter.notifyItemInserted(messages.size() - 1);
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                long id = mDAO.insertMessage(chm);
                chm.id = id;
            });

            //you must update the screen, redraw the whole list
            runOnUiThread(() -> {
                myAdapter.notifyDataSetChanged();
            });

            //clear the previous text:
            binding.textInput.setText("");
        });
        binding.recyclerview.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override

            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                } else {
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
            public int getItemViewType(int position) {
                if (messages.get(position).getIsSentButton()) {
                    return 0;
                } else {
                    return 1;
                }
            }

        });


        chatModel.selectedmessages.observe(this, (newMessageValue) -> {

            chatFragment = new MessageDetailsFragment(newMessageValue);
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.replace(R.id.fragmentLocation, chatFragment, FRAGMENT_TAG);
            fragmentTransaction.commit();
        });

    binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                position = getAbsoluteAdapterPosition();
                clickedMessage = messages.get(position);
                chatModel.selectedmessages.postValue(clickedMessage);

/*
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



     */
 });

                messageText = itemView.findViewById(R.id.message);
                timeText = itemView.findViewById(R.id.time);
                globalMessageText = itemView.findViewById(R.id.message);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_1:
                String messageText = chatModel.selectedmessages.getValue().getMessage();
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage("Do you want to delete the message:" + messageText);
                builder.setTitle("Question:")
                        .setPositiveButton("Yes", (dialog,cl) -> {
                            ChatMessage m = messages.get(position);

                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(() -> {
                                mDAO.deleteMessage(clickedMessage);
                            });

                            messages.remove(position);
                            myAdapter.notifyItemRemoved(position);
                            removeFragment();
                            Snackbar.make(globalMessageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk2 -> thread.execute(() -> {
                                        mDAO.insertMessage(clickedMessage);
                                        messages.add(position, clickedMessage);
                                        runOnUiThread(()-> myAdapter.notifyItemInserted(position));
                                    }))
                                    .show();
                        })
                        .setNegativeButton("No", (dialog, cl) ->{})
                        .create().show();
                break;

            case R.id.item_2:
                Toast.makeText(this, "Version 1.0. create by Zoe Gong", Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new IllegalStateException("Unexpected value" + item.getItemId());
        }
        return true;

        }
        public void removeFragment() {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();

            }
        }
}

