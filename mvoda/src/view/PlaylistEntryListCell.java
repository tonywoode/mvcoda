package view;

import playlist.PlaylistEntry;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;

public class PlaylistEntryListCell extends ListCell<PlaylistEntry> {
	
	@Override protected void updateItem(PlaylistEntry item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
        	
        	final int pos = this.getIndex();
        	
 
        	System.out.println("PlaylistEntry Cell: " + "NUMBER: " + (pos + 1) + "\t" + item.getFileUNC());
        	
            setText("NUMBER " + (pos + 1) + "\t" + item.getFileUNC());
            
            final String itemname = item.getFileUNC();
            
            if (this.getItem().getVideo() == null ) { this.setBlendMode(BlendMode.GREEN); }
            if (this.getItem().getVideo() != null ) { this.setBlendMode(null); }
            
           addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() > 1) {
                        System.out.println("double clicked on " + itemname);
                       
                        ViewController.reFindPlaylistEntry(pos);
                    }
                }
            });
            
            
        }
    }
	
}
