package com.blundell.solidnetworking.service;

import android.os.Message;

/**
 *
 * @author paul.blundell
 *
 * This is the interface stating we are listening to our tasks for a reply
 *
 */
public interface ResponseListener {

	public abstract void handleServiceResponse(Message msg);

}
