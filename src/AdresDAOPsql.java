import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO{

    private Connection conn;
    private ReizigerDAO rdao;

    public AdresDAOPsql(Connection conn){
        this.conn = conn;
    }

    public void setRdao(ReizigerDAO rdao) {
        this.rdao = rdao;
    }

    public boolean save(Adres adres){
        try{
            // Create a SQL Query
            String sqlQuery = "INSERT INTO adres(adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?) ";

            // Create a Statement
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setInt(1, adres.getId());
            st.setString(2, adres.getPostcode());
            st.setString(3, adres.getHuisnummer());
            st.setString(4, adres.getStraat());
            st.setString(5, adres.getWoonplaats());
            st.setInt(6, adres.getReiziger_id());

            st.executeUpdate();
            st.close();

            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Adres adres){
        try{
            // Create a SQL Query
            String sqlQuery = "UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? WHERE adres_id = ?";

            // Create a Statement
            PreparedStatement st = conn.prepareStatement(sqlQuery);

            st.setString(1, adres.getPostcode());
            st.setString(2, adres.getHuisnummer());
            st.setString(3, adres.getStraat());
            st.setString(4, adres.getWoonplaats());
            st.setInt(5, adres.getReiziger_id());
            st.setInt(6, adres.getId());

            st.executeUpdate();
            st.close();

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(Adres adres){
        try{
            // Create a SQL Query
            String sqlQuery = "DELETE FROM adres WHERE adres_id = ?";

            // Create a Statement
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setInt(1, adres.getId());

            st.executeUpdate();
            st.close();

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public Adres findById(int id){
        try{
            String sqlQuery = "SELECT * FROM adres WHERE adres_id = ?";
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if(rs.next()){
                return new Adres(rs.getInt("adres_id"),
                        rs.getString("postcode"),
                        rs.getString("huisnummer"),
                        rs.getString("straat"),
                        rs.getString("woonplaats"),
                        rs.getInt("reiziger_id"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Adres findByReiziger(Reiziger reiziger) {
        try{
            String sqlQuery = "SELECT * FROM adres WHERE reiziger_id = ?";
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setInt(1, reiziger.getId());
            ResultSet rs = st.executeQuery();

            if(rs.next()){
                return new Adres(rs.getInt("adres_id"),
                        rs.getString("postcode"),
                        rs.getString("huisnummer"),
                        rs.getString("straat"),
                        rs.getString("woonplaats"),
                        rs.getInt("reiziger_id"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Adres> findAll() {
        ArrayList<Adres> adressen = new ArrayList<>();
        try{
            String sqlQuery = "SELECT * FROM adres";
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            ResultSet rs = st.executeQuery();

            while(rs.next()){
                int id = rs.getInt("adres_id");
                String postcode = rs.getString("postcode");
                String huisnummer = rs.getString("huisnummer");
                String straat = rs.getString("straat");
                String woonplaats = rs.getString("woonplaats");
                int reiziger_id = rs.getInt( "reiziger_id");

                Adres newAdres = new Adres(id, postcode, huisnummer, straat, woonplaats, reiziger_id);
                adressen.add(newAdres);
            }
            return adressen;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
