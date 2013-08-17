package view;

import playlist.PlaylistEntry;
import media.MusicVideo;
import javafx.scene.control.ListCell;

public class PlaylistEntryListCell extends ListCell<PlaylistEntry> {

	@Override protected void updateItem(PlaylistEntry item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(item.getVideo().getFileUNC());
        }
    }
	
}
