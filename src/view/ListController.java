package view;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import app.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class ListController {

	@FXML
	ListView<String> listView;
	@FXML
	TextField songName;
	@FXML
	TextField songArtist;
	@FXML
	TextField songAlbum;
	@FXML
	TextField songYear;
	//@FXML
	//Button editButton;

	private List<Song> songList;
	private ObservableList<String> obsList;
	private boolean edit;

	public void start(Stage mainStage) {
		
		songList = new ArrayList<>();
		obsList = FXCollections.observableArrayList();	
		String row = "";

		try {
			BufferedReader fileReader = new BufferedReader(new FileReader("songs.txt"));
			while ((row = fileReader.readLine()) != null) {
				String[] data = row.split("\\|");
				songList.add(new Song(data[0], data[1], data[2], data[3]));
			}
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		edit = false;
		
		addToObsList();
		
		listView.setItems(obsList);

		if (obsList.size() > 0) {
			listView.getSelectionModel().select(0);
			showItem(mainStage);
		}
		
		listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> showItem(mainStage));
	}
	
	public void addToObsList() {
		
		Collections.sort(songList);
		
		obsList.clear();
		
		for (int i = 0; i < songList.size(); i++)
			obsList.add(songList.get(i).getDisplayString());
		
	}
	
	public void onActionEdit(ActionEvent e) {
		
		Button editButton = (Button)e.getSource();
		
		edit = true;
				
		editable(true);
		
	}
	
	public void onActionCancel(ActionEvent e) {
		
		updateFields();
		
		editable(false);
	}
	
	public void onActionAdd(ActionEvent e) {
		
		songName.setText(""); //change this to update
		songArtist.setText("");
		songAlbum.setText("");
		songYear.setText("");
				
		editable(true);
		
	}
	
	public void onActionDelete(ActionEvent e) {
		
		int index = listView.getSelectionModel().getSelectedIndex();
		if (index == -1)
			return;
		
		songList.remove(index);
		obsList.remove(index);
		
		if (index >= obsList.size())
			index = obsList.size()-1;
		listView.getSelectionModel().select(index);
		
		updateFields();
		
	}
	
	
	public void onActionSave(ActionEvent e) {
				
		if (!songName.isEditable())
			return;
		
		Song newSong = new Song(songName.getText(), songArtist.getText(), songAlbum.getText(), songYear.getText());
		
		if (newSong.getName().length() == 0 || newSong.getArtist().length() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Missing Info");
			alert.setContentText("Please Enter a Song Name AND a Song Artist");
			alert.showAndWait();
			return;
		}
		
		for (int i=0; i<songList.size(); i++) {
			if (newSong.compareTo(songList.get(i)) == 0) { //equals or .contains do no work
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Duplicate Error");
				alert.setContentText("The Song Name and Song Artist are already in the list");
				alert.showAndWait();
				return;
			}
		} 
		
		if(edit)
			onActionDelete(null);
		
		songList.add(newSong);
		addToObsList();
		
		listView.getSelectionModel().select(songList.indexOf(newSong));
		
		editable(false);
		
	}
	
	private void editable(boolean bool) {
		songName.setEditable(bool);
		songArtist.setEditable(bool);
		songAlbum.setEditable(bool);
		songYear.setEditable(bool);		
	}
	

	private void showItem(Stage mainStage) {		
		editable(false);
		edit = false;
		updateFields();
	}
	
	private void updateFields() {
		
		int index = listView.getSelectionModel().getSelectedIndex();
		if (index == -1) {
			songName.setText("");
			songArtist.setText("");
			songAlbum.setText("");
			songYear.setText("");
			return;
		}
		
		Song selectedSong = songList.get(index);
		songName.setText(selectedSong.getName());
		songArtist.setText(selectedSong.getArtist());
		songAlbum.setText(selectedSong.getAlbum());
		songYear.setText(selectedSong.getYear());
		
	}
	

}
