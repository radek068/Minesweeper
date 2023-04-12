package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
			Scene scene = new Scene(root);
			
			@SuppressWarnings("unchecked")
			ChoiceBox<String> difficultyPicker = (ChoiceBox<String>) root.lookup("#difficultyChoiceBox");
			String[] difficultyLevels = {"easy","medium","hard"};
			difficultyPicker.getItems().addAll(difficultyLevels);
			difficultyPicker.setValue(difficultyLevels[1]);
			
			TextField heightField = (TextField) root.lookup("#heightTextField");
			heightField.setTextFormatter(new TextFormatter<>(change -> {
			    String input = change.getText();
			    if (input.matches("[0-9]*")) {
			        return change;
			    }
			    return null;
			}));
			heightField.setText("12");
			
			TextField widthField = (TextField) root.lookup("#widthTextField");
			widthField.setTextFormatter(new TextFormatter<>(change -> {
			    String input = change.getText();
			    if (input.matches("[0-9]*")) {
			        return change;
			    }
			    return null;
			}));
			widthField.setText("10");
			
			primaryStage.setTitle("Minesweeper");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
