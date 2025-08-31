import java.util.List;
public interface IDecisionService {
    void addDecision(Decision decision);
    List<Decision> getAllDecisions();
    List<Decision> searchByComponent(String component);
    List<Decision> searchByPerson(String person);
}
//zasada odwrócenia zależności. Odpowiada za logikę biznesową związaną z decyzjami.
// To oznacza, że zajmuje się operacjami, które są związane z przetwarzaniem danych,
// walidacją, a także koordynowaniem działań między różnymi komponentami aplikacji.