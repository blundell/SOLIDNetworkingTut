package com.blundell.solidnetworking.service.factory;

import com.blundell.solidnetworking.R;
import com.blundell.solidnetworking.service.factory.task.DoSomethingTask;
import com.blundell.solidnetworking.service.factory.task.FireAndForgetTask;
import com.blundell.solidnetworking.util.Log;

import android.os.Message;

/**
 * This is used by our service to return the appropriate task that was asked for
 * @author paul.blundell
 *
 */
public class TaskFactory {

	public Task getTask(Message msg) {
		switch (msg.what) {
		case R.id.task_do_something:
			return new DoSomethingTask(msg);
		case R.id.task_do_fire_and_forget:
			return new FireAndForgetTask(msg);
		default:
			Log.d("No task found");
			return null;
		}
	}

}
