import java.time.LocalDate;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class Day {
    public LocalDate date;
    public Duration timeSpent = Duration.ZERO;
    public HashMap <String, Duration> actions = new HashMap<>();
    public Day(){}
    public Day (long timeShift){
        date = LocalDate.now().plus(timeShift, ChronoUnit.DAYS);
    }
}