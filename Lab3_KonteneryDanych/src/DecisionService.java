import java.util.List;
// zawiera logikę biznesową związaną z zarządzaniem decyzjami.
public class DecisionService implements IDecisionService {
    private final IDecisionRepository decisionRepository;

    public DecisionService(IDecisionRepository decisionRepository) {
        this.decisionRepository = decisionRepository;
    }

    @Override
    public void addDecision(Decision decision) {
        decisionRepository.add(decision);
    }

    @Override
    public List<Decision> getAllDecisions() {
        return decisionRepository.getAll();
    }

    @Override
    public List<Decision> searchByComponent(String component) {
        return decisionRepository.searchByComponent(component);
    }

    @Override
    public List<Decision> searchByPerson(String person) {
        return decisionRepository.searchByPerson(person);
    }
}