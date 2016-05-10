/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maharjan411.tictactoe.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.maharjan411.tictactoe.R;
import com.maharjan411.tictactoe.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class BluetoothTwoPlayerActivity extends AppCompatActivity {

	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	private static final int REQUEST_ENABLE_BT = 3;
	private static final String TAG = BluetoothTwoPlayerActivity.class.getSimpleName();


	private String connectedDeviceName = null;

	private StringBuffer outStringBuffer;
	private BluetoothAdapter bluetoothAdapter = null;
	private BluetoothService chatService = null;

	private Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					setStatus(getString(R.string.title_connected_to,
							connectedDeviceName));
					break;
				case BluetoothService.STATE_CONNECTING:
					setStatus(R.string.title_connecting);
					break;
				case BluetoothService.STATE_LISTEN:
				case BluetoothService.STATE_NONE:
					setStatus(R.string.title_not_connected);
					break;
				}
				break;
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;

				String writeMessage = new String(writeBuf);


				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;

				String readMessage = new String(readBuf, 0, msg.arg1);

				LogUtil.i(TAG," read from bluetooth "+readMessage);

				BluetoothMsg bluetoothMsg=getMsgFromBluetooth(readMessage);

				LogUtil.i(TAG,bluetoothMsg.toString());

				if(bluetoothMsg.restart){
					game.start();
				}else {
					game.putMark(bluetoothMsg.x, bluetoothMsg.y, bluetoothMsg.turn, bluetoothMsg.player);
				}
				break;
			case MESSAGE_DEVICE_NAME:

				connectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"Connected to " + connectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			}
			return false;
		}
	});

	BluetoothGame game;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		game=new BluetoothGame(this);
		game.setGameMode(2);
		game.setListener(new BluetoothGame.IMessage() {
			@Override
			public void onTouchDetected(boolean turn, int x, int y, int player) {
				LogUtil.i(TAG," turn "+turn+" x "+x+" y "+y +" player "+player);
				JSONObject responseObj=generateMsg(turn,x,y,player,false);
				sendMessage(responseObj.toString());
			}
		});
		setContentView(game);

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (bluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}
	}


	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE_SECURE:
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data, true);
			}
			break;
		case REQUEST_CONNECT_DEVICE_INSECURE:
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data, false);
			}
			break;
		case REQUEST_ENABLE_BT:
			if (resultCode == Activity.RESULT_OK) {
				setupChat();
			} else {
				Toast.makeText(this, R.string.bt_not_enabled_leaving,
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	private void connectDevice(Intent data, boolean secure) {
		String address = data.getExtras().getString(
				DeviceListActivity.DEVICE_ADDRESS);
		BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
		chatService.connect(device, secure);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.bluetooth_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent serverIntent = null;
		switch (item.getItemId()) {
		case R.id.secure_connect_scan:
			serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
			return true;
		case R.id.insecure_connect_scan:
			serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent,
					REQUEST_CONNECT_DEVICE_INSECURE);
			return true;
		case R.id.discoverable:
			ensureDiscoverable();
			return true;

			case R.id.restart:
				game.start();
				game.setTurn(true);
				JSONObject responseObj=generateMsg(false,0,0,1,true);
				sendMessage(responseObj.toString());
				return true;
		}
		return false;
	}

	private void ensureDiscoverable() {
		if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	private void sendMessage(String message) {
		if (chatService.getState() != BluetoothService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}

		if (message.length() > 0) {
			byte[] send = message.getBytes();
			chatService.write(send);

			outStringBuffer.setLength(0);
		}
	}


	private final void setStatus(int resId) {
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setSubtitle(resId);
	}

	private final void setStatus(CharSequence subTitle) {
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setSubtitle(subTitle);
	}

	private void setupChat() {
		chatService = new BluetoothService(this, handler);
		outStringBuffer = new StringBuffer("");
	}

	@Override
	public void onStart() {
		super.onStart();

		if (!bluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		} else {
			if (chatService == null)
				setupChat();
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();

		if (chatService != null) {
			if (chatService.getState() == BluetoothService.STATE_NONE) {
				chatService.start();
			}
		}
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (chatService != null)
			chatService.stop();
	}


	private BluetoothMsg getMsgFromBluetooth(String msg){

		BluetoothMsg bluetoothMsg=new BluetoothMsg();
		try {
			JSONObject obj=new JSONObject(msg);
			bluetoothMsg.setRestart(obj.getBoolean("restart"));
			bluetoothMsg.setTurn(obj.getBoolean("turn"));
			bluetoothMsg.setPlayer(obj.getInt("player"));
			bluetoothMsg.setX(obj.getInt("x"));
			bluetoothMsg.setY(obj.getInt("y"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return bluetoothMsg;

	}

	private JSONObject generateMsg(boolean turn, int x, int y, int player,boolean restart){
		JSONObject obj=new JSONObject();
		try {
			obj.put("restart",restart);
			obj.put("turn",turn);
			obj.put("x",x);
			obj.put("y",y);
			obj.put("player",player);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;

	}

}
