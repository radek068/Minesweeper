package application;

import javafx.scene.control.Label;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class TestMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Label label = new Label();
		label.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black;");
		BackgroundFill background = label.getBackground().getFills().get(0);
		Color color = (Color) background.getFill();
		if(color.equals(new Color(255,255,255,255))) {
		System.out.print(true);	
		} else System.out.print(false);
	}
}
