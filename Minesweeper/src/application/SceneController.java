package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SceneController {

	static int tileHeight;
	static int tileWidth;
	static int windowHeight;
	static int windowWidth;
	static String difficultyLevel;
	
	public void checkFields(ActionEvent event) {
		
		Button button = (Button) event.getSource();
		AnchorPane root = (AnchorPane) button.getParent();
		
		TextField heightField = (TextField) root.lookup("#heightTextField");
		TextField widthField = (TextField) root.lookup("#widthTextField");
		boolean flag = false;
		try {
	        int heightNumber = Integer.parseInt(heightField.getText());
	        int widthNumber = Integer.parseInt(widthField.getText());
	        if (heightNumber >= 5 && heightNumber <= 40 && widthNumber >= 5 && widthNumber <= 70) {
	            flag = true;
	            tileHeight = heightNumber;
	            tileWidth = widthNumber;
	            windowHeight = tileHeight*20;
	            windowWidth = tileWidth*20;
	            @SuppressWarnings("unchecked")
				ChoiceBox<String> difficultyPicker = (ChoiceBox<String>) root.lookup("#difficultyChoiceBox");
	            difficultyLevel = difficultyPicker.getValue();
	        } else {
	            flag = false;
	        }
	    } catch (NumberFormatException e) {
	    	flag = false;
	    }
		if(flag) {
			displayGameWindow(event);
		} else {
			displayIncorrectInputWindow();
		}
	}
	
	public void closeWindow(ActionEvent event) {
		Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
		stage.close();
	}
	
	public void displayIncorrectInputWindow() {
		Stage IncorrectInputStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("IncorrectInput.fxml"));
		try {
			AnchorPane popUp = loader.load();
			IncorrectInputStage.setScene(new Scene(popUp));
		} catch (IOException e) {
			e.printStackTrace();
		}
		IncorrectInputStage.initModality(Modality.APPLICATION_MODAL);
		IncorrectInputStage.setTitle("Incorrect Input");
		IncorrectInputStage.setResizable(false);
		IncorrectInputStage.show();
	}
	
	public void displayGameWindow(ActionEvent event) {
		GridPane gridPane = new GridPane();
		gridPane.setGridLinesVisible(true);
        gridPane.setStyle("-fx-grid-lines-visible: true; -fx-hgap: 1; -fx-vgap: 1; -fx-border-color: black;");
        
        for (int i = 0; i < tileHeight; i++) {
            for (int j = 0; j < tileWidth; j++) {
                Label label = new Label();
                label.setPrefWidth(20);
                label.setPrefHeight(20);
                label.setAlignment(Pos.CENTER);
                label.setStyle("-fx-background-color: #777777; -fx-text-fill: transparent;");
                
                label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                	
                    public void handle(MouseEvent event1) {
                    	startGame(label);
                    }
                });
                
                gridPane.add(label, j, i);
            }
        }
        
        Scene scene = new Scene(gridPane, windowWidth, windowHeight);
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
	}
	
	public void startGame(Label label) {
		Board board = new Board(tileHeight, tileWidth);
		int row = GridPane.getRowIndex(label);
		int column = GridPane.getColumnIndex(label);
        board.fillMinefield(row,column);
        
        GridPane gridPane = (GridPane) label.getParent();
        for (int i = 1; i <= tileHeight; i++) {
            for (int j = 1; j <= tileWidth; j++) {
            	Label cell = (Label) gridPane.getChildren().get((i-1)*tileWidth+j);
            	if(board.minefield[i-1][j-1].getIsBomb()) {
            		cell.setText("*");
                } else if(board.minefield[i-1][j-1].getNextTo()!=0) {
                	cell.setText(Integer.toString(board.minefield[i-1][j-1].getNextTo()));
                } else {
                	cell.setText(" ");
                }
            	cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                	
                    public void handle(MouseEvent event1) {
                    	if(cell.getText().equals("*")) {
                    		cell.setStyle("-fx-background-color: #ff0000; -fx-text-fill: black;");
                        	revealBombs(false,label);
                    		displayGameLostWindow();
                    	} else if(cell.getText().equals(" ")){
                    		revealAdjacentTiles(cell);
                    	} else {
                    		cell.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black;"); // reveal text on click
                    	}
                    	checkBoard(cell);
                    }
                });
            }
        }
        if(label.getText().equals(" ")){
    		revealAdjacentTiles(label);
    	} else {
    		label.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black;"); // reveal text on click
    	}
	}
	
	public void revealAdjacentTiles(Label label) {
		GridPane gridPane = (GridPane) label.getParent();
		label.setText("  ");
		label.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black;");
		int row = GridPane.getRowIndex(label)+1;
		int column = GridPane.getColumnIndex(label)+1;
    	for(int i=-1;i<=1;i++) {
			for(int j=-1;j<=1;j++) {     
				if(row+i>0 && row+i<=tileHeight && column+j>0 && column+j<=tileWidth) {
					Label adjacentCell = (Label) gridPane.getChildren().get((row+i-1)*tileWidth+column+j);
					if(adjacentCell.getText().equals(" ")){
						String style = label.getStyle();
						String color = style.substring(style.indexOf("-fx-background-color:")+22, style.indexOf("-fx-background-color:")+29);
						if(color.equals("#ffffff")) {
							revealAdjacentTiles(adjacentCell);
						}
					} else {
						adjacentCell.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black;");						
					}
				}
			}
		}
	}
	
	public void displayGameWonWindow() {
		Stage GameWonStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GameWon.fxml"));
		try {
			AnchorPane popUp = loader.load();
			GameWonStage.setScene(new Scene(popUp));
		} catch (IOException e) {
			e.printStackTrace();
		}
		GameWonStage.initModality(Modality.APPLICATION_MODAL);
		GameWonStage.setTitle("Game Won");
		GameWonStage.setResizable(false);
		GameWonStage.setOnCloseRequest(event -> System.exit(0));
		GameWonStage.show();
	}
	
	public void displayGameLostWindow() {
		Stage GameLostStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GameLost.fxml"));
		try {
			AnchorPane popUp = loader.load();
			GameLostStage.setScene(new Scene(popUp));
		} catch (IOException e) {
			e.printStackTrace();
		}
		GameLostStage.initModality(Modality.APPLICATION_MODAL);
		GameLostStage.setTitle("Game Lost");
		GameLostStage.setResizable(false);
		GameLostStage.setOnCloseRequest(event -> System.exit(0));
		GameLostStage.show();
	}
	
	public void checkBoard(Label label) {
	
		GridPane gridPane = (GridPane) label.getParent();
		boolean flag = true;
        for (int i = 1; i <= tileHeight && flag; i++) {
            for (int j = 1; j <= tileWidth && flag; j++) {
            	Label cell = (Label) gridPane.getChildren().get((i-1)*tileWidth+j);
				if(!cell.getText().equals("*")) {
					String style = cell.getStyle();
					String color = style.substring(style.indexOf("-fx-background-color:")+22, style.indexOf("-fx-background-color:")+29);
					if(!color.equals("#ffffff")) {
						flag = false;
					}
				}
            }
        }
        if(flag) {
        	revealBombs(true,label);
        	displayGameWonWindow();
        }
	}
	
	public void exitGame(ActionEvent event) {
		System.exit(0);
	}
	
	public void revealBombs(boolean gameWon,Label label) {
		GridPane gridPane = (GridPane) label.getParent();
        for (int i = 1; i <= tileHeight; i++) {
            for (int j = 1; j <= tileWidth; j++) {
            	Label cell = (Label) gridPane.getChildren().get((i-1)*tileWidth+j);
				if(cell.getText().equals("*")) {
					String style = cell.getStyle();
					String color = style.substring(style.indexOf("-fx-background-color:")+22, style.indexOf("-fx-background-color:")+29);
					if(!color.equals("#ff0000")) {
						if(gameWon) {
							cell.setStyle("-fx-background-color: #00ff00; -fx-text-fill: black;");		
						} else {
							cell.setStyle("-fx-background-color: #ffff00; -fx-text-fill: black;");		
						}
					}
				}
            }
        }
	}
}
