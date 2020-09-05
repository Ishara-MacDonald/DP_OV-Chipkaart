import java.util.List;

interface AdresDAO {
    public void setRdao(ReizigerDAO rdao);

    public boolean save(Adres adres);
    public boolean update(Adres adres);
    public boolean delete(Adres adres);

    public Adres findById(int id);
    public Adres findByReiziger(Reiziger reiziger);
    public List<Adres> findAll();
}
