import jdk.jshell.spi.ExecutionControlProvider;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
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
            PreparedStatement pStOne = conn.prepareStatement(sqlQuery);
            pStOne.setInt(1, product.getId());
            pStOne.setString(2, product.getNaam());
            pStOne.setString(3, product.getBeschrijving());
            pStOne.setFloat(4, product.getPrijs());

            pStOne.executeUpdate();
            pStOne.close();

            if(!kaarten.isEmpty()){
                for(OVChipkaart kaart : kaarten){
                    System.out.println("hey");
                    ovdao.save(kaart);
                    ovdao.findById(kaart.getId()).addProduct(product);
                    saveCardForProduct(kaart, product);
                }
            }

            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Product product) {
        try{
            List<OVChipkaart> kaarten = product.getKaarten();
            // Create a SQL Query
            String queryDeleteOvChipKaartProduct = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";

            PreparedStatement prepStatementOne = conn.prepareStatement(queryDeleteOvChipKaartProduct);

            prepStatementOne.setInt(1, product.getId());

            prepStatementOne.executeUpdate();
            prepStatementOne.close();

            String queryUpdateProduct = "UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?";

            // Create a Statement
            PreparedStatement prepStatementTwo = conn.prepareStatement(queryUpdateProduct);
            prepStatementTwo.setString(1, product.getNaam());
            prepStatementTwo.setString(2, product.getBeschrijving());
            prepStatementTwo.setFloat(3,  product.getPrijs());
            prepStatementTwo.setInt(4, product.getId());

            prepStatementTwo.executeUpdate();
            prepStatementTwo.close();

            if(!kaarten.isEmpty()){
                for(OVChipkaart kaart: kaarten){
                    saveCardForProduct(kaart, product);
                }
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private boolean saveCardForProduct(OVChipkaart kaart, Product product){
        try{
            String queryAddOVChipkaartProduct = "INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer, status, last_update)" +
                    "VALUES (?, ?, ?, ?)";

            PreparedStatement pStTwo= conn.prepareStatement(queryAddOVChipkaartProduct);

            pStTwo.setInt(1, kaart.getId());
            pStTwo.setInt(2, product.getId());
            pStTwo.setString(3, "gekocht");
            pStTwo.setDate(4, Date.valueOf(LocalDate.now()));

            pStTwo.executeUpdate();
            pStTwo.close();

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
            String sqlQuery = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?; DELETE FROM product WHERE product_nummer = ?";

            // Create a Statement
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setInt(1, product.getId());
            st.setInt(2, product.getId());

            st.executeUpdate();
            st.close();

            if(!kaarten.isEmpty()){
                for(OVChipkaart kaart : kaarten){
                    product.deleteCard(kaart);
                    ovdao.findById(kaart.getId()).deleteProduct(product);
                    ovdao.delete(kaart);
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
