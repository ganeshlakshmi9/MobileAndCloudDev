package ncsu.course.android.broadcastchat1.model;

import java.sql.Timestamp;

public class UserMessage {

	private String user;
	private String room;
	private String msg;
	
	private Timestamp ts;
	
	public UserMessage(){
		user = "";
		room = "";
		msg  = "";
	}
	
	public UserMessage(String user, String room, String msg) {
		super();
		this.user = user;
		this.room = room;
		this.msg = msg;
		this.ts = null;
	}
	
	public UserMessage(String user, String room, String msg, Timestamp ts) {
		super();
		this.user = user;
		this.room = room;
		this.msg = msg;
		this.ts = ts;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getRoom() {
		return room;
	}
	
	public void setRoom(String room) {
		this.room = room;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public Timestamp getTs() {
		return ts;
	}

	public void setTs(Timestamp ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		return user+";"+room+";"+msg;
	}
}
