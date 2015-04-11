package view.buttons;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 * Modal Dialog box used to display any popup messages required - based on the following: https://gist.github.com/jewelsea/1887631
 *
 */
public class Dialog {
	
		
public static void dialogBox(final Stage primaryStage, String msg){ //, Scene scene) {
	
			final Stage dialog = new Stage(StageStyle.TRANSPARENT);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(primaryStage);
			dialog.setScene(
					new Scene(
							HBoxBuilder.create().styleClass("modal-dialog").children(
									LabelBuilder.create().text(msg).build(),
									ButtonBuilder.create().text("Ok, got it").defaultButton(true).onAction(new EventHandler<ActionEvent>() {
										@Override public void handle(ActionEvent actionEvent) {
											primaryStage.getScene().getRoot().setEffect(null);
											dialog.close();
										}
									}).build()
									).build()
									, Color.TRANSPARENT
							)
					);
			//now we need to get the css resource, from a static context, so we must call ourself as a class
			//http://stackoverflow.com/questions/8275499/how-to-call-getclass-from-a-static-method-in-java
			dialog.getScene().getStylesheets().add(Dialog.class.getResource("modal-dialog.css").toExternalForm()); 
			
			// allow the dialog to be dragged around.
			final Node root = dialog.getScene().getRoot();
			final Delta dragDelta = new Dialog.Delta();
			root.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override public void handle(MouseEvent mouseEvent) {
					// record a delta distance for the drag and drop operation.
					dragDelta.x = dialog.getX() - mouseEvent.getScreenX();
					dragDelta.y = dialog.getY() - mouseEvent.getScreenY();
				}
			});
			root.setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override public void handle(MouseEvent mouseEvent) {
					dialog.setX(mouseEvent.getScreenX() + dragDelta.x);
					dialog.setY(mouseEvent.getScreenY() + dragDelta.y);
				}
			});
			
			primaryStage.getScene().getRoot().setEffect(new BoxBlur());
			dialog.show();
		}
		// records relative x and y co-ordinates.
		static class Delta { double x, y; } //http://stackoverflow.com/questions/9744639/must-qualify-the-allocation-with-an-enclosing-instance-of-type-geolocation
	}

