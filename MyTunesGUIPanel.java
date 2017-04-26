import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 
 * @author Dominick Edmonds
 */

@SuppressWarnings("serial")
public class MyTunesGUIPanel extends JPanel
{
	final String pattern = "###,###.##";
	final DecimalFormat DFORMAT = new DecimalFormat(pattern);
	
	ImageIcon //sets up icons we use
	prevIcon = new ImageIcon("images/media-skip-backward-48.gif"),
	playIcon = new ImageIcon("images/play-48.gif"),
	stopIcon = new ImageIcon("images/stop-48.gif"),
	nextIcon = new ImageIcon("images/media-skip-forward-48.gif");
	
	PlayList playlist;
	JList<Song> playlistJList;
	JButton[][] heatmap;
	Song[] songs;
	JLabel nowPlayingLabel;
	String numSongsString,timeInMinutesString;
	JButton songUpButton,songDownButton,addSongButton,removeSongButton,nextSongButton,prevSongButton,playStopSongButton;
	

	
	/**
	 * Main constructor. This will set up the main panel frame to be used in MyTunesGUI.java.
	 */
	public MyTunesGUIPanel()
	{
		playlist = new PlayList("Project 5 Mixtape");
		playlist.loadFromFile(new File("playlist.txt"));
		ButtonListener buttonListener = new ButtonListener();
		BorderLayout mainLayout = new BorderLayout();
		mainLayout.setHgap(10); 
		setLayout(mainLayout); 
		
		// left (border layout west)
		JPanel leftSide = new JPanel();
		leftSide.setLayout(new BoxLayout(leftSide,BoxLayout.Y_AXIS));
		leftSide.setPreferredSize(new Dimension(600,0));
		
		JPanel playlistLablePanel = new JPanel();
		playlistLablePanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JLabel playlistNameLabel = new JLabel(playlist.getName()); //Displays the loaded playlist's name
		playlistNameLabel.setFont(new Font(playlistNameLabel.getFont().getFontName(),Font.ITALIC,35)); 
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.ipadx = 20;
		playlistLablePanel.add(playlistNameLabel,c);
		
		numSongsString = new String(playlist.getNumSongs()+" songs"); 
		JLabel playlistSongCountLabel = new JLabel(numSongsString); //Displays the loaded playlist's song count
		playlistSongCountLabel.setFont(new Font(playlistSongCountLabel.getFont().getFontName(),Font.PLAIN,15));
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		playlistLablePanel.add(playlistSongCountLabel,c);
		
		
		JLabel playlistSpacingLabel = new JLabel("=============="); //Displays a spacing label
		playlistSpacingLabel.setFont(new Font(playlistSpacingLabel.getFont().getFontName(),Font.BOLD,20));
		c.gridx = 1;
		c.gridy = 1;
		playlistSpacingLabel.setAlignmentX(CENTER_ALIGNMENT);
		playlistLablePanel.add(playlistSpacingLabel,c);
		//playlist.getTotalPlayTimeInMinutes()
		timeInMinutesString = new String(DFORMAT.format(playlist.getTotalPlayTimeInMinutes())+" minutes"); //TODO: Update when adding/removing songs, make instanced
		JLabel playlistTimeLabel = new JLabel(timeInMinutesString); //Displays the loaded playlist's time
		playlistTimeLabel.setFont(new Font(playlistTimeLabel.getFont().getFontName(),Font.PLAIN,15));
		c.gridx = 2;
		c.gridy = 1;
		playlistLablePanel.add(playlistTimeLabel,c);
		
		leftSide.add(playlistLablePanel);
		
		
		
		//play list control panel
		JPanel playlistControlPanel = new JPanel();
		playlistControlPanel.setLayout(new BoxLayout(playlistControlPanel,BoxLayout.Y_AXIS));
		playlistControlPanel.setPreferredSize(new Dimension(0,500));
		
		
		//JList showing the songs in the play list
		playlistJList = new JList<Song>(playlist.getSongArray());
		playlistJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		playlistJList.setFont(new Font("monospaced", Font.PLAIN,12));
		playlistJList.addListSelectionListener(new PlaylistListListener());
		JScrollPane playlistScrollPane = new JScrollPane(playlistJList); 
		playlistScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		playlistControlPanel.add(playlistScrollPane); 
		
		System.out.println("Debug selected initial index: " + playlistJList.getSelectedIndex());
		
		JPanel playlistControlButtonPanel = new JPanel();
		playlistControlButtonPanel.setLayout(new BoxLayout(playlistControlButtonPanel,BoxLayout.X_AXIS));
		songUpButton = new JButton(new ImageIcon("images/move-up-16.gif"));
		songUpButton.addActionListener(buttonListener);
		songDownButton = new JButton(new ImageIcon("images/move-down-16.gif"));
		songDownButton.addActionListener(buttonListener);
		addSongButton = new JButton("add song");
		addSongButton.addActionListener(buttonListener);
		removeSongButton = new JButton("remove song");
		removeSongButton.addActionListener(buttonListener);
		
		playlistControlButtonPanel.add(songUpButton);
		playlistControlButtonPanel.add(songDownButton);
		playlistControlButtonPanel.add(addSongButton);
		playlistControlButtonPanel.add(removeSongButton);
		playlistControlButtonPanel.setBorder(BorderFactory.createTitledBorder("Playlist Controls"));
		playlistControlPanel.add(playlistControlButtonPanel);
		
		leftSide.add(playlistControlPanel);
		add(leftSide, BorderLayout.WEST);
	
		
		
		// right (border layout center)
		JPanel rightSide = new JPanel();
		rightSide.setLayout(new BoxLayout(rightSide,BoxLayout.Y_AXIS));
		
		//heat map panel
		JPanel heatmapPanel = new JPanel();
		heatmap = new JButton[5][5]; //TODO: Change the 5's to get the "square length" of the array list.
		heatmapPanel.setLayout(new GridLayout(heatmap.length,heatmap.length)); //TODO: Change the 5's to get the "square length" of the array list.
		heatmapPanel.setPreferredSize(new Dimension(400,400));
		
		for (int row=0; row<heatmap.length;row++ )
		{
			for (int col = 0;col<heatmap.length;col++)
			{
				JButton heatmapButton = new JButton("TestHM"+(row+1));
				//heatmapButton.setPreferredSize(new Dimension(10,10));//TODO: Get buttons to be square? 
				heatmapPanel.add(heatmapButton);
			}
		}
		
		
		rightSide.add(heatmapPanel);
		
		//playing song control panel
		JPanel playingSongPanel = new JPanel();
		playingSongPanel.setLayout(new BoxLayout(playingSongPanel, BoxLayout.Y_AXIS));
		
		nowPlayingLabel = new JLabel("\""+playlist.getPlaying().getTitle() +"\" by "+ playlist.getPlaying().getArtist()); //current playing song label; title border "now playing"
		nowPlayingLabel.setFont(new Font(nowPlayingLabel.getFont().getFontName(),Font.PLAIN,25));
		nowPlayingLabel.setAlignmentX(CENTER_ALIGNMENT);
		playingSongPanel.add(nowPlayingLabel);
		
		
		JPanel songControlButtonPanel = new JPanel(); //playing songs button panel
		prevSongButton = new JButton(prevIcon);
		prevSongButton.addActionListener(buttonListener);
		prevSongButton.setEnabled(false);
		playStopSongButton = new JButton(playIcon);
		playStopSongButton.addActionListener(buttonListener);
		nextSongButton = new JButton(nextIcon);
		nextSongButton.addActionListener(buttonListener);
		nextSongButton.setEnabled(false);
		
		songControlButtonPanel.add(prevSongButton);
		
		songControlButtonPanel.add(playStopSongButton);
		songControlButtonPanel.add(nextSongButton);
		songControlButtonPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		playingSongPanel.add(songControlButtonPanel);
		playingSongPanel.setBorder(BorderFactory.createTitledBorder("Now playing"));
		rightSide.add(playingSongPanel);
		
		add(rightSide, BorderLayout.CENTER);
	}
	
	//TODO: Change handlers & other required private methods
	
	/**
	 * This method will be called any time the selected song is changed, either by the list selector, the next or previous buttons,
	 * or the heat map buttons.
	 * 
	 * @author Dominick Edmonds
	 */
	private void updatePlayingSong() //TODO: Timer, start just after the  .playSong bit. Calls for the song to stop once it fires
	{
		playlist.getPlaying().stop();
		playlist.playSong(playlistJList.getSelectedIndex());
		playStopSongButton.setIcon(stopIcon);
		nowPlayingLabel.setText("\""+playlist.getPlaying().getTitle() +"\" by "+ playlist.getPlaying().getArtist());
		nextSongButton.setEnabled(true);
		prevSongButton.setEnabled(true);
		System.out.println("DEBUG PLAYS: " + playlist.getPlaying().getPlayCount()); //TODO: Replace me with heat map color changing
	}
	
	/**
	 * Used to refresh the playlist JList if either a song is added, removed, or moved.
	 * It will also update the heatmap buttons, ensuring everything is in order TODO finish me
	 * Additionally, it will check to see if the length of the heatmap's sides can be changed. TODO finish me
	 */
	private void syncPlaylistJList()
	{
		playlistJList.setListData(playlist.getSongArray());
	}
	
	/**
	 * Updates total time and number of songs labels when adding/removing songs to the play list.
	 * 
	 * TODO Finishe me boii
	 */
	private void syncPlaylistStatLables() //TODO You are here
	{
		
	}
	
	/**
	 * This listener is specifically for the heat map section
	 */
	
	/**
	 * This listener will listen for all the buttons on the panel, preforming actions accordingly.
	 * 
	 * @author Dominick Edmonds
	 */														//TODO: Look at lab 11/12 to see how to get heatmap[][] buttons to work
	private class ButtonListener implements ActionListener  //TODO: Maybe use eventActionCommands and add additional button functionality, like pressing kbd spacebar will do the same a play/pause button
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (e.getSource() == songUpButton) //Moves the song UP one in the list, as in -1 in the array refreshes the JList
			{
				System.out.println("songUp: " +playlistJList.getSelectedIndex() +" to " + (playlistJList.getSelectedIndex()-1));
				int targetIndex = playlist.moveUp(playlistJList.getSelectedIndex());
				syncPlaylistJList();
				playlistJList.setSelectedIndex(targetIndex);
			}
			
			if (e.getSource() == songDownButton) //Moves the song DOWN one in the list, as in +1 in the array, refreshes the JList TODO See lab 11/12
			{
				System.out.println("songDown: "+playlistJList.getSelectedIndex()+" to " + (playlistJList.getSelectedIndex()+1));
				int targetIndex = playlist.moveDown(playlistJList.getSelectedIndex());
				syncPlaylistJList();
				playlistJList.setSelectedIndex(targetIndex);
			}
			
			if (e.getSource() == addSongButton) //opens a file browser and ADDS a new song to the list at the end, refreshes the jlist
			{
				System.out.println("addSong");
				//add song
				//refresh jlist
				//refresh playstat labels
				//refresh heatmap
			}
			
			if (e.getSource() == removeSongButton) //opens a dialogue confirmation box, "yes" will delete the currently selected song. Refreshes the jlist
			{
				System.out.println("removeSong");
				//remove song
				//refresh jlist
				//refresh playstat labels
				//refresh heatmap
			}
			
			if (e.getSource() == nextSongButton) //takes the selected jlist item and goes to the NEXT song, looping if at either end, going to index 0 if none are selected, and begins to play the new song.
			{
				int index = playlistJList.getSelectedIndex();
				System.out.println("nextSong");
				index++;
				if (index > playlist.getSongArray().length-1)
				{
					index = 0;
				}
				
				playlistJList.setSelectedIndex(index);
				updatePlayingSong();
			}
			
			if (e.getSource() == prevSongButton) //takes the selected jlist item and goes to the PREVIOUS song, looping if at either end, going to index 0 if none are selected, and begins to play the new song.
			{
				int index = playlistJList.getSelectedIndex();
				System.out.println("prevSong");
				index--;
				if (index < 0)
				{
					index = playlist.getSongArray().length-1;
				}
				
				playlistJList.setSelectedIndex(index);
				updatePlayingSong();
			
			}
			
			if (e.getSource() == playStopSongButton) //plays/stops the selected song. Will alternate between the two depending on if a song is playing or not
			{
				if (playlist.getPlaying().getPlayTime() == 0)
				{
					System.out.println("play");
					if (playlistJList.getSelectedIndex() > -1)
					{
						updatePlayingSong();
					}
					else
					{
						System.err.println("cannot play; no song is selected");
					}
				}
				else
				{
					System.out.println("stop"); //TODO: Stop the timer as well
					playlist.stop();
					nowPlayingLabel.setText("\""+playlist.getPlaying().getTitle() +"\" by "+ playlist.getPlaying().getArtist());
					playStopSongButton.setIcon(playIcon);
				}
				
			}
			
		}
		
	}
	
	/**
	 * This method will react to the playlistList JList. Selecting a song changes the current playing song to the selection,
	 * as well as updates the "play count" for the selected song
	 * 
	 * @author Dominick Edmonds
	 */
	private class PlaylistListListener implements ListSelectionListener //TODO: What the fuck is this even for anymore? IS GOD DEAD? IF YES HE'S IN THIS METHOD
	{
		@Override
		public void valueChanged(ListSelectionEvent e) //TODO: Copy list looping functionality (index 0 -> last, last -> 0, like you're going in a loop around the list) from labs 11 & 12's jlist
		{
			if (e.getValueIsAdjusting()==false)
			{
				/*if (e.getSource().isSelectionEmpty==false)*/ //TODO: Solve de-selecting JList item issue, either by disabling deselection functionality, or by a boolean that uses "nothing by nobody" if the item is deselected
				//updatePlayingSong();
			}
			
		}
		
	}

}