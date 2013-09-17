package media;

import media.types.MediaWriter;
import media.types.StreamCoder;
import playlist.Playlist;

/**
 * Interface for the media emcoder. The encoder should be able to creat a writing container and add audio and video streams to render a playlist
 * The interface decouples and all types have been made generic, using the adapter pattern, for use in other frameworks
 * @author tony
 *
 */
public interface Encoder {

	/**
	 * When passed a playlist, will render the theme that has already been set for the encoder over that playlist
	 * @param playlist a playlist of music videos
	 */
	void render(Playlist playlist);

	/**
	 * Gets a new media writer so that it can start a container to hold media data on disk
	 * @param filepath the file representing the container will have
	 */
	MediaWriter getWriter(String filename);

	/**
	 * Adds a video stream to an instantiated writer so that it can give it an ID, index and hold it in a container
	 * @param writer the writer which is going to have a video stream addded to it
	 */
	void addVideoStreamTo(MediaWriter writer);

	/**
	 * Adds an audio stream to an instantiated writer so that it can give it an ID, index and hold it in a container
	 * @param writer the writer which is going to have a audio stream addded to it
	 * @param audioCodec the audio codec which is going to be used to write
	 */
	void addAudioStreamTo(MediaWriter writer, StreamCoder audioCodec);

}