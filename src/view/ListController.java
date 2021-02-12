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
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

	private List<Song> songList;
	private ObservableList<String> obsList;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Collections.sort(songList);
		
		for (int i = 0; i < songList.size(); i++) {
			obsList.add(songList.get(i).getDisplayString());
		}
		
		listView.setItems(obsList);

		if (obsList.size() > 0) {
			listView.getSelectionModel().select(0);
			showItem(mainStage);
		}
		
		listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> showItem(mainStage));
	}

	private void showItem(Stage mainStage) {		
		String item = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();
		
		Song selectedSong = songList.get(index);
		songName.setText(selectedSong.getName());
		songArtist.setText(selectedSong.getArtist());
		if (!selectedSong.getAlbum().equals("\\N")) {
			songAlbum.setText(selectedSong.getAlbum());
		} else {
			songAlbum.setText("unavailable");
		}
		if (!selectedSong.getYear().equals("\\N")) {
			songYear.setText(selectedSong.getYear());
		} else {
			songYear.setText("unavailable");
		}
	}

}
