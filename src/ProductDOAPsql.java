import jdk.jshell.spi.ExecutionControlProvider;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductDOAPsql implements ProductDAO {

    private Connection conn;
    private OVChipkaartDAO ovdao;
    private ReizigerDAO rdao;

    public ProductDOAPsql(Connection conn){
        this.conn = conn;
    }

    public void setOVdao(OVChipkaartDAO ovdao) { this.ovdao = ovdao; }
    public void setRdao(ReizigerDAO rdao) { this.rdao = rdao; }

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


            for(OVChipkaart kaart : kaarten){
                ovdao.save(kaart);
                ovdao.findById(kaart.getId()).addProduct(product);
                saveCardForProduct(kaart, product);
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

            if(!kaarten.isEmpty()){
                // Create a SQL Query
                String queryDeleteOvProduct = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?;";

                // Create a Statement
                PreparedStatement prepStatementOne = conn.prepareStatement(queryDeleteOvProduct);

                prepStatementOne.setInt(1, product.getId());

                prepStatementOne.executeUpdate();
                prepStatementOne.close();

                for(OVChipkaart kaart : kaarten){
                    product.deleteCard(kaart);
                    ovdao.findById(kaart.getId()).deleteProduct(product);
                    ovdao.delete(kaart);
                }
            }

            String queryDeleteProduct = "DELETE FROM product WHERE product_nummer = ?;";

            PreparedStatement prepStatementTwo = conn.prepareStatement(queryDeleteProduct);
            prepStatementTwo.setInt(1, product.getId());

            prepStatementTwo.executeUpdate();
            prepStatementTwo.close();
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

    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        try{
            List<Product> producten = new ArrayList<>();

            String queryGetProducts = "SELECT prod.product_nummer, naam, beschrijving, prijs\n" +
                    "FROM product prod\n" +
                    "INNER JOIN ov_chipkaart_product ovproduct\n" +
                    "ON prod.product_nummer = ovproduct.product_nummer\n" +
                    "WHERE ovproduct.kaart_nummer = ?;";

            PreparedStatement st = conn.prepareStatement(queryGetProducts);
            st.setInt(1, ovChipkaart.getId());

            ResultSet rs = st.executeQuery();

            while(rs.next()){
                producten.add( new Product(rs.getInt("product_nummer"),
                        rs.getString("naam"),
                        rs.getString("beschrijving"),
                        rs.getFloat("prijs"))
                );
            }
            return producten;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> findAll() {
        try{
            List<Product> producten = new ArrayList<>();

            String queryGetProducts = "SELECT * FROM product";

            PreparedStatement st = conn.prepareStatement(queryGetProducts);
            ResultSet resultSetProducts = st.executeQuery();
//            product_nummer, naam, beschrijving, prijs
            while(resultSetProducts.next()){
                Product newProduct = new Product(resultSetProducts.getInt("product_nummer"),
                        resultSetProducts.getString("naam"),
                        resultSetProducts.getString("beschrijving"),
                        resultSetProducts.getFloat("prijs"));

                // OVChipkaart koppelen aan product
                String queryGetKaarten = "SELECT kaart.kaart_nummer, geldig_tot, klasse, saldo, reiziger_id\n" +
                        "FROM ov_chipkaart kaart\n" +
                        "INNER JOIN ov_chipkaart_product ovproduct\n" +
                        "ON kaart.kaart_nummer = ovproduct.kaart_nummer\n" +
                        "WHERE ovproduct.product_nummer = ?;";

                PreparedStatement prepStatementTwo = conn.prepareStatement(queryGetKaarten);
                prepStatementTwo.setInt(1, newProduct.getId());
                ResultSet resultSetCards = prepStatementTwo.executeQuery();

                while(resultSetCards.next()){
                    newProduct.addKaart(new OVChipkaart(resultSetCards.getInt("kaart_nummer"),
                            resultSetCards.getDate("geldig_tot"),
                            resultSetCards.getInt("klasse"),
                            resultSetCards.getFloat("saldo"),
                            rdao.findById(resultSetCards.getInt("reiziger_id"))));
                }
                producten.add(newProduct);
            }
            return producten;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
