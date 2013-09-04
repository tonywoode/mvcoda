package controllers;

import lombok.Setter;
import lombok.Getter;
import playlist.Playlist;
import playlist.PlaylistEntry;
import view.ViewControllerListener;
import view.ViewController;

public class MainScreenController implements ViewControllerListener {

	private Playlist playlist;
	
	@Getter @Setter ViewController view;
	
	public void onNewTrackAvailable(String name) {
		/*PlaylistEntry pe = new PlaylistEntry(null);
		pe.setTrackName(name);
		playlist.setNextEntry(pe);*/
		
	}
	
}
