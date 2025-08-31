import java.util.List;
//Zawiera metody do dodawania decyzji, pobierania wszystkich decyzji oraz wyszukiwania decyzji według komponentu i osoby.
public interface IDecisionRepository {
    void add(Decision decision);
    List<Decision> getAll();
    List<Decision> searchByComponent(String component);
    List<Decision> searchByPerson(String person);
}



// Odpowiada za dostęp do danych. Jego głównym celem jest zarządzanie operacjami związanymi
// z przechowywaniem, pobieraniem
// i wyszukiwaniem danych w określonym źródle (np. plik, baza danych).