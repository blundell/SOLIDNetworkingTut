package com.blundell.solidnetworking.service.factory.task;

import com.blundell.solidnetworking.service.factory.Task;
import com.blundell.solidnetworking.util.Log;

import android.os.Bundle;
import android.os.Message;

/**
 * @author paul.blundell
 * Here you can do networking or other long running 'tasks' that do not belong on the main thread
 */
public class DoSomethingTask extends Task {
	public static final String SOME_STRING = "someString";
	public static final String INPUT_VAR = "someInputVar";

	public DoSomethingTask(Message msg) {
		super(msg);
	}

	@Override
	public void run() {
		// Do some work
		Log.i("Hi, I'm on another thread");

		Log.i("I received the variable: " + msg.getData().getString(INPUT_VAR));

		// Set the response - according to the work done
		Bundle b = new Bundle();
		b.putString(SOME_STRING, "test string in bundle, created in task - sent back to activity (responselistener)");
		setResponseData(b);

		// Finished so send reply
		sendReply();
	}

}
