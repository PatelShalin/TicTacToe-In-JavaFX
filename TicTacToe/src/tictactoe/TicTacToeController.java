//TicTacToeController
//Shalin A. Patel
//March 28, 2018
/*This program operates a simple 3x3 tictactoe game where it gets input from the user and determines a winner by looking for a 3 in a row combination of x's or o's The
 * user can choose from 3 game modes: pvp, easy ai, or hard ai. There is also an option to display the how to play and the about screen, as well a button to swap the
 * theme of the game board with 2 variants.
 */

package tictactoe;

import javafx.application.Platform;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TicTacToeController {

	@FXML
	Button b1;
	@FXML
	Button b2;
	@FXML
	Button b3;
	@FXML
	Button b4;
	@FXML
	Button b5;
	@FXML
	Button b6;
	@FXML
	Button b7;
	@FXML
	Button b8;
	@FXML
	Button b9;
	@FXML
	GridPane gameBoard;
	@FXML
	BorderPane border1;

	// Declare basic variables
	static Stage secondaryStage;

	static Stage tertiaryStage;

	static Stage quaternaryStage;

	int mode = 0;

	private int howManyLeft = 9;

	private int changeTheme = 0;

	private boolean isFirstPlayer = true;
	private boolean didSomeoneWin = false;

	// Method to handle button clicks on the main screen
	public void buttonClickHandler(ActionEvent evt) {
		// Store the clickedbutton and the clickedButton's text into variables
		Button clickedButton = (Button) evt.getTarget();
		String buttonLabel = clickedButton.getText();

		// If the user is in pvp mode
		if (mode == 0) {
			// If the square they clicked on is empty and its the first player's turn
			if ("".equals(buttonLabel) && isFirstPlayer) {
				// set the text of that button equal to X
				clickedButton.setText("X");
				// Make sure the first player does not go twice
				isFirstPlayer = false;
				// Check if someone won the game
				didSomeoneWin = find3InRow();
				// If someone won
				if (didSomeoneWin == true) {
					// display the win screen
					displayWinScreen();
				}
				// Else if the button is empty and it's player 2's turn
			} else if ("".equals(buttonLabel) && !isFirstPlayer) {
				// Set the text of the clicked button equal to O
				clickedButton.setText("O");
				// Now its the first player's turn
				isFirstPlayer = true;
				// Check if someone won the game
				didSomeoneWin = find3InRow();
				// If someone won
				if (didSomeoneWin == true) {
					displayWinScreen();
				}
			}
			// If the user is in Easy AI mode
		} else if (mode == 1) {
			// If the first player clicked on an empty square
			if ("".equals(buttonLabel) && isFirstPlayer) {
				// Set the text of the clicked button to X
				clickedButton.setText("X");
				// Check if someone won
				didSomeoneWin = find3InRow();
				// Now it's the AI's turn
				isFirstPlayer = false;
				// If someone won then display the win screen
				if (didSomeoneWin == true) {
					displayWinScreen();
				}
				// If there are still empty spots make the ai go
				if (didSomeoneWin == false && howManyLeft != 0) {
					easyAI();
				}
			}
			// If the user is in Hard AI mode
		} else if (mode == 2) {
			if ("".equals(buttonLabel) && isFirstPlayer) {
				clickedButton.setText("X");
				didSomeoneWin = find3InRow();
				isFirstPlayer = false;
				if (didSomeoneWin == true) {
					displayWinScreen();
				}
				if (didSomeoneWin == false && howManyLeft != 0) {
					hardAI();
				}
			}
		}
	}

	// Method to handle the menu button clicks
	public void menuClickHandler(ActionEvent evt) {
		// Store the menuitem that the user clicked and the text of that menuitem into
		// variables
		MenuItem clickedMenu = (MenuItem) evt.getTarget();
		String menuLabel = clickedMenu.getText();

		// If the user chose pvp
		if ("Play (PvP)".equals(menuLabel)) {
			// Set all of the buttons inside of the gameBoard to blank (meaning no X's or
			// O's) and get rid of any additional previous styling
			removeHighlighting();
			isFirstPlayer = true; // new game starts with X
			// make sure the buttons aren't disabled
			disableButtons(false);
			// Set the mode equal to PvP
			mode = 0;
		}
		// If the user chose quit, close all windows
		if ("Quit".equals(menuLabel)) {
			Platform.exit();
		}
		// If the user chose How To Play, open the how to play window
		if ("How To Play".equals(menuLabel)) {
			openHowToWindow();
		}
		// If the use chose about, open the about window
		if ("About".equals(menuLabel)) {
			openAbout();
		}
		// If the user chose the easy ai mode
		if ("Easy AI".equals(menuLabel)) {
			// set the game mode equal to easy ai
			mode = 1;
			// Make sure all buttons are setup correctly
			removeHighlighting();
			isFirstPlayer = true; // new game starts with X
			disableButtons(false);
		}
		// If the user chose hard ai
		if ("Hard AI".equals(menuLabel)) {
			mode = 2;
			removeHighlighting();
			isFirstPlayer = true; // new game starts with X
			disableButtons(false);
		}
		// If the user chose to change the theme
		if ("Change Theme".equals(menuLabel) && changeTheme == 0) {
			// Add the new border style class to the border of the game screen
			border1.getStyleClass().add("change-theme-border");
			// remove any previous highlighting from wins
			removeHighlighting();
			// Add the new button style class to the buttons, also remove the previous style
			// class
			ObservableList<Node> buttons = gameBoard.getChildren();
			buttons.forEach(btn -> {
				((Button) btn).setText("");
				btn.getStyleClass().add("change-theme-buttons");
				btn.getStyleClass().remove("back-to-normal");
			});
			// Set the variable change theme equal to 1
			changeTheme = 1;
			// Make sure it is player 1's turn
			isFirstPlayer = true;
			// Reactivate the buttons
			disableButtons(false);
			// If the user wants to switch back to the original theme
		} else if ("Change Theme".equals(menuLabel) && changeTheme == 1) {
			// Remove the newer theme from the border
			border1.getStyleClass().remove("change-theme-border");
			// Remove any highlighting
			removeHighlighting();
			// Add the back to normal style class to the buttons so that they appear as the
			// default theme
			ObservableList<Node> buttons = gameBoard.getChildren();
			buttons.forEach(btn -> {
				((Button) btn).setText("");
				btn.getStyleClass().add("back-to-normal");
			});
			// Set the change theme variable equal to 0
			changeTheme = 0;
			// Make sure it is the first player's turn
			isFirstPlayer = true;
			// Reactivate all of the buttons
			disableButtons(false);
		}
	}

	// Method to display the win screen
	private void displayWinScreen() {
		try {
			// Declare an anchorpane and a scene for this window
			AnchorPane root3 = (AnchorPane) FXMLLoader.load(getClass().getResource("YouWon.fxml"));
			Scene scene3 = new Scene(root3, 398, 316);
			// Add the .css to this new scene
			scene3.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			// Declare the stage that will hold the scene
			quaternaryStage = new Stage();
			quaternaryStage.setScene(scene3);
			quaternaryStage.setResizable(false);
			quaternaryStage.showAndWait();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method to display the how to play screen
	private void openHowToWindow() {
		try {
			AnchorPane root1 = (AnchorPane) FXMLLoader.load(getClass().getResource("HowToPlay.fxml"));
			Scene scene1 = new Scene(root1, 398, 316);
			scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			secondaryStage = new Stage();
			secondaryStage.setScene(scene1);
			secondaryStage.setResizable(false);
			secondaryStage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// method to display the about window
	private void openAbout() {
		try {
			AnchorPane root2 = (AnchorPane) FXMLLoader.load(getClass().getResource("About.fxml"));
			Scene scene2 = new Scene(root2, 398, 316);
			scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			tertiaryStage = new Stage();
			tertiaryStage.setScene(scene2);
			tertiaryStage.setResizable(false);
			tertiaryStage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// The method that handles the easy ai
	public void easyAI() {
		// Store all of the buttons, and the texts of those buttons into arrays
		String[] buttonText = { b1.getText(), b2.getText(), b3.getText(), b4.getText(), b5.getText(), b6.getText(),
				b7.getText(), b8.getText(), b9.getText() };
		Button[] button = { b1, b2, b3, b4, b5, b6, b7, b8, b9 };

		// Declare a variable to store the ai's choice
		int aiChoose = (int) (Math.random() * 9);

		// This while loop makes sure that the ai does not choose a square that is
		// already occuped by an X or an O
		while (true) {
			if ("X".equals(buttonText[aiChoose]) || "O".equals(buttonText[aiChoose])) {
				aiChoose = (int) (Math.random() * 9);
			} else {
				break;
			}
		}

		// Set the text of the square that the ai chose equal to O
		button[aiChoose].setText("O");
		// check if someone won
		didSomeoneWin = find3InRow();
		// Now it's the first player's turn
		isFirstPlayer = true;
		// if someone won, display the win screen
		if (didSomeoneWin == true) {
			displayWinScreen();
		}
	}

	// This is the method that handles the hard ai
	public void hardAI() {
		// Store all of the buttons and the texts of those button into arrays
		String[] buttonText = { b1.getText(), b2.getText(), b3.getText(), b4.getText(), b5.getText(), b6.getText(),
				b7.getText(), b8.getText(), b9.getText() };
		Button[] button = { b1, b2, b3, b4, b5, b6, b7, b8, b9 };

		// Make the ai's choice into a number (i randomly chose 99)
		int aiChoose = 99;

		// Here I check if the first player is about to win by having 2 squares in the
		// same row, and I make the ai place an O in that row to disrupt the win
		for (int i = 0; i < 9; i += 3) {
			if ("X".equals(buttonText[i]) && buttonText[i] == buttonText[i + 1]) {
				aiChoose = i + 2;
				break;
			}
			if ("X".equals(buttonText[i]) && buttonText[i] == buttonText[i + 2]) {
				aiChoose = i + 1;
				break;
			}
			if ("X".equals(buttonText[i + 1]) && buttonText[i + 1] == buttonText[i + 2]) {
				aiChoose = i;
				break;
			}
		}

		// Here I check if the first player is about to win by having 2 squares in the
		// same column, and I make the ai place an O in that column to disrupt the win
		for (int i = 0; i < 3; i++) {
			if ("X".equals(buttonText[i]) && buttonText[i] == buttonText[i + 3]) {
				aiChoose = i + 6;
				break;
			}
			if ("X".equals(buttonText[i]) && buttonText[i] == buttonText[i + 6]) {
				aiChoose = i + 3;
				break;
			}
			if ("X".equals(buttonText[i + 3]) && buttonText[i + 3] == buttonText[i + 6]) {
				aiChoose = i;
				break;
			}
		}

		// Here I check if the first player is about to win by having 2 squares in the
		// same diagonal, and I make the ai place an O in that diagonal to disrupt the
		// win
		if ("X".equals(buttonText[0]) && buttonText[0] == buttonText[8]) {
			aiChoose = 4;
		}
		if ("X".equals(buttonText[0]) && buttonText[0] == buttonText[4]) {
			aiChoose = 8;
		}
		if ("X".equals(buttonText[4]) && buttonText[4] == buttonText[8]) {
			aiChoose = 0;
		}

		if ("X".equals(buttonText[2]) && buttonText[2] == buttonText[6]) {
			aiChoose = 4;
		}
		if ("X".equals(buttonText[2]) && buttonText[2] == buttonText[4]) {
			aiChoose = 6;
		}
		if ("X".equals(buttonText[4]) && buttonText[4] == buttonText[6]) {
			aiChoose = 2;
		}

		// Here I check if the ai has 2 O's in the same row, and I place another O into
		// that row to make the ai win
		for (int i = 0; i < 9; i += 3) {
			if ("O".equals(buttonText[i]) && buttonText[i] == buttonText[i + 1]) {
				aiChoose = i + 2;
				break;
			}
			if ("O".equals(buttonText[i]) && buttonText[i] == buttonText[i + 2]) {
				aiChoose = i + 1;
				break;
			}
			if ("O".equals(buttonText[i + 1]) && buttonText[i + 1] == buttonText[i + 2]) {
				aiChoose = i;
				break;
			}
		}

		// Here I check if the ai has 2 O's in the same column, and I place another O
		// into that column to make the ai win
		for (int i = 0; i < 3; i++) {
			if ("O".equals(buttonText[i]) && buttonText[i] == buttonText[i + 3]) {
				aiChoose = i + 6;
				break;
			}
			if ("O".equals(buttonText[i]) && buttonText[i] == buttonText[i + 6]) {
				aiChoose = i + 3;
				break;
			}
			if ("O".equals(buttonText[i + 3]) && buttonText[i + 3] == buttonText[i + 6]) {
				aiChoose = i;
				break;
			}
		}

		// Here I check if the ai has 2 O's in the same diagonal and I place another O
		// in that diagonal to make the ai win
		if ("O".equals(buttonText[0]) && buttonText[0] == buttonText[8]) {
			aiChoose = 4;
		}
		if ("O".equals(buttonText[0]) && buttonText[0] == buttonText[4]) {
			aiChoose = 8;
		}
		if ("O".equals(buttonText[4]) && buttonText[4] == buttonText[8]) {
			aiChoose = 0;
		}

		if ("O".equals(buttonText[2]) && buttonText[2] == buttonText[6]) {
			aiChoose = 4;
		}
		if ("O".equals(buttonText[2]) && buttonText[2] == buttonText[4]) {
			aiChoose = 6;
		}
		if ("O".equals(buttonText[4]) && buttonText[4] == buttonText[6]) {
			aiChoose = 2;
		}

		// If none of the about if statements hold true, I simply let the ai make a
		// random choice
		if (aiChoose == 99 || "X".equals(buttonText[aiChoose]) || "O".equals(buttonText[aiChoose])) {
			aiChoose = (int) (Math.random() * 9);
			while (true) {
				if ("X".equals(buttonText[aiChoose]) || "O".equals(buttonText[aiChoose])) {
					aiChoose = (int) (Math.random() * 9);
				} else {
					break;
				}
			}
		}

		// Set the text of the button that the ai chose equal to O
		button[aiChoose].setText("O");
		// Check if someone won
		didSomeoneWin = find3InRow();
		// Now it is the user's turn
		isFirstPlayer = true;
		// If someone won, display the win screen
		if (didSomeoneWin == true) {
			displayWinScreen();

		}
	}

	// This is the method to close the how to play window
	public void howToCloser(ActionEvent evt) {
		secondaryStage.close();
	}

	// This is the method to close the about window
	public void aboutCloser(ActionEvent evt) {
		tertiaryStage.close();
	}

	// This is the method to close the win screen
	public void winScreenCloser(ActionEvent evt) {
		quaternaryStage.close();
	}

	// This is the method to disable the buttons when someone won or tied
	private void disableButtons(boolean disable) {
		ObservableList<Node> buttons = gameBoard.getChildren();
		buttons.forEach(btn -> {
			((Button) btn).setDisable(disable);
		});
	}

	// This is the method that checks if someone won
	private boolean find3InRow() {
		// Declare variables to store the buttons and the texts of those buttons
		String[] buttonText = { b1.getText(), b2.getText(), b3.getText(), b4.getText(), b5.getText(), b6.getText(),
				b7.getText(), b8.getText(), b9.getText() };
		Button[] button = { b1, b2, b3, b4, b5, b6, b7, b8, b9 };

		// This is a for loop that checks if all of the squares in the same row are the
		// same (i.e. all X's or all O's)
		for (int i = 0; i < 9; i += 3) {
			if ("" != buttonText[i] && buttonText[i] == buttonText[i + 1] && buttonText[i + 1] == buttonText[i + 2]) {
				// If they are the same, highlight the winning combo
				highlightWinningCombo(button[i], button[i + 1], button[i + 2]);
				// disable all of the buttons
				disableButtons(true);
				return true;
			}
		}

		// This is a for loop that checks if all of the squares in the same column are
		// the same
		for (int i = 0; i < 3; i++) {
			if ("" != buttonText[i] && buttonText[i] == buttonText[i + 3] && buttonText[i + 3] == buttonText[i + 6]) {
				highlightWinningCombo(button[i], button[i + 3], button[i + 6]);
				disableButtons(true);
				return true;
			}
		}

		// Here I check if all of the squares in both diagonals are the same
		if ("" != buttonText[0] && buttonText[0] == buttonText[4] && buttonText[4] == buttonText[8]) {
			highlightWinningCombo(b1, b5, b9);
			disableButtons(true);
			return true;
		}
		// Diagonal 2
		if ("" != buttonText[2] && buttonText[2] == buttonText[4] && buttonText[4] == buttonText[6]) {
			highlightWinningCombo(b3, b5, b7);
			disableButtons(true);
			return true;
		}

		// Here I check if a tie has occurred
		for (int i = 0; i < 9; i++) {
			// I count how many tiles are occupied by an X or an O
			if ("" != buttonText[i]) {
				howManyLeft -= 1;
			}
		}
		// If all of the squares are occupied, but no one has won then display a tie
		// screen
		if (howManyLeft == 0) {
			displayWinScreen();
			disableButtons(true);
		} else {
			howManyLeft = 9;
		}

		return false;
	}

	// Here is the method that highlights the winning combo in the game, it simply
	// appends a .css style class to the 3 buttons
	private void highlightWinningCombo(Button first, Button second, Button third) {

		first.getStyleClass().add("winning-button");
		second.getStyleClass().add("winning-button");
		third.getStyleClass().add("winning-button");
	}

	// Her is the method to remove the highlighting and to remove any of the texts
	// on the squares when the game is restarted or when the theme is swapped
	private void removeHighlighting() {
		// If the user is in the default theme
		if (changeTheme == 0) {
			// Remove the winning-button style class from the buttons and set the text to ""
			ObservableList<Node> buttons = gameBoard.getChildren();
			buttons.forEach(btn -> {
				((Button) btn).setText("");
				btn.getStyleClass().remove("winning-button");
			});
			// Else (if the user is in the newer theme)
		} else {
			// Set the text of the buttons to "", add the new theme to the buttons, and
			// remove the winning-button style class
			ObservableList<Node> buttons = gameBoard.getChildren();
			buttons.forEach(btn -> {
				((Button) btn).setText("");
				btn.getStyleClass().add("change-theme-buttons");
				btn.getStyleClass().remove("winning-button");

			});
		}

	}

}
