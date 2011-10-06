package ncsu.course.android.broadcastchat1.services;

import android.os.Binder;

public class ServiceBinder extends Binder {
	
	private BroadcastChatService service;

	public ServiceBinder(BroadcastChatService service){
		this.service = service;
	}
	
	public BroadcastChatService getService(){
		return service;
	}
}
