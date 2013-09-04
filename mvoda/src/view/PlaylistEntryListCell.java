package view;

import playlist.PlaylistEntry;
import media.MusicVideo;
import javafx.scene.control.ListCell;

public class PlaylistEntryListCell extends ListCell<PlaylistEntry> {
	
	//static int number = 1;

	@Override protected void updateItem(PlaylistEntry item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
        	 //setText("NUMBER: " + number + item.getFileUNC());
        	//number++;
        	
        	int pos = this.getIndex() + 1;
//        	int pos = item.getPositionInPlaylist();
        	
        	System.out.println("PlaylistEntry Cell: " + "NUMBER: " + pos + "\t" + item.getFileUNC());
        	
            setText("NUMBER " + pos + "\t" + item.getFileUNC());
        }
    }
	
}
