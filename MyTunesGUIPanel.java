import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.File;

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
	//TODO: Btw you can totes have instance variables here for things like the current playlist, etc.
	//TODO: jlist's list, the playlist object "selected playlist", current song index (?), 2d buttons array for heatmap,
	//		color changing thing (what will it be?)
	
	//TODO: Put all buttons and components up here so we can change them within other methods
	JList<Song> playlistList;
	PlayList playlist; //TODO: Update if you change the playlist(do we need this fuction for the project?)
	JButton[][] heatmap;
	Song[] songs;
	JLabel nowPlayingLabel;
	
	/**
	 * Main constructor. This will set up the main panel frame to be used in MyTunesGUI.java.
	 */
	public MyTunesGUIPanel()
	{
		playlist = new PlayList("Project 5 Mixtape");
		playlist.loadFromFile(new File("playlist.txt"));
		
		BorderLayout mainLayout = new BorderLayout();
		mainLayout.setHgap(10); 
		setLayout(mainLayout); 
		
		// left (border layout west)
		JPanel leftSide = new JPanel();
		leftSide.setLayout(new BoxLayout(leftSide,BoxLayout.Y_AXIS));
		leftSide.setPreferredSize(new Dimension(600,0));
		
		//play list label panel //TODO: Fix the spacing here
		JPanel playlistLablePanel = new JPanel();
		playlistLablePanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JLabel playlistNameLabel = new JLabel(playlist.getName()); //Displays the loaded playlist's name TODO: replace with proper content
		playlistNameLabel.setFont(new Font(playlistNameLabel.getFont().getFontName(),Font.ITALIC,35)); 
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.ipadx = 20;
		playlistLablePanel.add(playlistNameLabel,c);
		
		String numSongs = new String(playlist.getNumSongs()+" songs"); //TODO: Update when adding/removing songs, make instanced
		JLabel playlistSongCountLabel = new JLabel(numSongs); //Displays the loaded playlist's song count TODO: replace with proper content
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
		
		String totalTime = new String(playlist.getTotalPlayTime()+" minutes"); //TODO: Update when adding/removing songs, make instanced
		JLabel playlistTimeLabel = new JLabel(totalTime); //Displays the loaded playlist's time TODO: replace with proper content; mm.(%remaining minute)
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
		playlistList = new JList<Song>(playlist.getSongArray());
		playlistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		playlistList.setFont(new Font("monospaced", Font.PLAIN,12));
		playlistList.addListSelectionListener(new PlaylistListListener());
		JScrollPane playlistScrollPane = new JScrollPane(playlistList); 
		playlistScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		playlistControlPanel.add(playlistScrollPane); 
		
		
		JPanel playlistControlButtonPanel = new JPanel();
		playlistControlButtonPanel.setLayout(new BoxLayout(playlistControlButtonPanel,BoxLayout.X_AXIS));
		JButton songUpButton = new JButton("^");
		JButton songDownButton = new JButton("V");
		JButton addSongButton = new JButton("add song");
		JButton removeSongButton = new JButton("remove song");
		
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
		JButton prevSongButton = new JButton(new ImageIcon("images/media-skip-backward-48.gif"));
		JButton playStopSongButton = new JButton(new ImageIcon("images/play-48.gif"));
		JButton nextSongButton = new JButton(new ImageIcon("images/media-skip-forward-48.gif"));
		
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
	private void updatePlayingSong() //TODO: Doesn't update song on list seleciton, only when a heatmap button is pressed or the play button is selected.
	{
			playlist.playSong(playlistList.getSelectedIndex());
			nowPlayingLabel.setText((playlist.getPlaying().getTitle() +" by "+ playlist.getPlaying().getArtist()));
	}
	
	
	//TODO: Event handlers: Buttons: soungUP,songDOWN,nextSong,prevSong,addSong,removeSong,playSong/stopSong,heatmapButtons(ID'd by the song object in the associated jlist(?))
	
	/**
	 * This method will react to the playlistList JList. Selecting a song changes the current playing song to the selection,
	 * as well as updates the "play count" for the selected song
	 * 
	 * @author Dominick Edmonds
	 */
	private class PlaylistListListener implements ListSelectionListener 
	{
		@Override
		public void valueChanged(ListSelectionEvent e) //TODO: Copy list looping functionality (index 0 -> last, last -> 0, like you're going in a loop around the list) from labs 11 & 12's jlist
		{
			if (e.getValueIsAdjusting()==false)
			{
				/*if (e.getSource().isSelectionEmpty==false)*/ //TODO: Solve deselecting jlist item issue, either by disabling deselection functionality, or by a boolean that uses "nothing by nobody" if the item is deselected
				updatePlayingSong();
			}
			
		}
		
	}

}
