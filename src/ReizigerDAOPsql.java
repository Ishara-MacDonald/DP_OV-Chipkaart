import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;
    private AdresDAO adao;
    private OVChipkaartDAO ovdao;

    public ReizigerDAOPsql(Connection conn){
        this.conn = conn;
    }

    public void setAdao(AdresDAO adao) {
        this.adao = adao;
    }
    public void setOVdao(OVChipkaartDAO ovdao) {
        this.ovdao = ovdao;
    }

    // Hoe moet je een adres opslaan in save(Reiziger reiziger) ?
    public boolean save(Reiziger reiziger){
        try{
            List<OVChipkaart> newKaarten = reiziger.getKaarten();
            Adres newAdres = reiziger.getAdres();
            // Create a SQL Query
            String sqlQuery = "INSERT INTO reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)";

            // Create a Statement
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setInt(1, reiziger.getId());
            st.setString(2, reiziger.getVoorletters());
            st.setString(3, reiziger.getTussenvoegsel());
            st.setString(4, reiziger.getAchternaam());
            st.setDate(5, (Date) reiziger.getGeboortedatum());

            st.executeUpdate();
            st.close();
            if( newAdres != null){ adao.save(newAdres); }
            if(!newKaarten.isEmpty()){
                for(OVChipkaart kaart : newKaarten){ ovdao.save(kaart); }
            }

            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Reiziger reiziger){
        try{
            List<OVChipkaart> newKaarten = reiziger.getKaarten();
            Adres newAdres = reiziger.getAdres();
            // Create a SQL Query
            String sqlQuery = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";

            // Create a Statement
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setString(1, reiziger.getVoorletters());
            st.setString(2, reiziger.getTussenvoegsel());
            st.setString(3, reiziger.getAchternaam());
            st.setDate(4, (Date) reiziger.getGeboortedatum());
            st.setInt(5, reiziger.getId());

            st.executeUpdate();
            st.close();

            if( reiziger.getAdres() != null){
                if(adao.findById(newAdres.getId()) != null){
                    adao.update(newAdres);
                }else{ adao.save(newAdres); }
            }
            if(!newKaarten.isEmpty()){
                for(OVChipkaart kaart : newKaarten){
                    if(adao.findById(kaart.getId()) != null){
                        ovdao.update(kaart);
                    }else{ ovdao.save(kaart); }
                }
            }

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(Reiziger reiziger){
        try{
            List<OVChipkaart> newKaarten = reiziger.getKaarten();
            Adres newAdres = reiziger.getAdres();
            // Create a SQL Query
            String sqlQuery = "DELETE FROM reiziger WHERE reiziger_id = ?";

            // Create a Statement
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setInt(1, reiziger.getId());

            if( newAdres != null){
                if(ovdao.findById(newAdres.getId()) != null) {
                    adao.delete(newAdres);
                }
            }
            if(!newKaarten.isEmpty()){
                for(OVChipkaart kaart : newKaarten){
                    ovdao.delete(kaart);
                }
            }
            reiziger.deleteOVKaarten();

            st.executeUpdate();
            st.close();

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public Reiziger findById(int id){
        try{
            String sqlQuery = "SELECT * FROM reiziger WHERE reiziger_id = ?";
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if(rs.next()){
                Reiziger newReiziger = new Reiziger(rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum"));

                if( newReiziger.getAdres() != null){
                    newReiziger.setAdres(adao.findByReiziger(newReiziger));
                }

                return newReiziger;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Reiziger> findByGbdatum(String datum){
        try{
            String sqlQuery = "SELECT * FROM reiziger WHERE geboortedatum = ?";
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setDate(1, Date.valueOf(datum));

            return loopThroughDataBase(st.executeQuery());
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Reiziger> findAll(){
        try{
            String sqlQuery = "SELECT * FROM reiziger";
            PreparedStatement st = conn.prepareStatement(sqlQuery);

            return loopThroughDataBase(st.executeQuery());
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Reiziger> loopThroughDataBase(ResultSet rs){
        ArrayList<Reiziger> reizigers = new ArrayList<>();
        try{
            while(rs.next()){

                int id = rs.getInt("reiziger_id");
                String voorletters = rs.getString("voorletters");
                String tussenvoegsel = rs.getString("tussenvoegsel");
                String achternaam = rs.getString("achternaam");
                Date geboortedatum = rs.getDate("geboortedatum");

                Reiziger newReiziger = new Reiziger(id, voorletters, tussenvoegsel, achternaam, geboortedatum);
                newReiziger.setAdres(adao.findByReiziger(newReiziger));
                reizigers.add(newReiziger);
            }
            return reizigers;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
