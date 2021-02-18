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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class ListController {

	@FXML
	ListView<String> listView;
	@FXML
	TextField songName, songArtist, songAlbum, songYear;
	@FXML
	Button addButton, editButton, deleteButton, saveButton, cancelButton;

	private List<Song> songList;
	private ObservableList<String> obsList;
	private String editDisplayName;

	public void start(Stage mainStage) {
		songList = new ArrayList<>();
		obsList = FXCollections.observableArrayList();
		editDisplayName = null;
		
		try {
			String row = "";
			BufferedReader fileReader = new BufferedReader(new FileReader("songs.txt"));
			while ((row = fileReader.readLine()) != null) {
				String[] data = row.split("\\|");
				if (data.length != 4)
					continue;
				songList.add(new Song(data[0], data[1], data[2], data[3]));
			}
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Collections.sort(songList);

		for (int i = 0; i < songList.size(); i++)
			obsList.add(songList.get(i).getDisplayString());

		listView.setItems(obsList);

		if (obsList.size() > 0)
			listView.getSelectionModel().select(0);
		updateFields();

		listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> showItem(mainStage));
		
		disable(false);
	}

	public void onActionEdit(ActionEvent e) {
		if (listView.getSelectionModel().getSelectedIndex() == -1)
			return;

		editDisplayName = songList.get(listView.getSelectionModel().getSelectedIndex()).getDisplayString();
		editable(true);
		disable(true);
	}

	public void onActionAdd(ActionEvent e) {
		if (songName.isEditable()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Hint");
			alert.setContentText("Please use the save button to add/update changes");
			alert.showAndWait();
			return;
		}

		songName.setText("");
		songArtist.setText("");
		songAlbum.setText("");
		songYear.setText("");

		editable(true);
		disable(true);
	}

	public void onActionDelete(ActionEvent e) {
		if (listView.getSelectionModel().getSelectedIndex() == -1)
			return;
				
		boolean delete = true;
		
		if (e != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirm Delete");
			alert.setContentText("Please confirm that you would like to delete the selected song");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() != ButtonType.OK)
				delete = false;
		}

		if (delete) {
			int index = listView.getSelectionModel().getSelectedIndex();
			if (index == -1)
				return;

			songList.remove(index);
			obsList.remove(index);

			if (index >= obsList.size())
				index = obsList.size() - 1;
			listView.getSelectionModel().select(index);
			
			updateFields();
		}
		disable(false);
	}

	public void onActionCancel(ActionEvent e) {
		updateFields();
		editable(false);
		disable(false);
		editDisplayName = null;
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

		if (!newSong.getDisplayString().equals(editDisplayName)) {
			for (int i = 0; i < songList.size(); i++) {
				if (newSong.compareTo(songList.get(i)) == 0) { // equals or .contains do no work
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Duplicate Song");
					alert.setContentText("The Song Name and Song Artist are already in the list");
					alert.showAndWait();
					return;
				}
			}
		}

		if (newSong.getName().contains("|") || newSong.getArtist().contains("|") || newSong.getAlbum().contains("|")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Character Entered");
			alert.setContentText("A vertical bar ('|') is not permitted in the Song Name, Artist, or Album");
			alert.showAndWait();
			return;
		}

		if (newSong.getYear() != null && !newSong.getYear().equals("")) {
			try {
				Integer yearInteger = Integer.parseInt(newSong.getYear());
				if (yearInteger < 0) {
					throw new NumberFormatException("Negative Value");
				}
			} catch (NumberFormatException ex) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid Year");
				alert.setContentText("The Song Year must be a positive integer.");
				alert.showAndWait();
				return;
			}
		}

		if (editDisplayName != null)
			onActionDelete(null);

		songList.add(newSong);
		Collections.sort(songList);
		obsList.clear();

		for (int i = 0; i < songList.size(); i++)
			obsList.add(songList.get(i).getDisplayString());

		listView.getSelectionModel().select(songList.indexOf(newSong));

		editable(false);
		disable(false);
	}

	private void editable(boolean bool) {
		songName.setEditable(bool);
		songArtist.setEditable(bool);
		songAlbum.setEditable(bool);
		songYear.setEditable(bool);
	}
	
	private void disable(boolean bool) {
		addButton.setDisable(bool);
		editButton.setDisable(bool);
		deleteButton.setDisable(bool);
		if (obsList.size() == 0) {
			editButton.setDisable(true);
			deleteButton.setDisable(true);
		}
		saveButton.setDisable(!bool);
		cancelButton.setDisable(!bool);
	}

	private void showItem(Stage mainStage) {
		editable(false);
		editDisplayName = null;
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

	public List<Song> getSongList() {
		return songList;
	}

}
