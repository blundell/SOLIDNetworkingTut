package com.blundell.solidnetworking.service;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import com.blundell.solidnetworking.util.Log;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;

/**
 * We deal with calls to the service in a FIFO manner
 *
 * @author Blundell
 */
public abstract class ServiceConnectionManager implements ServiceConnection {
	// The context the service is running in
	private Context context;
	// If the service has been bound yet or not
	private boolean bound;
	// A handler to the service to send messages
	private Messenger service;
	// A queue of tasks that the app is wanting the service to do
	private final LinkedList<Message> msgQueue;
	// This is the listener if the Activity wants to get a response from the task ran by the service
	private final ReplyHandler replyTo;

	public ServiceConnectionManager(Context context, ResponseListener listener) {
		this.context = context;
		replyTo = new ReplyHandler(listener);
		msgQueue = new LinkedList<Message>();
		context.bindService(new Intent(context, SolidService.class), this, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		this.service = new Messenger(service);
        bound = true;

        // If there is a msg waiting to be sent, send after binding
        sendServiceMessage();
	}

	/**
	 * Get a message with the id of the task we want to run
	 * @param taskId
	 * @return
	 */
	protected Message obtainMessage(int taskId){
		Message msg = Message.obtain();
		msg.replyTo = new Messenger(replyTo);
		msg.what 	= taskId;
		return msg;
	}

	protected void addToQueue(Message msg) {
		msgQueue.add(msg);
		if(bound) {
			sendServiceMessage();
		} else {
			Log.d("The service is not bound.");
        	Log.d("Message ID was:" + Log.identifyMessage(context.getResources(), msg));
        	Log.d("Message was added to the queue and service call will be done on next call (when we're bound)");
		}
	}

	private void sendServiceMessage() {
		try {
			while(!msgQueue.isEmpty()){
				Message msg = msgQueue.removeFirst();
				service.send(msg);
				Log.d("Sent the service the msg: "+Log.identifyMessage(context.getResources(), msg));
			}
		} catch (RemoteException e) {
			Log.d("Can't do much about remote exceptions.");
		}
	}

	private static class ReplyHandler extends Handler {
		private final WeakReference<ResponseListener> mListener;

		public ReplyHandler(ResponseListener listener) {
			mListener = new WeakReference<ResponseListener>(listener);
		}

		@Override
		public void handleMessage(Message msg) {
			// We now pass the message onto the Activity if it set a listener (which we are enforcing it does with the BlundellActivity, using the ResponseListener interface)
			if(mListener.get() != null)
				mListener.get().handleServiceResponse(msg);
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		service = null;
        bound = false;
	}

	public void stop(){
		msgQueue.clear();
		context.unbindService(this);
		bound = false;
		context = null;
	}
}