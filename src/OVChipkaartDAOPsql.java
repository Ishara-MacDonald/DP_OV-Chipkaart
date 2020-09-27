import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO{

    private Connection conn;
    private ReizigerDAO rdao;

    public OVChipkaartDAOPsql(Connection conn){
        this.conn = conn;
    }

    public void setRdao(ReizigerDAO rdao) {
        this.rdao = rdao;
    }

    public boolean save(OVChipkaart ovChipkaart){
        Reiziger reiziger = ovChipkaart.getReiziger();
        try{
            // Create a SQL Query
            String sqlQuery = "INSERT INTO ov_chipkaart(kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)";

            // Create a Statement
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setInt(1, ovChipkaart.getId());
            st.setDate(2, (java.sql.Date) ovChipkaart.getGeldig_tot());
            st.setInt(3, ovChipkaart.getKlasse());
            st.setFloat(4, ovChipkaart.getSaldo());
            st.setInt(5, reiziger.getId());

            reiziger.addOVKaart(ovChipkaart);

            st.executeUpdate();
            st.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(OVChipkaart ovChipkaart){
        Reiziger reiziger = ovChipkaart.getReiziger();
        try{
            // Create a SQL Query
            String sqlQuery = "UPDATE ov_chipkaart SET geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ? WHERE kaart_nummer = ?";

            // Create a Statement
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setDate(1, (java.sql.Date) ovChipkaart.getGeldig_tot());
            st.setInt(2, ovChipkaart.getKlasse());
            st.setFloat(3, ovChipkaart.getSaldo());
            st.setInt(4, reiziger.getId());
            st.setInt(5, ovChipkaart.getId());

            st.executeUpdate();
            st.close();

            reiziger.deleteOVKaart(ovChipkaart);
            reiziger.addOVKaart(ovChipkaart);

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(OVChipkaart ovChipkaart){
        Reiziger reiziger = ovChipkaart.getReiziger();
        try{
            // Create a SQL Query
            String sqlQuery = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";

            // Create a Statement
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setInt(1, ovChipkaart.getId());

            st.executeUpdate();
            st.close();

            reiziger.deleteOVKaart(ovChipkaart);

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public OVChipkaart findById(int id){
        try{
            String sqlQuery = "SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?";
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if(rs.next()){
                return new OVChipkaart(rs.getInt("kaart_nummer"),
                        rs.getDate("geldig_tot"),
                        rs.getInt("klasse"),
                        rs.getFloat("saldo"),
                        rdao.findById(rs.getInt("reiziger_id")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        ArrayList<OVChipkaart> kaarten = new ArrayList<>();
        try{
            String sqlQuery = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setInt(1, reiziger.getId());
            ResultSet rs = st.executeQuery();

            while (rs.next()){
                kaarten.add(new OVChipkaart(rs.getInt("kaart_nummer"),
                        rs.getDate("geldig_tot"),
                        rs.getInt("klasse"),
                        rs.getInt("saldo"),
                        reiziger));
            }
            return kaarten;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<OVChipkaart> findAll() {
        ArrayList<OVChipkaart> kaarten = new ArrayList<>();
        try{
            String sqlQuery = "SELECT * FROM ov_chipkaart";
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            ResultSet rs = st.executeQuery();

            while(rs.next()){
                int kaartnr = rs.getInt("kaart_nummer");
                Date geldig_tot = rs.getDate("geldig_tot");
                int klasse = rs.getInt("klasse");
                float saldo = rs.getFloat("saldo");
                int reiziger_id = rs.getInt("reiziger_id");
                Reiziger newReiziger = rdao.findById(reiziger_id);

                OVChipkaart newOVChipkaart = new OVChipkaart(kaartnr, geldig_tot, klasse, saldo, newReiziger);
                kaarten.add(newOVChipkaart);
            }
            return kaarten;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
