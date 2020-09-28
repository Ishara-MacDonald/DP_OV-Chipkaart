import java.util.List;

interface ProductDAO {

    public void setOVdao(OVChipkaartDAO ovdao);

    public boolean save(Product product);
    public boolean update(Product product);
    public boolean delete(Product product);

    public Product findById(int id);
    public Product findByReiziger(Reiziger reiziger);
    public List<Product> findAll();

}
