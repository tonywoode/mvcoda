package view;

import playlist.PlaylistEntry;
import media.MusicVideo;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;

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
        	item.setPositionInPlaylist(pos);//I don't think this does anything....
        	
            setText("NUMBER " + pos + "\t" + item.getFileUNC());
            
            final String itemname = item.getFileUNC();
            
            if (this.getItem().getFileUNC().equals("Not Found") ) { this.setBlendMode(BlendMode.GREEN); }
            
           addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() > 1) {
                        System.out.println("double clicked! on " + itemname);
                        //TableCell c = (TableCell) event.getSource();
                       // System.out.println("Cell text: " + c.getText());
                    }
                }
            });
            
            
        }
    }
	
}
