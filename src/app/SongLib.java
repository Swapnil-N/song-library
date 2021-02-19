//Swapnil Napuri and Srinandini Marpaka

package app;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.ListController;

public class SongLib extends Application {

	List<Song> songList;

	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/main.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		ListController listController = loader.getController();
		listController.start(primaryStage);

		Scene scene = new Scene(root, 560, 310);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Song Library");
		primaryStage.setResizable(false);
		primaryStage.show();

		songList = listController.getSongList();
	}

	public static void main(String[] args) {

		launch(args);

	}

	@Override
	public void stop() throws Exception {

		try {
			FileWriter writer = new FileWriter("songs.txt");

			for (Song song : songList)
				writer.write(song.getTextFileString());

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		super.stop();
	}

}
