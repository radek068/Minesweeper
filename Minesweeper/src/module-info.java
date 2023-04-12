module Minesweeper {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	requires java.desktop;
	requires java.xml;
	requires jdk.internal.le;
	
	opens application to javafx.graphics, javafx.fxml;
}
