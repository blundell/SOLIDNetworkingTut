package com.blundell.solidnetworking.service.factory;

import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * A single unit of work that is executed as a task
 * @author paul.blundell
 *
 */
public abstract class Task implements Runnable {

	protected final Message msg;

	public Task(Message msg) {
		this.msg = Message.obtain(msg);
	}

	@Override
	public abstract void run();

	/**
	 * Call this if you want to add data to your message before it is sent back up to your Activity
	 * @param data attach any data to the bundle here
	 */
	protected void setResponseData(Bundle data){
		msg.setData(data);
	}

	/**
	 * Replies to our Service saying this task is completed
	 * @param response The response message to send
	 */
	protected void sendReply() {
		Messenger replyTo = msg.replyTo;
		if (replyTo != null) {
			try {
				replyTo.send(msg);
			} catch (Exception e) { // Mostly RemoteException
				try {
					msg.what = 666; // Some error code
					replyTo.send(msg);
				} catch (RemoteException e1) { /** Nothing we can do */ }
			}
		}
	}
}