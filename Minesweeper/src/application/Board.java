package application;

import java.util.Random;

public class Board {

	public Tile[][] minefield;
	private int howManyBombs;
	
	public Board(int height,int width) {
		minefield = new Tile[height][width];
		for(int i=0;i<minefield.length;i++) {
			for(int j=0;j<minefield[0].length;j++) {
				minefield[i][j] = new Tile();
			}
		}
		switch(SceneController.difficultyLevel) {
        case "easy":
        	howManyBombs = (int) ((height*width)/9 + 1);
        	break;
        case "medium":
        	howManyBombs = (int) ((height*width)/8 + 1);
        	break;
        case "hard":
        	howManyBombs = (int) ((height*width)/7 + 1);
        	break;
        default:
        	howManyBombs = (int) ((height*width)/8 + 1);
		}
	}
	
	public void fillMinefield(int height,int width) {
		Random rand = new Random();
		int[][] bombLocations = new int[howManyBombs][2];
		for(int i=0;i<howManyBombs;i++) {
			int newBombHeight = rand.nextInt(minefield.length);
			int newBombWidth = rand.nextInt(minefield[0].length);
			if(newBombHeight == height && newBombWidth == width) {
				i--;
				continue;
			}
			boolean flag = false;
			for(int j=0;j<i;j++) {
				if(bombLocations[j][0] == newBombHeight && bombLocations[j][1] == newBombWidth) {
					flag=true;
					break;
				}
			}
			if(flag == true) {
				i--;
				continue;
			}
			bombLocations[i][0] = newBombHeight;
			bombLocations[i][1] = newBombWidth;
		}
		
		for(int i=0;i<minefield.length;i++) {
			for(int j=0;j<minefield[0].length;j++) {
				for(int m=0;m<howManyBombs;m++) {
					if(bombLocations[m][0] == i && bombLocations[m][1] == j) {
						minefield[i][j].setIsBomb(true);	
						for(int k=-1;k<=1;k++) {
							for(int l=-1;l<=1;l++) {
								if(i+k>=0 && i+k<minefield.length) {
									if(j+l>=0 && j+l<minefield[0].length) {
										minefield[i+k][j+l].incrementNextTo();
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
