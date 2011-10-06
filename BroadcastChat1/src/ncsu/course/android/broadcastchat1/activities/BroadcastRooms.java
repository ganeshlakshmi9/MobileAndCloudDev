package ncsu.course.android.broadcastchat1.activities;

import java.util.ArrayList;

import ncsu.course.android.broadcastchat1.R;
import ncsu.sourse.android.broadcastchat1.dao.BroadcastDbAdapter;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BroadcastRooms extends ListActivity{

	private BroadcastDbAdapter mDbAdapter; 
	
	private static final int DELETE_ID = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDbAdapter = new BroadcastDbAdapter(this);
        mDbAdapter.open();
		
		setContentView(R.layout.room_list);
		setTitle(R.string.app_name);
		
		fillData();
	}
	
	private void fillData(){
		Cursor roomsCursor = mDbAdapter.fetchAllRooms();
        startManagingCursor(roomsCursor);
        
        roomsCursor.moveToFirst();
        
        ArrayList<String> list = new ArrayList<String>();
        while(roomsCursor.moveToNext()){
        	int i = roomsCursor.getColumnIndexOrThrow(BroadcastDbAdapter.KEY_MESSAGE_ROOM);
        	list.add(roomsCursor.getString(i));
        }
        
		 // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{BroadcastDbAdapter.KEY_MESSAGE_ROOM};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.layout.rooms_row};
		
//		SimpleCursorAdapter roomsAdapter = new SimpleCursorAdapter(this, R.layout.rooms_row, roomsCursor, from, to);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.rooms_row, list));
	}
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.contextmenu_delete_room);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
//                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                mDbAdapter.deleteRoom(item.toString());
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        Toast.makeText(getApplicationContext(), ((TextView) v).getText(),
                Toast.LENGTH_SHORT).show();
//        Intent i = new Intent(this, BroadcastChat.class);
//        i.putExtra(NotesDbAdapter.KEY_ROWID, id);
//        startActivity(i);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	mDbAdapter.close();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	mDbAdapter.open();
    }
}
