import java.util.List;

interface OVChipkaartDAO {
    public List<OVChipkaart> findByReiziger(Reiziger reiziger);
    public List<OVChipkaart> findAll();


}
