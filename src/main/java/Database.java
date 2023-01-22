import java.time.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    public ArrayList <Day> diary = new ArrayList<>();
    public String currentAction;
    public LocalTime startTime;

    public Database (){
        diary.add (new Day(-1));
        diary.add (new Day(0));
        currentAction = "doing nothing";
        startTime = LocalTime.now();
    }
    public void startDay(){
        Day newDay = new Day(0);
        newDay.actions = (HashMap<String, Duration>) LastDay().actions.clone();
        newDay.timeSpent = LastDay().timeSpent;
        diary.add (newDay);
    }
    public void changeAction (String newAction){
        var duration = Duration.between(startTime, LocalTime.now());
        LastDay().actions.put (currentAction, LastDay().actions.getOrDefault(newAction, Duration.ZERO).plus(duration));
        LastDay().timeSpent = LastDay().timeSpent.plus(duration);
        currentAction = newAction;
        startTime = LocalTime.now();
    }
    public Day LastDay(){
        return diary.get(diary.size() - 1);
    }
    public HashMap <String, Duration> getStatistics(int startDay, int endDay){
        var oldActions = diary.get(startDay - 1).actions;
        var newActions = diary.get(endDay).actions;
        var statistics = new HashMap <String, Duration>();
        for (String name : newActions.keySet())
            statistics.put (name, newActions.get(name) .minus (oldActions.getOrDefault(name, Duration.ZERO)));
        return statistics;
    }
    public int findDayNearestTo(LocalDate date, int edge){
        int left = 1, middle, right = diary.size() - 1;
        LocalDate guess;
        while (left - right > 1) {
            middle = (left + right) / 2;
            guess = diary.get(middle).date;
            if (guess.isBefore(date))
                left = middle + 1;
            else
                right = middle - 1;
        }
        return new int[]{left, right}[edge];
    }
}