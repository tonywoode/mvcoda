package view;

import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
import playlist.PlaylistEntry;

/**
 * Customises the Playlist List View's list cells, thus each cell will behave as the below
 * @author tony
 *
 */
public class PlaylistEntryListCell extends ListCell<PlaylistEntry> {
	
	/**
	 * Listening on the cell for a change in the position of a chart number, a change in validation status (for which the
	 * cell will change background and text colour), or a double click for which we ask the viewController to produce its
	 * open file dialog
	 */
	@Override protected void updateItem(PlaylistEntry item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
        	
        	/**
        	 * The chart position we want to display
        	 */
        	final int chartPosition = this.getIndex() + 1;
        	this.setTooltip(new Tooltip("Double click to refind files"));
        	System.out.println("PlaylistEntry Cell: " + "Chart Position  " + chartPosition + "\t" + item.getFileUNC());
        	
            setText( "Chart Number " + chartPosition + "\t" + item.getFileUNC() );
            
            final String itemname = item.getFileUNC();
            
            if (this.getItem().getVideo() == null ) { this.setBlendMode(BlendMode.GREEN); }
            if (this.getItem().getVideo() != null ) { this.setBlendMode(null); }
            
           addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent event) {
                    if (event.getClickCount() > 1) {
                        System.out.println("double clicked on " + itemname);                      
                        ViewController.reFindPlaylistEntry(chartPosition - 1); //ie this.getIndex()
                    }
                }
            });
            
            
        }
    }
	
}
