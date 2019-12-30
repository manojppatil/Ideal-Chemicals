package com.bitsinfotec.idealchemicals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bitsinfotec.idealchemicals.Adapter.ChatAppMsgAdapter;
import com.bitsinfotec.idealchemicals.Helper.DialogBox;
import com.bitsinfotec.idealchemicals.Helper.L;
import com.bitsinfotec.idealchemicals.Helper.Routes;
import com.bitsinfotec.idealchemicals.Helper.SharedPreferencesWork;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;

public class MessagesActivity extends AppCompatActivity {
    private int inserted_id;
    Context context;
    EditText msgInputText;
    RecyclerView msgRecyclerView;
    List<ChatAppMsgDTO> msgDtoList;
    ChatAppMsgAdapter chatAppMsgAdapter;
    ArrayList arr = new ArrayList();
    RequestParams requestParams = new RequestParams();
    ChatAppMsgDTO msgDto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        setTitle("Send Message to Admin");

        // Get RecyclerView object.
        msgRecyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);

        // Set RecyclerView layout manager.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(linearLayoutManager);

        // Create the initial data list.
        msgDtoList = new ArrayList<ChatAppMsgDTO>();
        msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, "hello");
        msgDtoList.add(msgDto);

        // Create the data adapter with above data list.
        chatAppMsgAdapter = new ChatAppMsgAdapter(msgDtoList, MessagesActivity.this);

        // Set data adapter to RecyclerView.
        msgRecyclerView.setAdapter(chatAppMsgAdapter);
        msgInputText = (EditText) findViewById(R.id.chat_input_msg);

        Button msgSendButton = (Button) findViewById(R.id.chat_send_msg);

        requestParams.add("tbname", "chat_message");

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post(Routes.selectAll, requestParams, new AsyncHttpResponseHandler() {
            AlertDialog dialog = new SpotsDialog(MessagesActivity.this, R.style.Custom);

            public void onStart() {
                super.onStart();
                dialog.show();
            }

            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                dialog.dismiss();
                String str = new String(responseBody);
//                str = str.replace("<br>", "\n");
                JSONArray jr = null;
                JSONObject jo = null;
                L.L(str);
                try {
                    if (statusCode == 200) {
                        try {
                            jr = new JSONArray(str);
                            ArrayList arr2 = new ArrayList();
                            for (int i = 0; i < jr.length(); i++) {
                                ChatAppMsgDTO chatAppMsgDTO = new ChatAppMsgDTO();
                                jo = jr.getJSONObject(i);
                                if (jo != null) {
                                    chatAppMsgDTO.setMsgContent(jo.get("input_message").toString());
                                    chatAppMsgDTO.setId(jo.get("id").toString());
                                    chatAppMsgDTO.setSender_id(jo.get("sender_id").toString());
                                    L.L(chatAppMsgDTO.getMsgContent());
                                }
                                arr2.add(chatAppMsgDTO);
                            }
                            ChatAppMsgAdapter chatAppMsgAdapter = new ChatAppMsgAdapter(arr2, MessagesActivity.this);
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MessagesActivity.this));
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(chatAppMsgAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        L.L(statusCode + str);
                        new DialogBox(MessagesActivity.this, str).asyncDialogBox();
                    }
                } catch (Exception ex) {
                    L.L(ex.toString());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                    error) {
                new DialogBox(MessagesActivity.this, responseBody.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                dialog = ProgressDialog.show(MessagesActivity.this, "none to say",
                        "Saving.Please wait...", true);
            }

        });
    }


    public void send(View view) {
        String msgContent = msgInputText.getText().toString();
        if (!TextUtils.isEmpty(msgContent)) {
            // Add a new sent message to the list.
            ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, msgContent);
            msgDtoList.add(msgDto);

            int newMsgPosition = msgDtoList.size() - 1;

            // Notify recycler view insert one new data.
            chatAppMsgAdapter.notifyItemInserted(newMsgPosition);

            // Scroll RecyclerView to the last message.
            msgRecyclerView.scrollToPosition(newMsgPosition);

            // Empty the input edit text box.
            msgInputText.setText("");
            HashMap hashMap = new SharedPreferencesWork(MessagesActivity.this).checkAndReturn(Routes.sharedPrefForLogin,"id");
            L.L(hashMap.size()+"*-----*--*-*-");
            String userid = hashMap.get("id").toString();
            RequestParams requestParams = new RequestParams();
            requestParams.add("sender_id",userid);
            requestParams.add("receiver_id","Manoj_Patil849");
            requestParams.add("input_message", msgContent);
            requestParams.add("msgType", "MSG_TYPE_SENT");
            requestParams.add("status", "active");
            requestParams.add("tbname", "chat_message");

            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.post(Routes.insert2, requestParams, new AsyncHttpResponseHandler() {
                ProgressDialog dialog;

                @Override
                public void onStart() {
                    dialog = ProgressDialog.show(MessagesActivity.this, "Working",
                            "Saving data...", true);
                }

                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    dialog.dismiss();
                    String str = new String(responseBody);
                    str = str.replace("<br>", "\n");
                    JSONArray jr = null;
                    JSONObject jo = null;
                    L.L(str);
                    try {
                        if (statusCode == 200) {
                            try {
                                jo = new JSONObject(str);
                                if (jo.getString("status").equals("success")) {
                                    inserted_id = Integer.parseInt(jo.getString("recentinsertedid"));
                                    new DialogBox(MessagesActivity.this, jo.get("message").toString()).asyncDialogBox();
                                    Toast.makeText(context, "Data inserted " + jo.getString("status"), Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(context, "Data not inserted " + jo.getString("status"), Toast.LENGTH_SHORT).show();
                                    new DialogBox(MessagesActivity.this, jo.get("message").toString()).asyncDialogBox();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            L.L(statusCode + str);
                            new DialogBox(MessagesActivity.this, str).asyncDialogBox();
                        }
                    } catch (Exception ex) {
                        L.L(ex.toString());
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }
    }
}





