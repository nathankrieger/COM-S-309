package com.example.frontend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.frontend.UserManager;
import com.example.frontend.WebSocketListener;
import com.example.frontend.WebSocketManager;
import com.example.frontend.models.User;

import org.java_websocket.handshake.ServerHandshake;

    public class AboutFragment extends Fragment implements WebSocketListener {

        // UI elements
        private TextView usernameTextView;
        private TextView roleTextView;
        private TextView bookmarksTextView;
        private TextView messagesTextView;
        private EditText dmEditText;
        private Button sendDmButton;

    // WebSocket instance for DM
    private WebSocketManager webSocketManager;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        // Initialize the UI elements
        usernameTextView = view.findViewById(R.id.usernameTextView);
//        roleTextView = view.findViewById(R.id.roleTextView);
        bookmarksTextView = view.findViewById(R.id.bookmarksTextView);
        messagesTextView = view.findViewById(R.id.messagesTextView);
        dmEditText = view.findViewById(R.id.dmEditText);
        sendDmButton = view.findViewById(R.id.sendDmButton);

        // Fetch the user data from the UserManager
        User user = UserManager.getInstance().getUser();
        if (user != null) {
            usernameTextView.setText("Username: " + user.getUsername());
            // roleTextView.setText("Role: " + user.getRole());  // You can enable this when you have the role
        }

        // Connect WebSocket
        webSocketManager = WebSocketManager.getInstance();
        webSocketManager.setWebSocketListener(this);
        webSocketManager.connectWebSocket("ws://coms-3090-022.class.las.iastate.edu:8080/AdminDM/" + "jakenickel");

        // Set up the button to send a DM
        sendDmButton.setOnClickListener(v -> {
            String message = dmEditText.getText().toString().trim();
            if (!message.isEmpty()) {
                sendDirectMessage(message);
                dmEditText.setText(""); // Clear input after sending
            }
        });

        return view;
    }

    // Send the direct message over WebSocket
    private void sendDirectMessage(String message) {
        webSocketManager.getInstance().sendMessage(message);

    }

    // Handle incoming WebSocket messages (for direct messages)
    @Override
    public void onWebSocketMessage(String message) {
        // Handle incoming WebSocket messages

            // Split the message by first space to separate the recipient and message content
            String[] splitMsg = message.split("\\s+", 2);
            String recipient = splitMsg[0].substring(1); // Remove '@' from the start
            String msgContent = splitMsg.length > 1 ? splitMsg[1] : ""; // Get the rest of the message

            // Display the received message in the UI
            requireActivity().runOnUiThread(() -> {
                String currentMessages = messagesTextView.getText().toString();
                String newMessage = recipient + ": " + msgContent + "\n";
                messagesTextView.setText(currentMessages + newMessage);
            });





    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        // Optionally handle WebSocket open event (not needed for this case)
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        // Optionally handle WebSocket close event (not needed for this case)
    }

    @Override
    public void onWebSocketError(Exception ex) {
        // Optionally handle WebSocket error event (not needed for this case)
    }
}