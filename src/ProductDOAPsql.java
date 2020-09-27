import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class ProductDOAPsql implements ProductDAO {

    private Connection conn;
    private OVChipkaartDAO ovdao;

    public ProductDOAPsql(Connection conn){
        this.conn = conn;
    }

    public void setOVdao(OVChipkaartDAO ovdao) { this.ovdao = ovdao; }

    public boolean save(Product product) {
        try{
            List<OVChipkaart> kaarten = product.getKaarten();
            // Create a SQL Query
            String sqlQuery = "INSERT INTO product(product_nummer, naam, beschrijving, prijs) VALUES (?, ?, ?, ?)";

            // Create a Statement
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setInt(1, product.getId());
            st.setString(2, product.getNaam());
            st.setString(3, product.getBeschrijving());
            st.setFloat(4, product.getPrijs());

            if(!kaarten.isEmpty()){
                for(OVChipkaart kaart : kaarten){
                    product.addKaart(kaart);
                    ovdao.save(kaart);
                    ovdao.findById(kaart.getId()).addProduct(product);
                }
            }

            st.executeUpdate();
            st.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Product product) {
        try{
            // Create a SQL Query
            String sqlQuery = "UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?";

            // Create a Statement
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setString(1, product.getNaam());
            st.setString(2, product.getBeschrijving());
            st.setFloat(3,  product.getPrijs());
            st.setInt(4,product.getId());

            st.executeUpdate();
            st.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(Product product) {
        try{
            List<OVChipkaart> kaarten = product.getKaarten();
            // Create a SQL Query
            String sqlQuery = "DELETE FROM product WHERE product_nummer = ?";

            // Create a Statement
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setInt(1, product.getId());
            st.executeUpdate();
            st.close();

            if(!kaarten.isEmpty()){
                for(OVChipkaart kaart : kaarten){
                    product.deleteCard(kaart);
                    ovdao.findById(kaart.getId()).deleteProduct(product);
                }
            }

            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public Product findById(int id) {
        try{
            String sqlQuery = "SELECT * FROM product WHERE product_nummer = ?";
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if(rs.next()){
                return new Product(rs.getInt("product_nummer"),
                        rs.getString("naam"),
                        rs.getString("beschrijving"),
                        rs.getFloat("prijs"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Product findByReiziger(Reiziger reiziger) {
        return null;
    }

    public List<Product> findAll() {
        return null;
    }
}
