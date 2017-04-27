import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;

/**
 * The meat and potatoes of project 5.
 * This class will initializes the user interface for the MyTunes GUI window frame, and also
 * keeps it updated as the user interacts with it's components.
 * 
 * @author Dominick Edmonds
 */

@SuppressWarnings("serial")
public class MyTunesGUIPanel extends JPanel
{
	final String pattern = "###,###.##";
	final DecimalFormat DFORMAT = new DecimalFormat(pattern);
	
	private ImageIcon //sets up icons we use
	prevIcon = new ImageIcon("images/media-skip-backward-48.gif"),
	playIcon = new ImageIcon("images/play-48.gif"),
	stopIcon = new ImageIcon("images/stop-48.gif"),
	nextIcon = new ImageIcon("images/media-skip-forward-48.gif");
	
	private PlayList playlist;
	private JList<Song> playlistJList;
	private JButton[][] heatmap;
	private JLabel nowPlayingLabel,playlistTimeLabel,playlistSongCountLabel;
	private String numSongsString,timeInMinutesString;
	private JButton songUpButton,songDownButton,addSongButton,removeSongButton,nextSongButton,prevSongButton,playStopSongButton;
	private JPanel heatmapPanel;
	private Timer songTimer;

	
	/**
	 * Main constructor. This will set up the main panel frame to be used in MyTunesGUI.java
	 * Any action events will update the GUI as needed; all this does is set up it's initial state when the program
	 * is launched.
	 */
	public MyTunesGUIPanel()
	{
		playlist = new PlayList("Project 5 Mixtape");
		playlist.loadFromFile(new File("playlist.txt"));
		ButtonListener buttonListener = new ButtonListener();
		BorderLayout mainLayout = new BorderLayout();
		mainLayout.setHgap(10); 
		setLayout(mainLayout); 
		
		//Left side of the main panel starts here
		JPanel leftSide = new JPanel();
		leftSide.setLayout(new BoxLayout(leftSide,BoxLayout.Y_AXIS));
		leftSide.setPreferredSize(new Dimension(600,0));
		
		JPanel playlistLablePanel = new JPanel();
		playlistLablePanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JLabel playlistNameLabel = new JLabel(playlist.getName());
		playlistNameLabel.setFont(new Font(playlistNameLabel.getFont().getFontName(),Font.ITALIC,35)); 
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.ipadx = 20;
		playlistLablePanel.add(playlistNameLabel,c);
		
		numSongsString = new String(playlist.getNumSongs()+" songs"); 
		playlistSongCountLabel = new JLabel(numSongsString); 
		playlistSongCountLabel.setFont(new Font(playlistSongCountLabel.getFont().getFontName(),Font.PLAIN,15));
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		playlistLablePanel.add(playlistSongCountLabel,c);
		
		JLabel playlistSpacingLabel = new JLabel("==============");
		playlistSpacingLabel.setFont(new Font(playlistSpacingLabel.getFont().getFontName(),Font.BOLD,20));
		c.gridx = 1;
		c.gridy = 1;
		playlistSpacingLabel.setAlignmentX(CENTER_ALIGNMENT);
		playlistLablePanel.add(playlistSpacingLabel,c);
		timeInMinutesString = new String(DFORMAT.format(playlist.getTotalPlayTimeInMinutes())+" minutes");
		playlistTimeLabel = new JLabel(timeInMinutesString);
		playlistTimeLabel.setFont(new Font(playlistTimeLabel.getFont().getFontName(),Font.PLAIN,15));
		c.gridx = 2;
		c.gridy = 1;
		playlistLablePanel.add(playlistTimeLabel,c);
		leftSide.add(playlistLablePanel);
		
		//play list control panel
		JPanel playlistControlPanel = new JPanel();
		playlistControlPanel.setLayout(new BoxLayout(playlistControlPanel,BoxLayout.Y_AXIS));
		playlistControlPanel.setPreferredSize(new Dimension(0,700));
		
		playlistJList = new JList<Song>(playlist.getSongArray());
		playlistJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		playlistJList.setFont(new Font("monospaced", Font.PLAIN,12));
		playlistJList.setSelectedIndex(0);
		JScrollPane playlistScrollPane = new JScrollPane(playlistJList); 
		playlistScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		playlistScrollPane.setPreferredSize(new Dimension(0,700));
		playlistControlPanel.add(playlistScrollPane); 
		
		System.out.println("Debug selected initial index: " + playlistJList.getSelectedIndex());
		
		JPanel playlistControlButtonPanel = new JPanel();
		playlistControlButtonPanel.setLayout(new BoxLayout(playlistControlButtonPanel,BoxLayout.X_AXIS));
		playlistControlButtonPanel.setPreferredSize(new Dimension(500,80));
		
		songUpButton = new JButton(new ImageIcon("images/move-up-16.gif"));
		songUpButton.setToolTipText("Moves the song up in the playlist");
		songUpButton.addActionListener(buttonListener);
		songDownButton = new JButton(new ImageIcon("images/move-down-16.gif"));
		songDownButton.setToolTipText("Moves the selected song down in the playlist");
		songDownButton.addActionListener(buttonListener);
		addSongButton = new JButton("add song");
		addSongButton.addActionListener(buttonListener);
		removeSongButton = new JButton("remove song");
		removeSongButton.addActionListener(buttonListener);
		
		playlistControlButtonPanel.add(songUpButton);
		playlistControlButtonPanel.add(Box.createHorizontalStrut(10));
		playlistControlButtonPanel.add(songDownButton);
		playlistControlButtonPanel.add(Box.createHorizontalStrut(10));
		playlistControlButtonPanel.add(addSongButton);
		playlistControlButtonPanel.add(Box.createHorizontalStrut(10));
		playlistControlButtonPanel.add(removeSongButton);
		playlistControlButtonPanel.setBorder(BorderFactory.createTitledBorder("Playlist Controls"));
		playlistControlPanel.add(playlistControlButtonPanel);
		
		leftSide.add(playlistControlPanel);
		add(leftSide, BorderLayout.WEST);
	
		
		
		// Right side of the main Panel begins here
		JPanel rightSide = new JPanel();
		rightSide.setLayout(new BoxLayout(rightSide,BoxLayout.Y_AXIS));
		
		//heat map panel
		heatmapPanel = new JPanel();
		heatmap = new JButton[playlist.getSongSquare().length][playlist.getSongSquare().length];
		heatmapPanel.setLayout(new GridLayout(heatmap.length,heatmap.length));
		heatmapPanel.setPreferredSize(new Dimension(400,400));
				
		for (int row=0; row<heatmap.length;row++ )
		{
			for (int col = 0;col<heatmap.length;col++)
			{
				heatmap[row][col] = new JButton();
			}
		}
		
		for (int row = 0; row<heatmap.length;row++)
		{
			for (int col = 0; col<heatmap[row].length;col++)
			{
				heatmap[row][col].setText(playlist.getSongSquare()[row][col].getTitle());
				heatmap[row][col].addActionListener(new HeatmapListener());
				heatmap[row][col].setBackground(getHeatMapColor(playlist.getSongSquare()[row][col].getPlayCount()));
				heatmapPanel.add(heatmap[row][col]);				
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
		
		
		JPanel songControlButtonPanel = new JPanel(); 
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
		
	
	 /**
     * Given the number of times a song has been played, this method will
     * return a corresponding heat map color.
     *
     * Sample Usage: Color color = getHeatMapColor(song.getTimesPlayed());
     *
     * This algorithm was borrowed from:
     * http://www.andrewnoske.com/wiki/Code_-_heatmaps_and_color_gradients
     *
     * @param plays The number of times the song that you want the color for has been played.
     * @return The color to be used for your heat map.
     */
    private Color getHeatMapColor(int plays) 
    {
         final int MAX_PLAYS = 50;
         double minPlays = 0, maxPlays = MAX_PLAYS;    // upper/lower bounds
         double value = (plays - minPlays) / (maxPlays - minPlays); // normalize play count

         // The range of colors our heat map will pass through. This can be modified if you
         // want a different color scheme.
         Color[] colors = { Color.CYAN, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED };
         int index1, index2; // Our color will lie between these two colors.
         float dist = 0;     // Distance between "index1" and "index2" where our value is.

         if (value <= 0) 
         {
              index1 = index2 = 0;
         } 
         else if (value >= 1) 
         {
              index1 = index2 = colors.length - 1;
         } 
         else 
         {
              value = value * (colors.length - 1);
              index1 = (int) Math.floor(value); // Our desired color will be after this index.
              index2 = index1 + 1;              // ... and before this index (inclusive).
              dist = (float) value - index1; // Distance between the two indexes (0-1).
         }

         int r = (int)((colors[index2].getRed() - colors[index1].getRed()) * dist)
                   + colors[index1].getRed();
         int g = (int)((colors[index2].getGreen() - colors[index1].getGreen()) * dist)
                   + colors[index1].getGreen();
         int b = (int)((colors[index2].getBlue() - colors[index1].getBlue()) * dist)
                   + colors[index1].getBlue();

         return new Color(r, g, b);
    }
	
	
	/**
	 * This method will be called any time the selected song is changed, either by the list selector, the next or previous buttons,
	 * or the heat map buttons.
	 * 
	 * @author Dominick Edmonds
	 */
	private void updatePlayingSong()
	{	
		//playlist.getPlaying().stop();
		if (playlist.getPlaying().getPlayTime() > 0)
		{
			fullStop();
		}
		
		playlist.playSong(playlistJList.getSelectedIndex());
		
		songTimer = new Timer(playlist.getPlaying().getPlayTimeMS(), new TimerListener());
		songTimer.start();
		songTimer.setRepeats(false);
		
		
		playStopSongButton.setIcon(stopIcon);
		nowPlayingLabel.setText("\""+playlist.getPlaying().getTitle() +"\" by "+ playlist.getPlaying().getArtist());
		
		nextSongButton.setEnabled(true);
		prevSongButton.setEnabled(true);
		
		syncPlaylist();
		
		System.out.println("DEBUG PLAYS: " + playlist.getPlaying().getPlayCount()); 
	}
	
	/**
	 * Stops everything without playing something else
	 */
	private void fullStop()
	{
		System.out.println("debug: full stop"); 
		songTimer.stop();
		playlist.stop();
		nowPlayingLabel.setText("\""+playlist.getPlaying().getTitle() +"\" by "+ playlist.getPlaying().getArtist());
		playStopSongButton.setIcon(playIcon);
	}
	
	/**
	 * Used to refresh the sync the JList and Heat map if either a song is added, removed, or moved.
	 * This includes a check to see if the length of the heatmap's sides can be changed or not
	 * as well as the colors of the heat map buttons.
	 */
	private void syncPlaylist()
	{
		playlistJList.setListData(playlist.getSongArray());
		playlistJList.setSelectedValue(playlist.getPlaying(), true); //ensures any time we sync the play list, we still have something valid selected 
		heatmapPanel.removeAll();
		
		
		
		if (playlist.getNumSongs() != 0)
		{
			heatmap = new JButton[playlist.getSongSquare().length][playlist.getSongSquare().length];
			heatmapPanel.setLayout(new GridLayout(heatmap.length,heatmap.length));
			heatmapPanel.setPreferredSize(new Dimension(400,400));
			
			for (int row=0; row<heatmap.length;row++ )
			{
				for (int col = 0;col<heatmap.length;col++)
				{
					heatmap[row][col] = new JButton();
				}
			}
			
			for (int row = 0; row<heatmap.length;row++)
			{
				for (int col = 0; col<heatmap[row].length;col++)
				{
					heatmap[row][col].setText(playlist.getSongSquare()[row][col].getTitle());
					heatmap[row][col].addActionListener(new HeatmapListener());
					heatmap[row][col].setBackground(getHeatMapColor(playlist.getSongSquare()[row][col].getPlayCount())); 
					heatmapPanel.add(heatmap[row][col]);
				}
			}
			heatmapPanel.validate();
		}
		else
		{
			System.out.println("Debug, removed last song");
			heatmapPanel.removeAll();
			heatmapPanel.repaint();
			heatmapPanel.validate();
		}
		
	}
	
	
	/**
	 * Updates total time and number of songs labels 
	 * Used in any situation that changes the contents of the play list, like adding/removing songs.
	 */
	private void syncPlaylistStatLables() 
	{
		numSongsString = new String(playlist.getNumSongs()+" songs"); 
		playlistSongCountLabel.setText(numSongsString);
		timeInMinutesString = new String(DFORMAT.format(playlist.getTotalPlayTimeInMinutes())+" minutes");
		playlistTimeLabel.setText(timeInMinutesString);
	}
	
	/**
	 * This listener is specifically for the heat map section; plays the song selected as soon as it's clicked. Updates the JList
	 */
	private class HeatmapListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			for (int row=0;row<heatmap.length;row++)
			{
				for(int col=0; col<heatmap[row].length;col++)
				{
					if (e.getSource() == heatmap[row][col])
					{
						playlistJList.setSelectedValue(playlist.getSongSquare()[row][col],true);
						updatePlayingSong();
					}		
				}
			}
		}	
	}
	
	/**
	 * This listener will listen for all the buttons on the panel, performing actions accordingly.
	 * 
	 * @author Dominick Edmonds
	 */														
	private class ButtonListener implements ActionListener  
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (e.getSource() == songUpButton) //Moves the song UP one in the list, as in -1 in the array refreshes the JList
			{
				System.out.println("songUp: " +playlistJList.getSelectedIndex() +" to " + (playlistJList.getSelectedIndex()-1));
				int targetIndex = playlist.moveUp(playlistJList.getSelectedIndex());
				syncPlaylist();
				playlistJList.setSelectedIndex(targetIndex);
			}
			
			if (e.getSource() == songDownButton) //Moves the song DOWN one in the list, as in +1 in the array, refreshes the JList 
			{
				System.out.println("songDown: "+playlistJList.getSelectedIndex()+" to " + (playlistJList.getSelectedIndex()+1));
				int targetIndex = playlist.moveDown(playlistJList.getSelectedIndex());
				syncPlaylist();
				playlistJList.setSelectedIndex(targetIndex);
			}
			
			if (e.getSource() == addSongButton) //opens a file browser and ADDS a new song to the list at the end, refreshes the JList
			{
				System.out.println("addSong");
				
				//Create a file chooser
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setCurrentDirectory(new File("sounds/"));
				
				//In response to a button click:
				int returnVal = fileChooser.showOpenDialog(null);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) 
				{
		           File file = fileChooser.getSelectedFile();
		           fileChooser.setCurrentDirectory(file.getParentFile());
		           if (file.exists() == true)
		           {
		        	   JTextField title = new JTextField(); 
		        	   JTextField artist = new JTextField();
		        	   
		        	   JComboBox<Integer> timeMin = new JComboBox<Integer>();
		        	   for (int i=0;i<21;i++)
		        	   {
		        		   timeMin.addItem(i);
		        	   }
		        	   timeMin.setSelectedIndex(0);
		        	   
		        	   JComboBox<Integer> timeSec = new JComboBox<Integer>();
		        	   for (int i=0;i<60;i++)
		        	   {
		        		   timeSec.addItem(i);
		        	   }
		        	   timeSec.setSelectedIndex(0);
		        	   boolean inputAccepted = false;
		        	   while(!inputAccepted) 
		        	   {
		        		   try 
		        		   {
		        			   Object[] message = {"Title:", title,"Artist:", artist,"Minutes:",timeMin,"Seconds:",timeSec};
		        			   int option = JOptionPane.showConfirmDialog(null, message, "New song", JOptionPane.OK_CANCEL_OPTION);
		        	   
		        			   if (option == JOptionPane.OK_OPTION) 
		        			   {		        		   
		        				   String finalTitle = title.getText().trim();
		        				   String finalArtist = artist.getText().trim();
		        				   if (finalTitle == null || finalTitle.isEmpty())
		        				   {
		        					   throw new RuntimeException("Please enter a Title");
		        				   }
		        				   else
		        				   {
		        					   if (finalArtist == null || finalArtist.isEmpty())
		        					   {
		        						   throw new RuntimeException("Please enter the Artist"); 
		        					   }
		        					   else
		        					   {
		        						   int finalSeconds = ((int)timeMin.getSelectedItem()*60)+(int)timeSec.getSelectedItem();
		        						   playlist.addSong(new Song(finalTitle,finalArtist,finalSeconds,file.getPath()));
		        						   inputAccepted=true;
		        					   }
		        				   }
		        			   }   
		        		   } 
		        		   catch (RuntimeException ex)
		        		   {
		        			   JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		        		   }
		        		   
		        		 }
		        	   }
					} 
				
				syncPlaylist();
				syncPlaylistStatLables();
			}
			
			if (e.getSource() == removeSongButton) //opens a dialogue, "yes" will delete the currently selected song.
			{
				
				int selected = playlistJList.getSelectedIndex();
				System.out.println("debug removeSong@: "+selected);
				
				if (selected == -1) //If nothing is actually selected in the list, this won't do anything
				{
					JOptionPane.showMessageDialog(null,"Please select a song to remove","Error",JOptionPane.ERROR_MESSAGE);
				}	
				else
				{
					
					int choice = JOptionPane.showConfirmDialog(null,"Are you sure you want to remove this song?","Remove this song?",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
					if (choice == JOptionPane.YES_OPTION)
					{
						playlist.removeSong(selected);
						syncPlaylist();
						syncPlaylistStatLables();
						if (selected >= playlist.getNumSongs()-1)
						{
							playlistJList.setSelectedIndex(selected-1);
						}
						else
						{
							playlistJList.setSelectedIndex(selected);
						}
							
						System.out.println("Debug: Removed the song sucsessfully");
					}
				}
			}
			
			
			if (e.getSource() == nextSongButton) //takes the selected JList item and goes to the NEXT song, looping if at either end, going to index 0 if none are selected, and begins to play the new song.
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
				if (playlist.getPlaying().getPlayTime() <= 0)
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
					fullStop();
				}
				
			}
			
		}
		
	}
	
	/**
	 * Controls the timer to stop songs at the end of their play time
	 * 
	 *@author Dominick Edmonds
	 */
	private class TimerListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("Debug: Timer's done!");
			fullStop();
		}
	}

}