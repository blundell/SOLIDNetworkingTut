package com.blundell.solidnetworking.service.factory.task;

import com.blundell.solidnetworking.service.factory.Task;
import com.blundell.solidnetworking.util.Log;

import android.os.Message;

/**
 * @author paul.blundell
 * This is run on a background thread, but does not send a response back to whoever started it
 */
public class FireAndForgetTask extends Task {

	public FireAndForgetTask(Message msg) {
		super(msg);
	}

	@Override
	public void run() {
		Log.i("I do some work but don't send a reply. This is good for things like sending statistics to a server (analytics).");
	}
}