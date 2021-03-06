import java.sql.Connection;
import java.util.List;

interface ReizigerDAO {
    public void setAdao(AdresDAO adao);
    public void setOVdao(OVChipkaartDAO ovdao);

    public boolean save(Reiziger reiziger);
    public boolean update(Reiziger reiziger);
    public boolean delete(Reiziger reiziger);

    public Reiziger findById(int id);
    public List<Reiziger> findByGbdatum(String datum);
    public List<Reiziger> findAll();
}
