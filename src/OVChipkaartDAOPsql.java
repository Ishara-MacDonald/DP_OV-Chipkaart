import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO{

    private Connection conn;
    private OVChipkaartDAO ovdao;
    private Reiziger reiziger;


    public OVChipkaartDAOPsql(Connection conn){
        this.conn = conn;
    }

    public void setOvdao(OVChipkaartDAO ovdao) {
        this.ovdao = ovdao;
    }

    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        ArrayList<OVChipkaart> kaarten = new ArrayList<>();
        try{
            String sqlQuery = "SELECT * FROM adres WHERE reiziger_id = ?";
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setInt(1, reiziger.getId());
            ResultSet rs = st.executeQuery();

            while(rs.next()){

                kaarten.add(new OVChipkaart(rs.getInt("kaart_nummer"),
                        rs.getDate("geldig_tot"),
                        rs.getInt("klasse"),
                        rs.getInt("saldo"),
                        rs.getInt("reiziger_id")));
            }
            return kaarten;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;

    }
}
