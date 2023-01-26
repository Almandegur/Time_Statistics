import javafx.scene.chart.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
    // class which gets data from database and fills GUI elements
public class OutputController {
    public Database database = Main.database;
    public PieChart averageDuration;
    public LineChart actionsDynamic;

    public void checkRequest (LocalDate startDate, LocalDate endDate){ // checking is something important happened
        if ((startDate != null && endDate != null)) {
            int start = database.findDayNearestTo(startDate, 0);
            int end = database.findDayNearestTo(endDate,1);
            database.changeAction(database.currentAction);
            fillAverageDuration(start, end);
            fillActionsDynamic(start, end);
        }
    }
    // filling pie chart
    public void fillAverageDuration(int startDay, int endDay){
        averageDuration.getData().clear();
        var statistics = database.getStatistics(startDay, endDay);
        for (String name : statistics.keySet()){
            averageDuration.getData().add (new PieChart.Data (name, statistics.get(name).getSeconds()));
        }
    }
    // filling line chart
    public void fillActionsDynamic(int startDay, int endDay){
        // getting name of actions
        actionsDynamic.getData().clear();
        for (String name : database.LastDay().actions.keySet()){
            var dataSeries = new XYChart.Series<>();
            dataSeries.setName(name);
            actionsDynamic.getData().add (dataSeries);
        }
        // adding them to line chart
        for (int currentDay = startDay; currentDay <= endDay; currentDay++){
            var statistics = database.getStatistics(startDay, currentDay);
            var formatter = DateTimeFormatter.ofPattern("MMM. dd. yyyy");
            int seriesNumber = 0;
            for (String name : statistics.keySet()){
                var dataSeries = (XYChart.Series) actionsDynamic.getData().get(seriesNumber);
                var date = database.diary.get(currentDay).date.format(formatter);
                var totalTimeSpent = database.diary.get(endDay).timeSpent .minus (database.diary.get(currentDay - 1).timeSpent);
                dataSeries.getData().add(new XYChart.Data(date, (double)statistics.get(name).getSeconds() / totalTimeSpent.getSeconds()));
                seriesNumber++;
            }
        }
    }
}