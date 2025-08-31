import java.io.Serializable;
import java.time.LocalDate;
//pojedyńcza decyzja w systemie
public class Decision implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDate date;
    private String component;
    private String person;
    private int importance;
    private String description;

    public Decision(LocalDate date, String component, String person, int importance, String description) {
        this.date = date;
        this.component = component;
        this.person = person;
        this.importance = importance;
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getComponent() {
        return component;
    }

    public String getPerson() {
        return person;
    }

    public int getImportance() {
        return importance;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Decyzja{" +
                "data=" + date +
                ", komponent='" + component + '\'' +
                ", osoba='" + person + '\'' +
                ", ważność=" + importance +
                ", opis='" + description + '\'' +
                '}';
    }
}
