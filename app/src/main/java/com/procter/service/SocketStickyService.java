package com.procter.service;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.procter.R;
import com.procter.activity.MainActivity;
import com.procter.application.ProcterAppController;
import com.procter.interfaces.SocketInterface;
import com.procter.receiver.NetworkChangeReceiver;
import com.procter.session.Session;
import com.procter.utils.ConnectionDetector;
import com.procter.utils.CusDialogProg;
import com.procter.utils.NetworkStatus;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * Created by ptblr-1199 on 20/12/17.
 */

public class SocketStickyService extends Service implements
        NetworkChangeReceiver.NetworkStatusListener {

    private static final String TAG = "SocketStickyService";
    private static final long INTERVAL = 1000 * 5;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private static final int NOTIFICATION_ID = 99;
    private final IBinder mBinder = new SocketStickyService.LocalBinder();
    WebSocketClient mWebSocketClient;
    SocketInterface socketInterface;
    URI uri;
    int dialogFlag = 0;
    //CusDialogProg pDialog;
    Boolean isInternetPresent = false;
    ConnectionDetector cd = null;
    Dialog dialog = null;
    Context context;
    private boolean isSocketConnectedDueToNetwork;
    private boolean isNetworkConnected;

    public SocketStickyService() {
    }

    public SocketStickyService(Context context) {
        Log.e(TAG, "SocketStickyService: Constructor **** " + context);
        this.context = context;
        socketInterface = (SocketInterface) context;

        Log.e(TAG, "SocketStickyService: " + this.context);
        if (context != null) {
            isNetworkConnected = NetworkStatus.isNetwrokAvailable(context);
        }
        //connect_to_port(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: Sticky Service****");
        ProcterAppController.getInstance().setNetworkStatusListener(this);
        try {
            //ec2-52-200-8-36.compute-1.amazonaws.com
            //port:8421
//            uri = new URI("ws://ec2-52-66-87-59.ap-south-1.compute.amazonaws.com:8421");
//            uri = new URI("ws://ec2-52-200-8-36.compute-1.amazonaws.com:5216");
            uri = new URI("ws://52.200.8.36:5216");
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }
        connectWebSocket();
    }

    public void initCallBack(Context ctx) {
        socketInterface = (SocketInterface) ctx;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.e(TAG, "onNetworkConnectionChanged:SocketSticky ***** " + isConnected);
        //Once Network Came Socket Got connected so avoid dual connection  maintain  isSocketConnectAfterNetworkGone flag
        if (isSocketConnectedDueToNetwork && isConnected) {
            Log.e(TAG, "onNetworkConnectionChanged: service inside ********");
            connectWebSocket();

//        } else if (isConnected) {
//            runAsForeground("LinkMeEasy is Online");
//        } else if (!isConnected) {
//            runAsForeground("LinkMeEasy is Offline Now");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //    super.onStartCommand(intent, flags, startId);

        Log.e(TAG, "onStartCommand: " + intent);
        Log.e(TAG, "onStartCommand: " + flags);
        Log.e(TAG, "onStartCommand: " + startId);
        Log.e(TAG, "onStartCommand: context " + this.context);
        Log.e(TAG, "onStartCommand: getAPpContext" + getApplicationContext());

        return START_STICKY;
    }

    public void connect_to_port(Context context) {
        Log.e(TAG, "connect_to_port:  context " + context);
        cd = new ConnectionDetector(context);
        Log.e(TAG, "connect_to_port: " + cd);
        socketInterface = (SocketInterface) context;
        Log.e(TAG, "connect_to_port: " + socketInterface);
      //  pDialog = new CusDialogProg(context);
        //Log.e(TAG, "connect_to_port: " + pDialog);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: Sticky" + intent);
        Log.e(TAG, "onBind: Sticky" + this.context);
        //connect_to_port(context);
        return mBinder;
        //  return null;MainApplication
    }

    public void connectWebSocket() {
        Log.e(TAG, "connectWebSocket: " + isNetworkConnected);

        if (NetworkStatus.isNetwrokAvailable(ProcterAppController.getInstance())) {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    dialogFlag = 0;
                }
            }
            try {
                mWebSocketClient = new WebSocketClient(uri) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        //ApplicationPreference sp = ApplicationPreference.getInstance(getApplicationContext());
                        JSONObject obj = new JSONObject();
                        Session preference = new Session(getApplicationContext());
                        try {
                            obj.put("case_value", "201");
                            obj.put("user_id", preference.getUserInfo().getUser_id());

                        } catch (JSONException e) {
                            Log.e(TAG, "onOpen: Exception Block" + e);
                            e.printStackTrace();
                        }

                        Log.e(TAG, "onOpen: " + mWebSocketClient);
                        mWebSocketClient.send(obj.toString());
                        Log.e("Web_socket", "Opened" + " initialData" + obj.toString());
                    }

                    @Override
                    public void onMessage(String s) {
                        final String response = s;
                        Log.e(TAG, "onMessage: " + s);
                        try {
                            JSONObject rootObj = new JSONObject(response);
                            if (rootObj.has("key")) {
//                                if (rootObj.getString("key").equals("new_ride_request")) {
//                                    Log.e(TAG, "onMessage: Driver Request came *****");
//                                    pushAppToForground();
//                                    //     parseDataToMainActivity(response);
//
//                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            try {
//                                                Log.e(TAG, "run: Service *************** For new Request" + response);
//                                                Log.e(TAG, "run: " + socketInterface);
//                                                if (socketInterface != null) {
//                                                    socketInterface.socketResponse(response);
//                                                }
//                                            } catch (Exception e) {
//                                                Log.e(TAG, "run: exception service" + e);
//                                            }
//                                        }
//                                    }, 3000);
//                                } else {
//                                    Log.e(TAG, "onMessage: Else Request*****");
                                    if (rootObj.has("key") && rootObj.getString("key").equalsIgnoreCase("socket_connected")) {
                                        isSocketConnectedDueToNetwork = false;
                                    }
                                    parseDataToMainActivity(response);
//                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onClose(int i, String s, boolean b) {

                        Log.e(TAG, "Closed reason " + s + "I " + i + "b " + b);
                        try {
                            if (i == 1006) {
                                final JSONObject rootObj = new JSONObject();
                                rootObj.put("key", "socket_disconnected_1006");
                                Log.e(TAG, "onClose: insidee****" + mWebSocketClient.getReadyState());
                                Log.e(TAG, "onClose: inseide***********" + mWebSocketClient.getConnection().getReadyState());
                                //   pushAppToForground();

                                sendErrorLogToActivity(rootObj);

                            } else if (i == -1 && s != null && s.equals("Connection timed out")) {
                                Log.e(TAG, "onClose: *******Connection Time Out Error Block ******");
                                JSONObject rootObj = new JSONObject();
                                rootObj.put("key", "Connection timed out");
                                //    pushAppToForground();
                                sendErrorLogToActivity(rootObj);

                            } else {
                                Log.e(TAG, "onClose: elseONcLOSE");
                                JSONObject rootObj = new JSONObject();
                                rootObj.put("key", "socket_disconnected");
                                parseDataToMainActivity(rootObj.toString());
                            }


                        } catch (JSONException e) {
                            Log.e(TAG, "onClose: " + e);
                            e.printStackTrace();
                        }

                    }

                    private void sendErrorLogToActivity(final JSONObject rootObj) {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.e(TAG, "run: Service *************** For close application");
                                    Log.e(TAG, "run: " + socketInterface);
                                    parseDataToMainActivity(rootObj.toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e(TAG, "run: exception service" + e);
                                }
                            }
                        }, 5000);
                    }

                    @Override
                    public void onError(Exception e) {

                        Log.e(TAG, "Error " + e.getMessage());
                        try {
                            JSONObject rootObj = new JSONObject();
                            rootObj.put("key", "socket_error_from_android");
                            parseDataToMainActivity(rootObj.toString());

                        } catch (JSONException ex) {
                            Log.e(TAG, "onError: ******" + ex);
                            ex.printStackTrace();
                        }

                    }
                };

                Log.e(TAG, "connectWebSocket: *****after block " + mWebSocketClient);
                if (mWebSocketClient != null && mWebSocketClient.getSocket() != null) {
                    Log.e(TAG, "connectWebSocket: SocketTIme out" + mWebSocketClient.getSocket().getSoTimeout());
                }

                Log.e(TAG, "connectWebSocket: *****after block .isClosed()" + mWebSocketClient.getConnection().isClosed());
                Log.e(TAG, "connectWebSocket: *****after block .isCloseding()" + mWebSocketClient.getConnection().isClosing());

                Log.e(TAG, "connectWebSocket: *****after block getReday  status" + mWebSocketClient.getReadyState());
                /*Socket socket = mWebSocketClient.getSocket();
                socket.close();*/

                if (/*!mWebSocketClient.getConnection().isOpen() && !mWebSocketClient.getConnection().isConnecting()*/ mWebSocketClient.getReadyState() == WebSocket.READYSTATE.NOT_YET_CONNECTED || mWebSocketClient.getReadyState() == WebSocket.READYSTATE.CLOSED) {
                    Log.e(TAG, "connectWebSocket: is not open ******");
                    mWebSocketClient.connectBlocking();
                } else {
                    if (mWebSocketClient.getConnection().isOpen() && mWebSocketClient.getConnection().isConnecting()) {
                        Log.e("Socket", "connect initiated trying to connect");
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "connectWebSocket: exp" + e);

            }
        } else {
            isSocketConnectedDueToNetwork = true;
            Log.e(TAG, "connectWebSocket: network else********************");
            Log.e(TAG, "connectWebSocket: DialogFlag " + dialogFlag);
            if (dialogFlag == 0) {
                try {
                    if (ProcterAppController.isActivityVisible()) {
                        showInternetErrorDialog();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "connectWebSocket: Expe SHow Dialog ****");
                    e.printStackTrace();
                }
            }
        }
    }

    /*  This method basically I am using when service is running and and netwrok got disconnected and then
        we killed the application again launch app so  will have to call socket connection method in
        onServiceConnected() method
        */
    public boolean isSocketConnectAfterNetworkGone() {
        return isSocketConnectedDueToNetwork;
    }

    public void setSocketConnectedStatus(boolean isConnected) {
        isSocketConnectedDueToNetwork = isConnected;
    }

    private void pushAppToForground() {
        if (!ProcterAppController.isActivityVisible()) {
            Log.e(TAG, "callFragWithDelay:  available********");
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
      /*  KeyguardManager myKM = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        if (myKM.inKeyguardRestrictedInputMode()) {
            //it is locked
            Log.e(TAG, "callFragWithDelay: locked *******");
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        }*/
    }

    public void sendDataToServer(String data, int i) {

        // Log.e(TAG, "sendDataToServer: StickyService isNetworkConnected"+isNetworkConnected );
        if (NetworkStatus.isNetwrokAvailable(ProcterAppController.getInstance())) {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    dialogFlag = 0;

                }
            }
            if (i == 1) {
                showProgress();
            }

            Log.e(TAG, "sendDataToServer: mWebSocketClient Ref********** " + mWebSocketClient);
            if (mWebSocketClient != null) {
                Log.e(TAG, "sendDataToServer: ***not null");
                Log.e(TAG, "sendDataToServer:Connection STATUS mWebSocketClient.getReadyState()" + mWebSocketClient.getReadyState());
                Log.e(TAG, "sendDataToServer:Connection STATUS" + mWebSocketClient.getConnection().getReadyState());
                if (mWebSocketClient.getConnection().isOpen()) {
                    mWebSocketClient.send(data);
                    Log.e(TAG, "sendDataToServer:  WithoutProgress is open" + data);
                    //    Log.e(TAG, data);

                } else {
                    if (mWebSocketClient.getConnection() != null && mWebSocketClient.getReadyState() == WebSocket.READYSTATE.CLOSED) {
                        connectWebSocket();
                    }

                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("key", "socket_connection_not_established_android_log");
                    } catch (JSONException e) {
                        Log.e(TAG, "sendDataToServer: " + e);
                        e.printStackTrace();
                    }
                    parseDataToMainActivity(obj.toString());
                }

            } else {
                Log.e(TAG, "sendDataToServer: Else Socket Ref is null " + mWebSocketClient);
//                connectWebSocket();
            }
        } else {
            Log.e(TAG, "sendDataToServer: Else NETWORK IS NOT AVAILABLE*******");
            if (dialogFlag == 0) {
                Log.e(TAG, "sendDataToServer: Else network dialogflag" + dialogFlag);
                if (ProcterAppController.isActivityVisible()) {
                    if (dialogFlag == 0) {
                        showInternetErrorDialog();
                    }
                }
            }
        }
    }

    private void parseDataToMainActivity(final String response) {

       /* if (pDialog != null) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }*/
        new Handler(Looper.getMainLooper()).post(() -> {
            try {
                Log.e(TAG, "run: Service ***************" + response);
                Log.e(TAG, "run: " + socketInterface);
                if (socketInterface != null) {
                    socketInterface.socketResponse(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "run: exception service" + e);
            }
        });
    }

    public void showInternetErrorDialog() {

        dialogFlag = 1;
        dialog = new Dialog(ProcterAppController.getInstance());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.setContentView(R.layout.no_internet_layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        final Window dialogWindow = dialog.getWindow();
        final WindowManager.LayoutParams dialogWindowAttributes = dialogWindow.getAttributes();

        // Set fixed width (280dp) and WRAP_CONTENT height
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogWindowAttributes);
        lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280, getResources().getDisplayMetrics());
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);

        // Set to TYPE_SYSTEM_ALERT so that the Service can display it
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialogWindow.setType(WindowManager.LayoutParams.TYPE_TOAST);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialogWindow.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            dialogWindow.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();

     /*   TextView txtNoInternetMsg = (TextView) dialog.findViewById(R.id.txt_no_internet);
        if(txtNoInternetMsg != null){
            txtNoInternetMsg.setText("Please check your internet connection ");
        }*/
        TextView tv_ok = dialog.findViewById(R.id.tv_ok);

        tv_ok.setOnClickListener(v -> {
            dialog.dismiss();
            dialogFlag = 0;
            Log.e(TAG, "onClick: dialog click    " + dialogFlag);
        });

    }

    private void showProgress() {
        // TODO Auto-generated method stub
        //here we set layout of progress dialog
     //   pDialog.show();
       /* pDialog.setContentView(R.layout.dialog_common_progress);
        pDialog.setCancelable(true);
        pDialog.show();*/

    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy! Sticky Service *********");
        super.onDestroy();
    }

    public void disconnectSocket() {
        Log.e(TAG, "disconnectSocket: " + mWebSocketClient);
        if (mWebSocketClient != null) {
            Log.e(TAG, "disconnectSocket: not null group ****");
            mWebSocketClient.close();
            mWebSocketClient = null;
        }

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e(TAG, "onTaskRemoved!******* Sticky Service ***********");
        disconnectSocket();
        stopSelf();
//        Intent broadcastIntent = new Intent("com.bikebot.rider.SocketStickySerice.Restarter");
//        sendBroadcast(broadcastIntent);
    }

    public class LocalBinder extends Binder {
        public SocketStickyService getStickyService() {
            // Return this instance of LocalService so clients can call public methods
            return SocketStickyService.this;
        }
    }
}
