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
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javafx.util.StringConverter;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class TalesAhora extends Application {
    private Button btCalculate = new Button("Calcular");

    private ImageView imageView = new ImageView();
    private Label lbTheta = new Label(new StringBuilder().append(Character.toString(952))
            .append(" = ").toString());
    private Label lbDistance = new Label("D = ");
    private Label lbHeight = new Label("h = ? [m]");

    private TextField tfTheta = new TextField();
    private TextField tfDistance = new TextField();

    @Override
    public void start(Stage primaryStage) {
        lbTheta.setLabelFor(tfTheta);
        lbDistance.setLabelFor(tfDistance);

        Font bigger = Font.font(14);
        Font times = Font.font("Times New Roman", FontWeight.NORMAL, FontPosture.REGULAR, 16);
        Font timesIt = Font.font("Times New Roman", FontWeight.NORMAL, FontPosture.ITALIC, 16);
        btCalculate.setFont(bigger);
        tfTheta.setFont(bigger);
        tfDistance.setFont(bigger);
        lbTheta.setFont(timesIt);
        lbDistance.setFont(timesIt);
        lbHeight.setFont(timesIt);

        Image image = new Image("graph_ahora.png");

        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(500);

        btCalculate.setOnAction(e -> updateHeight());

        tfTheta.setAlignment(Pos.CENTER_RIGHT);
        tfTheta.setOnAction(e -> updateHeight());
        tfTheta.setTextFormatter(getUnitTextFormatter(Character.toString(176)));
        tfTheta.setOnMouseClicked(e -> tfTheta.selectAll());

        tfDistance.setAlignment(Pos.CENTER_RIGHT);
        tfDistance.setOnAction(e -> updateHeight());
        tfDistance.setTextFormatter(getUnitTextFormatter("m"));
        tfDistance.setOnMouseClicked(e -> tfDistance.selectAll());

        GridPane gridPane = new GridPane();
        gridPane.add(lbTheta, 0, 0);
        gridPane.add(tfTheta, 1, 0);
        gridPane.add(lbHeight, 2, 0);
        gridPane.add(lbDistance, 0, 1);
        gridPane.add(tfDistance, 1, 1);
        gridPane.add(btCalculate, 2, 1);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(5);
        GridPane.setHalignment(lbHeight, HPos.CENTER);
        GridPane.setHalignment(btCalculate, HPos.CENTER);
        GridPane.setHalignment(lbTheta, HPos.RIGHT);
        GridPane.setHalignment(lbDistance, HPos.RIGHT);
        btCalculate.setPrefWidth(150);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(imageView);
        borderPane.setBottom(gridPane);
        borderPane.setPadding(new Insets(5));

        Scene scene = new Scene(borderPane, 600, 400);
        primaryStage.setTitle("Tales en la Actualidad");
        primaryStage.getIcons().add(new Image("icono_tales.jpg"));
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(530);
        primaryStage.show();
        primaryStage.setMinHeight(imageView.getBoundsInLocal().getHeight()
                + gridPane.getBoundsInLocal().getHeight() + 50);
    }

    private void updateHeight() {
        double theta = (Double)tfTheta.getTextFormatter().getValue();
        double distance = (Double)tfDistance.getTextFormatter().getValue();

        double height = distance * Math.tan(Math.toRadians(theta));

        lbHeight.setText(String.format("h = %.2f [m]", height));
    }

    private TextFormatter<Double> getUnitTextFormatter(String unit) {
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

        return new TextFormatter<>(converter, 0.0, filter);
    }
}
