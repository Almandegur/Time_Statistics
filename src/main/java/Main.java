import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

import java.time.LocalDate;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;

public class Main extends Application{
    public static Database database;
    public static ObjectMapper mapper = new ObjectMapper();
    public static File dataFile = new File ("database.json");

    public static void main(String[] args) throws IOException {
        mapper.registerModule(new JSR310Module());
        if (!dataFile.exists())
            database = new Database();
        else {
            database = mapper.readValue(dataFile, Database.class);
            if (!database.LastDay().date.equals(LocalDate.now()))
                database.startDay();
        }
        launch(args);
    }
    public void start (Stage window) throws IOException{
        window.setTitle ("Time statistics");
        window.getIcons().add (new Image("images\\clock.png"));

        Parent canvas = FXMLLoader.load (getClass().getResource("GUI.fxml"));
        Scene page = new Scene (canvas);
        page.getStylesheets().add("stylesheet.css");
        window.setScene (page);
        window.show();
        window.setOnCloseRequest (event -> {try {mapper.writeValue(dataFile, database);}
        catch (IOException e) {RuntimeException ex;}});
    }
}