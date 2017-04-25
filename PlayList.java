import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This <code>PlayList</code> class will create and represent PlayList objects
 * using an array list to keep track of a user's songs.
 * 
 *@author Dominick Edmonds
 */

public class PlayList implements MyTunesPlayListInterface
{

	private ArrayList<Song> songList;
	private String playlistName; // name of the play list
	private Song playing; //The song that is currently playing
	

	/**
	 * Constructor: Builds a Play List, only needs the name, creates a new array list and
	 * sets the currently playing song to null upon initialization. 
	 * 
	 * @param name the PlayList's name
	 */
	
	public PlayList(String name)
	{
		this.playlistName = name;
		this.playing = null;
		this.songList = new ArrayList<Song>();
	}
	
	/**
	 * Getter method play list name.
	 * 
	 * @return the name of the play list
	 */
	public String getName()
	{
		return playlistName;
	}
	
	/**
	 * Getter methods for the currently playing song.
	 * 
	 * @return playing the song that's currently playing 
	 * @return noSong a "blank" song, in case there isn't a song playing yet. Nothing by Nobody is 0 seconds long, and is located nowhere.
	 */
	public Song getPlaying()
	{
		if (this.playing != null)
		{
		return this.playing;
		}
		else
		{
			Song nothing = new Song("Nothing","Nobody",0,"Nowhere");
			return nothing;
		}
	}

	/**
	 * Getter methods for the songList.
	 * 
	 * @return The song's name
	 */
	public ArrayList<Song> getSongList()
	{
		return this.songList;
	}
	
	/**
	 * Setter method for name
	 * @param name changes the name of the current play list
	 */
	public void setName(String name)
	{
		this.playlistName = name;
	}
	
	/**
	 * Add songs to the PlayList
	 * 
	 * @param newSong the desired song object to be added to the end of the list
	 */
	public void addSong(Song newSong)
	{
		this.songList.add(newSong);
		this.sortList(); //TODO: Do we want this still?
	}
	
	@Override
	public void loadFromFile(File file) // called when an file item is selected with "add new song" button's file chooser dialogue, and the user inputs proper information into the song creation dialogue box
	{
		try {
			Scanner scan = new Scanner(file);
			while(scan.hasNextLine()) {
				String title = scan.nextLine().trim();
				String artist = scan.nextLine().trim();
				String playtime = scan.nextLine().trim();
				String songPath = scan.nextLine().trim();

				int colon = playtime.indexOf(':');
				int minutes = Integer.parseInt(playtime.substring(0, colon));
				int seconds = Integer.parseInt(playtime.substring(colon+1));
				int playtimesecs = (minutes * 60) + seconds;

				Song song = new Song(title, artist, playtimesecs, songPath);
				songList.add(song);
			}
			scan.close();
		} catch (FileNotFoundException e) {
			System.err.println("Failed to load playlist. " + e.getMessage());
		}
		
	}
	
	/**
	*Removes songs from the PlayList by taking an index value as input, 
	*returns null if index is out of range.
	*@param index The index of the song desired for removal
	*@return the song to be removed- null if invalid
	*/
	public Song removeSong(int index)
	{
		Song toRemove = null;
		if (index < this.getNumSongs() && index >= 0)
		{
			toRemove = this.songList.remove(index);
		}
		return toRemove;
	}
	
	/**
	 * Gets the number of songs in the PlayList as an integer
	 * @return the number of songs in the list
	 */
	public int getNumSongs()
	{
		return this.songList.size();
	}
	
	/**
	 * Gets the song at the given index, null if index is out of range
	 * @param index The index of the song desired to get
	 * @return the song to be got
	 */
	public Song getSong(int index)
	{
		Song theSong = null;
		if (index < this.getNumSongs() && index >= 0)
		{
			theSong = this.songList.get(index);
		}
		//If false, it does nothing, keeping theSong object set to null.
		return theSong;
	}
	
	
	/**
	 * Gets the total play time in seconds of the play list
	 * and returns the value.
	 * 
	 * @return The total time of all songs in the play list
	 */
	public int getTotalPlayTime()
	{
		int totalTime = 0;
		for (Song song : this.songList)
		{
			totalTime += song.getPlayTime();
		}
		return totalTime;
	}
	
	/**
	 * Plays the song at the selected index
	 * 
	 * @param index Index of the song desired to play
	 */
	public void playSong(int index)
	{
		if (index <= this.songList.size() && index >= 0)
		{
			Song theSong = this.songList.get(index);
			this.playing = theSong;
		}
		else
		{
			//does nothing, or throws an exception
		}
	}
	
	@Override
	public void playSong(Song song) 
	{
		if (this.songList.contains(song))
		{
			this.playing = song;
		}
		
	}
	
	/**
	 * Returns a formated string of info about each song in the playlist,
	 * includes average play time, total time, shortest and longest songs,
	 * and then formats them to adhere to project specifications.
	 * 
	 * @return A representation of the Playlist's contents with some extra info
	 */
	public String getInfo()                      
	{                                            
		String theInfo = new String();           
		int index = 0;                           
		if (this.songList.size() > 0)
		{
			if (index < this.songList.size() && index >= 0) //Checks if we're out of range
			{
				Song longest = this.songList.get(this.songList.size()-1);
				Song shortest = this.songList.get(0);
				double avgPlayTime = (double)this.getTotalPlayTime()/ this.songList.size();
				
				theInfo  = String.format("The average play time is: "+"%.2f"+" seconds\n", avgPlayTime); // TODO: Decimal format?
				theInfo += String.format("The shortest song is: "+"%s\n",shortest.toString());
				theInfo += String.format("The longest song is: "+"%s\n",longest.toString());
				theInfo += String.format("Total play time: "+"%d seconds\n", this.getTotalPlayTime());
			}
		}
		else
		{
			theInfo = "There are no songs.";
		}
		return theInfo;
	}
	
	/**
	 *  Prints out a string representing the PlayList object, containing the name,
	 *  number of songs, and all songs in the play list.
	 *  
	 *  @return A formated string representing the play list
	 */
	public String toString()
	{
		String playListString = new String();
		playListString = "------------------\n"
						+this.getName()+"("+this.getNumSongs()+" songs)\n"
						+"------------------\n";
		int index = 0;
		if (this.songList.size() > 0)
		{	
			for(Song song : this.songList)
			{
				playListString += "(";
				playListString += String.format("%d", index) + ")" + song.toString() + "\n";
				index++;
			}
		}
		else
		{
			playListString += "There are no songs.";
		}
		playListString += "------------------\n";		
		return playListString;
	}
	
	/**
	 * Method to be used to sort the songs, so later we can pull out a shortest
	 * and a longest in the simplest manor. Will be updated every time we add a song
	 * shortest index = 0, longest = the array's last entry
	 */
	public void sortList()
	{
		Boolean isSorted = false; 
		Song temp = null;
		
		while (!isSorted)
		{
			isSorted = true;
			
			for (int i=0;i<getNumSongs()-1;i++)
			{
				if ( this.getSong(i).getPlayTime() > this.getSong(i+1).getPlayTime())
				{
					temp = this.songList.get(i);
					this.songList.set(i, this.songList.get(i+1));
					this.songList.set(i+1, temp);
					isSorted=false;
				}
			}
		}
	}


	@Override
	public void stop() 
	{
		playing = null;	
	}

	@Override
	public Song[] getSongArray() 
	{
		Song[] songArray = new Song[songList.size()];
		
		for (int i = 0; i<songArray.length;i++)
		{
			songArray[i] = (songList.get(i));
		}
		
		return songArray;
	}

	@Override
	public int moveUp(int index) 
	{
		// TODO Auto-generated method stub
		return index;
	}

	@Override
	public int moveDown(int index) 
	{
		// TODO Auto-generated method stub
		return index;
	}

	@Override
	public Song[][] getSongSquare() 
	{
		//TODO: SEE LAB11 AND LAB12, LOOK AT PHOTO SQUARE
		return null;
	}
	
	
}
