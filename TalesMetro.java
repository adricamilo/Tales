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

public class TalesMetro extends Application {
    private Button btCalculate = new Button("Calcular");

    private ImageView imageView = new ImageView();
    private Label lbOpposite = new Label("c = ");
    private Label lbAdjacent = new Label("d = ");
    private Label lbDistance = new Label("D = ");
    private Label lbHeight = new Label("h = ? [m]");

    private TextField tfOpposite = new TextField();
    private TextField tfAdjacent = new TextField();
    private TextField tfDistance = new TextField();

    @Override
    public void start(Stage primaryStage) {
        lbOpposite.setLabelFor(tfOpposite);
        lbAdjacent.setLabelFor(tfAdjacent);
        lbDistance.setLabelFor(tfDistance);

        Font bigger = Font.font(14);
        Font timesIt = Font.font("Times New Roman", FontWeight.NORMAL, FontPosture.ITALIC, 16);
        btCalculate.setFont(bigger);
        tfOpposite.setFont(bigger);
        tfAdjacent.setFont(bigger);
        tfDistance.setFont(bigger);
        lbOpposite.setFont(timesIt);
        lbAdjacent.setFont(timesIt);
        lbDistance.setFont(timesIt);
        lbHeight.setFont(timesIt);

        Image image = new Image("graph_metro.png");

        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(300);

        btCalculate.setOnAction(e -> updateHeight());

        tfOpposite.setAlignment(Pos.CENTER_RIGHT);
        tfOpposite.setOnAction(e -> updateHeight());
        tfOpposite.setTextFormatter(getUnitTextFormatter("m"));
        tfOpposite.setOnMouseClicked(e -> tfOpposite.selectAll());

        tfAdjacent.setAlignment(Pos.CENTER_RIGHT);
        tfAdjacent.setOnAction(e -> updateHeight());
        tfAdjacent.setTextFormatter(getUnitTextFormatter("m"));
        tfAdjacent.setOnMouseClicked(e -> tfAdjacent.selectAll());

        tfDistance.setAlignment(Pos.CENTER_RIGHT);
        tfDistance.setOnAction(e -> updateHeight());
        tfDistance.setTextFormatter(getUnitTextFormatter("m"));
        tfDistance.setOnMouseClicked(e -> tfDistance.selectAll());

        GridPane gridPane = new GridPane();
        gridPane.add(lbOpposite, 0, 0);
        gridPane.add(tfOpposite, 1, 0);
        gridPane.add(lbAdjacent, 0, 1);
        gridPane.add(tfAdjacent, 1, 1);
        gridPane.add(lbDistance, 0, 2);
        gridPane.add(tfDistance, 1, 2);
        gridPane.add(lbHeight, 2, 0, 1, 2);
        gridPane.add(btCalculate, 2, 2);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(5);
        GridPane.setHalignment(lbHeight, HPos.CENTER);
        GridPane.setHalignment(btCalculate, HPos.CENTER);
        GridPane.setHalignment(lbOpposite, HPos.RIGHT);
        GridPane.setHalignment(lbAdjacent, HPos.RIGHT);
        GridPane.setHalignment(lbDistance, HPos.RIGHT);
        btCalculate.setPrefWidth(150);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(imageView);
        borderPane.setBottom(gridPane);
        borderPane.setPadding(new Insets(5));

        Scene scene = new Scene(borderPane, 600, 400);
        primaryStage.setTitle(new StringBuilder().append("Tales con un flex")
                .append(Character.toString(243)).append("metro").toString());
        primaryStage.getIcons().add(new Image("icono_tales.jpg"));
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinWidth(imageView.getBoundsInLocal().getWidth() + 30);
        primaryStage.setMinHeight(imageView.getBoundsInLocal().getHeight()
                + gridPane.getBoundsInLocal().getHeight() + 60);
    }

    private void updateHeight() {
        double opposite = (Double)tfOpposite.getTextFormatter().getValue();
        double adjacent = (Double)tfAdjacent.getTextFormatter().getValue();
        double distance = (Double)tfDistance.getTextFormatter().getValue();

        double height = distance * opposite / adjacent;

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
