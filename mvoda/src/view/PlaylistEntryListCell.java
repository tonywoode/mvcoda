package view;

import playlist.PlaylistEntry;
import media.MusicVideo;
import javafx.scene.control.ListCell;

public class PlaylistEntryListCell extends ListCell<PlaylistEntry> {
	
	//static int number = 1;

	@Override protected void updateItem(PlaylistEntry item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
        	 //setText("NUMBER: " + number + item.getVideo().getFileUNC());
        	//number++;
            setText("NUMBER " + item.getPositionInPlaylist() + "\t" + item.getVideo().getFileUNC());
        }
    }
	
}
