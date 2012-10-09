package com.blundell.solidnetworking.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.blundell.solidnetworking.service.factory.Task;
import com.blundell.solidnetworking.service.factory.TaskFactory;
import com.blundell.solidnetworking.util.Log;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;

/**
 * Our service is used to manage the running of tasks. We have a queue of tasks that need to be ran
 * currently this runs the tasks in a linear fashion. Therefore tasks that send replys to the UI could be queued behind fire and forget tasks *hint (no comparison)*
 * @author paul.blundell
 *
 */
public class SolidService extends Service {
	private static final int QUEUE_CAPACITY = 10;
	// A handler for incoming messages from our client
	private Messenger messenger;
	// A pool of threads for the android system to manage our tasks
	private static ThreadPoolExecutor executor;
	// A factory to help us retrieve our tasks
	private static TaskFactory factory;

	@Override
	public void onCreate() {
		super.onCreate();
		messenger = new Messenger(messageHandler);
		executor = new ThreadPoolExecutor(1, 3, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(QUEUE_CAPACITY));
		factory = new TaskFactory();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d("Service was bound");
		return messenger.getBinder();
	}

	protected static Handler messageHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			Task task = factory.getTask(msg);
			if(task != null)
				executor.execute(task);
		};
	};

	@Override
	public void onDestroy() {
		waitForTasksToFinishThenStop();
		messenger = null;
		factory = null;
		super.onDestroy();
	}

	private static void waitForTasksToFinishThenStop() {
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			Log.e("Unfinished tasks", e);
		}
		executor = null;
	}
}