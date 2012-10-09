package com.blundell.solidnetworking.ui;

import com.blundell.solidnetworking.service.ResponseListener;
import com.blundell.solidnetworking.service.ServiceClient;
import com.blundell.solidnetworking.util.Log;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

/**
 * Our 'super' activity, here we can put common code. It is also used to bind to our service,
 * therefore any class that subclasses this class can talk to the service and perform tasks off the UI thread.
 * @author paul.blundell
 *
 */
public abstract class BlundellActivity extends Activity implements ResponseListener {

	private ServiceClient serviceClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lazyInitService();
	}

	@Override
	protected void onResume() {
		super.onResume();
		lazyInitService();
	}

    protected ServiceClient getServiceClient() {
    	lazyInitService();
        Log.d("Returning service from super activity");
        return serviceClient;
    }

	private void lazyInitService() {
		if(serviceClient == null) {
			Log.d("Created new service");
			serviceClient = new ServiceClient(this, this);
		}
	}

	public void popToast(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public void popBurntToast(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	/** Does nothing here */
	@Override
	public void handleServiceResponse(Message msg){
		// Implemented in sub classes
		// If you wanted you could check for errors here and shown a generic dialog error message
	}

	@Override
	protected void onStop() {
		stopService();
		super.onStop();
	}

	private void stopService() {
		if(serviceClient != null){
			Log.d("super activity onStop - stopping service");
			serviceClient.stop();
			serviceClient = null;
		}
	}
}