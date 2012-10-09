package com.blundell.solidnetworking.service;

import com.blundell.solidnetworking.R;
import com.blundell.solidnetworking.service.factory.task.DoSomethingTask;

import android.content.Context;
import android.os.Message;

/**
 * A client for our service, here we create the 'interface' that our activity's can talk to.
 * @author paul.blundell
 *
 */
public class ServiceClient extends ServiceConnectionManager {

	public ServiceClient(Context context, ResponseListener listener) {
		super(context, listener);
	}

	public void doSomething(String input){
		Message msg = obtainMessage(R.id.task_do_something);
		msg.getData().putString(DoSomethingTask.INPUT_VAR, input);
		addToQueue(msg);
	}

	public void doFireAndForgetTask() {
		Message msg = obtainMessage(R.id.task_do_fire_and_forget);
		addToQueue(msg);
	}

}