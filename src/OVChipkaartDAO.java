import java.util.List;

interface OVChipkaartDAO {
    public void setRdao(ReizigerDAO rdao);

    public boolean save(OVChipkaart ovChipkaart);
    public boolean update(OVChipkaart ovChipkaart);
    public boolean delete(OVChipkaart ovChipkaart);

    public OVChipkaart findById(int id);
    public List<OVChipkaart> findByReiziger(Reiziger reiziger);
    public List<OVChipkaart> findAll();



}
