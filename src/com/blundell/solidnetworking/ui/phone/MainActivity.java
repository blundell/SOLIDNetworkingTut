package com.blundell.solidnetworking.ui.phone;

import com.blundell.solidnetworking.FromXML;
import com.blundell.solidnetworking.R;
import com.blundell.solidnetworking.service.factory.task.DoSomethingTask;
import com.blundell.solidnetworking.ui.BlundellActivity;
import com.blundell.solidnetworking.util.Log;

import android.os.Bundle;
import android.os.Message;
import android.view.View;

/**
 *
 * @author paul.blundell
 *
 */
public class MainActivity extends BlundellActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @FromXML
    public void doFireAndForgetTask(View button){
    	getServiceClient().doFireAndForgetTask();
    }

    @FromXML
    public void doSomethingTask(View button){
    	getServiceClient().doSomething("I sent this");
    }

    @Override
    public void handleServiceResponse(Message msg) {
    	dealWithResponse(msg.what, msg.getData());
    }

	private void dealWithResponse(int responseId, Bundle data) {
		switch(responseId){
    	case R.id.task_do_something:
			dealWithDoSomethingResult(data);
    		break;
    	default:
    		Log.v("Received service reply that wasn't handled");
    	}
	}

	private void dealWithDoSomethingResult(Bundle data) {
		String string = data.getString(DoSomethingTask.SOME_STRING);

		Log.d("Received reply from the task. The var was: "+ string);

		popToast("Do Something Task Complete");
	}
}