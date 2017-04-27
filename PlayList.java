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
	private final Song NOSONG = new Song("Nothing","Nobody",0,"Nowhere"); //for when no other song is selected
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
			Song nothing = NOSONG;
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
	}
	
	
	@Override
	public void loadFromFile(File file) // called when creating a new playlist
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
	public Song removeSong(int index) //TODO: Consider a dialogue confirmation box. Also, consider an error dialogue box if there's no song to remove
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
	 * @return totalTime The total time of all songs in the play list
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
	 * 
	 * @return totalTime the total time in minutes
	 */
	public double getTotalPlayTimeInMinutes()
	{
		double totalTime = 0;
		for (Song song : this.songList)
		{
			totalTime += song.getPlayTime();
		}
		totalTime = totalTime/60;
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
			theSong.play();
			this.playing = theSong;
		}
		else
		{
			//does nothing, or throws an exception?
		}
	}
	
	@Override
	public void playSong(Song song) 
	{
		if (this.songList.contains(song))
		{
			song.play();
			this.playing = song;
		}	
	}
	
	@Override
	public void stop() 
	{
		if(playing != null)
		{
			playing.stop();
			playing = null;	
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
				
				theInfo  = String.format("The average play time is: "+"%.2f"+" seconds\n", avgPlayTime);
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
		Song[] temp;
		
		if (index==0) //when you're at the top of the list already, "up" will set the last item in the array to the chosen index
		{
			temp = this.getSongArray();
			temp[songList.size()-1] = songList.get(index);
			temp[index] = songList.get(songList.size()-1);
			index = songList.size()-1;
		}
		else // anywhere else in the list
		{
			temp = this.getSongArray();
			temp[index-1] = songList.get(index);
			temp[index] = songList.get(index-1);
			index = index-1;
		}
		
		for (int i = 0; i<songList.size();i++)
		{
			songList.set(i, temp[i]);
		}
		
		return index;
	}

	@Override
	public int moveDown(int index) 
	{
		Song[] temp;
		
		if(index == songList.size()-1) //for when the index is already the last; loops it around to the front
		{
			temp = this.getSongArray();
			temp[0] = songList.get(index);
			temp[index] = songList.get(0);
			index = 0;
		}
		else //anywhere else in the list
		{
			temp = this.getSongArray();
			temp[index+1] = songList.get(index);
			temp[index] = songList.get(index+1);
			index = index+1;
		}
		
		for (int i = 0; i<songList.size();i++)
		{
			songList.set(i, temp[i]);
		}
		
		return index; 
	}

	@Override
	public Song[][] getSongSquare() 
	{
		int index = 0;
		int dimentions = (int) Math.ceil(Math.sqrt(this.songList.size()));
		
		Song[][] songSquare = new Song[dimentions][dimentions];	
		
		for (int row=0;row<songSquare.length;row++) //row
		{
			for(int col=0;col<songSquare[row].length;col++) //column
			{
				if (index > this.songList.size()-1)
				{
					index = 0;
				}
				songSquare[row][col] = this.songList.get(index);
				index++;
			}
		}
		return  songSquare;
	}
	
	
}