package com.example.finalproject2_0;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject2_0.Adapters.MessageAdapter;
import com.example.finalproject2_0.Models.Message;
import com.example.finalproject2_0.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Chat extends AppCompatActivity {

    private FirebaseFirestore db;
    private List<Message> chatMessages;
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private TextView gameName;
    ImageButton back_btn;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();


        db = FirebaseFirestore.getInstance();

        gameName = findViewById(R.id.gameName);
        gameName.setText(intent.getStringExtra("gameName"));

        recyclerView = findViewById(R.id.recyclerView);
        EditText editTextMessage = findViewById(R.id.messageEditText);
        ImageButton buttonSend = findViewById(R.id.sendButton);
        back_btn = findViewById(R.id.back);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chat.this, MainActivity.class);
                intent.putExtra("toMyGames", true);
                startActivity(intent);
            }
        });

        chatMessages = new ArrayList<>();
        messageAdapter = new MessageAdapter(this,chatMessages,intent.getStringExtra("ownerID"));
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadChatMessages(intent.getStringExtra("gameID"));

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = editTextMessage.getText().toString();
                if (!messageText.isEmpty()) {

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(new Date());

                    db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    User user = documentSnapshot.toObject(User.class);
                                    String userName = documentSnapshot.getString("name");

                                    Message message = new Message(userId, messageText, timestamp, userName, user);
                                    chatMessages.add(message);
                                    messageAdapter.notifyItemInserted(chatMessages.size() - 1);
                                    recyclerView.scrollToPosition(chatMessages.size() - 1);

                                    updateGameChat(intent.getStringExtra("gameID"), chatMessages);

                                    editTextMessage.setText("");

                                }
                            });

                }
            }
        });
    }

    private void loadChatMessages(String gameId) {
        DocumentReference gameRef = db.collection("Games").document(gameId);
        gameRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<Map<String, Object>> chatData = (List<Map<String, Object>>) documentSnapshot.get("chat");
                if (chatData != null) {
                    for (Map<String, Object> messageData : chatData) {
                        String userId = (String) messageData.get("userId");
                        String message = (String) messageData.get("message");
                        String timestamp = (String) messageData.get("timestamp");
                        String userName = (String) messageData.get("username");

                        // Ensure user data is retrieved as a Map
                        Map<String, Object> userMap = (Map<String, Object>) messageData.get("user");
                        User user = null;
                        if (userMap != null) {
                            String age = (String) userMap.get("age");
                            String available = (String) userMap.get("available");
                            String hobbies = (String) userMap.get("hobbies");
                            String name = (String) userMap.get("name");
                            String userID = (String) userMap.get("userID");

                            // Initialize the User object with the retrieved fields
                            user = new User(age, available, hobbies, name, userID);
                        }

                        chatMessages.add(new Message(userId, message, timestamp, userName, user));
                    }
                    messageAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void updateGameChat(String gameId, List<Message> messages) {
        DocumentReference gameRef = db.collection("Games").document(gameId);
        List<Map<String, Object>> chatData = new ArrayList<>();
        for (Message message : messages) {
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("userId", message.getUserId());
            messageMap.put("message", message.getMessage());
            messageMap.put("timestamp", message.getTimestamp());
            messageMap.put("username", message.getUserName());

            User user = message.getUser();
            if (user != null) {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("age", user.getAge());
                userMap.put("available", user.getAvailable());
                userMap.put("hobbies", user.getHobbies());
                userMap.put("name", user.getName());
                userMap.put("userID", user.getUserID());
                messageMap.put("user", userMap);
            } else {
                messageMap.put("user", null);
            }

            chatData.add(messageMap);
        }

        gameRef.update("chat", chatData);
    }
}