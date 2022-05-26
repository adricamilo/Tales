import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javafx.util.StringConverter;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class TalesAntiguo extends Application {
    private ImageView imageView = new ImageView();
    private Label lbHeightValue = new Label();
    private Slider slHeightValue = new Slider();
    private Label lbThalesValue = new Label();
    private Slider slThalesValue = new Slider();
    private Label lbHeightThales = new Label("h = ? [tales]");

    private CheckBox cbThalesToMeters = new CheckBox("Convertir a metros");
    private Label lbThalesToMeters = new Label("1 [tales] = ");
    private TextField tfThalesToMeters = new TextField();
    private Label lbHeightMeters = new Label("h = ? [m]");

    @Override
    public void start(Stage primaryStage) {
        Font bigger = Font.font(14);
        Font timesIt = Font.font("Times New Roman", FontWeight.NORMAL, FontPosture.ITALIC, 16);
        cbThalesToMeters.setFont(bigger);
        tfThalesToMeters.setFont(bigger);
        lbThalesToMeters.setFont(timesIt);
        lbHeightValue.setFont(timesIt);
        lbThalesValue.setFont(timesIt);
        lbHeightMeters.setFont(timesIt);
        lbHeightThales.setFont(timesIt);

        Image image = new Image("graph_antiguo.png");

        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(250);

        slThalesValue.valueProperty().addListener((observable, oldValue, newValue) -> {
            lbThalesValue.setText(String.format("n = %.0f", newValue));
        });
        slHeightValue.valueProperty().addListener((observable, oldValue, newValue) -> {
            lbHeightValue.setText(String.format("m = %.0f", newValue));
        });

        slThalesValue.setMin(1);
        slThalesValue.setMax(15);
        slThalesValue.setShowTickMarks(true);
        slThalesValue.setSnapToTicks(true);
        slThalesValue.setMajorTickUnit(1);
        slThalesValue.setMinorTickCount(0);
        slThalesValue.setValue(1);

        slHeightValue.minProperty().bind(slThalesValue.valueProperty());
        slHeightValue.setMax(15);
        slHeightValue.setShowTickMarks(true);
        slHeightValue.setSnapToTicks(true);
        slHeightValue.setMajorTickUnit(1);
        slHeightValue.setMinorTickCount(0);
        slHeightValue.setValue(1);

        slThalesValue.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateHeight();
        });
        slHeightValue.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateHeight();
        });

        slHeightValue.minProperty().addListener((observable, oldValue, newValue) -> {
            slHeightValue.setValue(Math.rint(slHeightValue.getValue()));
        });

        VBox heightVBox = new VBox();
        heightVBox.getChildren().addAll(lbHeightValue, slHeightValue);
        heightVBox.setAlignment(Pos.CENTER);

        VBox thalesVBox = new VBox();
        thalesVBox.getChildren().addAll(lbThalesValue, slThalesValue);
        thalesVBox.setAlignment(Pos.CENTER);

        lbThalesToMeters.setLabelFor(tfThalesToMeters);
        lbThalesToMeters.setVisible(false);
        tfThalesToMeters.setAlignment(Pos.CENTER_RIGHT);
        tfThalesToMeters.setOnAction(e -> updateHeight());
        tfThalesToMeters.setTextFormatter(getUnitTextFormatter("m", 1.0));
        tfThalesToMeters.setOnMouseClicked(e -> tfThalesToMeters.selectAll());
        tfThalesToMeters.setVisible(false);
        lbHeightMeters.setVisible(false);

        cbThalesToMeters.setAllowIndeterminate(false);
        cbThalesToMeters.setSelected(false);

        cbThalesToMeters.selectedProperty().addListener((obs, oldValue, newValue) ->{
            lbThalesToMeters.setVisible(newValue);
            tfThalesToMeters.setVisible(newValue);
            lbHeightMeters.setVisible(newValue);
            if (newValue)
                tfThalesToMeters.requestFocus();
        });

        GridPane gridPane = new GridPane();
        gridPane.add(heightVBox, 0, 0, 2, 1);
        gridPane.add(lbHeightThales, 2, 0);
        gridPane.add(thalesVBox, 0, 1, 2, 1);
        gridPane.add(cbThalesToMeters, 2, 1);
        gridPane.add(lbThalesToMeters, 0, 2);
        gridPane.add(tfThalesToMeters, 1, 2);
        gridPane.add(lbHeightMeters, 2, 2);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(5);
        GridPane.setHalignment(lbHeightThales, HPos.CENTER);
        GridPane.setHalignment(lbHeightMeters, HPos.CENTER);
        GridPane.setHalignment(cbThalesToMeters, HPos.CENTER);
        GridPane.setHalignment(heightVBox, HPos.CENTER);
        GridPane.setHalignment(thalesVBox, HPos.CENTER);
        GridPane.setHalignment(lbThalesToMeters, HPos.RIGHT);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(imageView);
        borderPane.setBottom(gridPane);
        borderPane.setPadding(new Insets(5));

        Scene scene = new Scene(borderPane, 600, 400);
        primaryStage.setTitle(new StringBuilder().append("Tales en la Antig")
                .append(Character.toString(252)).append("edad").toString());
        primaryStage.getIcons().add(new Image("icono_tales.jpg"));
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinWidth(imageView.getBoundsInLocal().getWidth() + 30);
        primaryStage.setMinHeight(imageView.getBoundsInLocal().getHeight()
                + gridPane.getBoundsInLocal().getHeight() + 60);
    }

    private void updateHeight() {
        double m = slHeightValue.getValue();
        double n = slThalesValue.getValue();

        double heightThales = m / n + 1.0;
        double heightMeters = heightThales * (Double)tfThalesToMeters.getTextFormatter().getValue();

        lbHeightThales.setText(String.format("h = %.2f [tales]", heightThales));
        lbHeightMeters.setText(String.format("h = %.2f [m]", heightMeters));
    }

    private TextFormatter<Double> getUnitTextFormatter(String unit, double initialValue) {
        String pattern = new StringBuilder()
            .append("(([1-9][0-9]*)|0)?(,[0-9]*[Ee]?[0-9]*)?( *\\[")
            .append(unit).append("\\])?").toString();
        Pattern validEditingState = Pattern.compile(pattern);

        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) {
                return c;
            } else {
                return null;
            }
        };

        StringConverter<Double> converter = new StringConverter<Double>() {
            @Override
            public Double fromString(String s) {
                String emptyPattern = new StringBuilder().append(",?[Ee]? *\\[")
                    .append(unit).append("\\]").toString();
                if (s.isEmpty() || s.matches(",[Ee]?") || s.matches(emptyPattern)) {
                    return 0.0;
                } else {
                    String value = s.replaceFirst(new StringBuilder().append(" *\\[")
                            .append(unit).append("\\]").toString(), "").replace(',', '.')
                        .trim();
                    if (value.endsWith("E") || value.endsWith("e"))
                        return Double.valueOf(value.substring(0,
                                    value.length() - 1));
                    return Double.valueOf(value);
                }
            }

            @Override
            public String toString(Double d) {
                String commaValue = d.toString().replace('.', ',');
                return new StringBuilder().append(commaValue).append(" [")
                    .append(unit).append("]").toString();
            }
        };

        return new TextFormatter<>(converter, initialValue, filter);
    }
}
