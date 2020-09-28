import java.util.List;

interface ProductDAO {

    public void setOVdao(OVChipkaartDAO ovdao);

    public boolean save(Product product);
    public boolean update(Product product);
    public boolean delete(Product product);

    public Product findById(int id);
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart);
    public List<Product> findAll();

}
