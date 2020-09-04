import jdk.jshell.spi.ExecutionControlProvider;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;

    public ReizigerDAOPsql(Connection conn){
        this.conn = conn;
    }

    public boolean save(Reiziger reiziger){
        try{
            // Create a SQL Query
            String sqlQuery = "INSERT INTO reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?) ";

            // Create a Statement
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setInt(1, reiziger.getId());
            st.setString(2, reiziger.getVoorletters());
            st.setString(3, reiziger.getTussenvoegsel());
            st.setString(4, reiziger.getAchternaam());
            st.setDate(5, (Date) reiziger.getGeboortedatum());

            st.executeUpdate();
            st.close();

            return true;
            /*            // 2. Create a statement
            Statement st = connection.createStatement();

            // 3. Execute SQL query
            ResultSet rs = st.executeQuery("SELECT * FROM reiziger");

            // 4. Process the result set
            System.out.println("Alle reizigers:");
            while (rs.next()){
                System.out.println(rs.getString("reiziger_id") + " " + rs.getString("voorletters") + ". " + rs.getString("achternaam"));
            }*/
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Reiziger reiziger){
        try{
            // Create a SQL Query
            String sqlQuery = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ?) WHERE reiziger_id = ?";

            // Create a Statement
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setString(1, reiziger.getVoorletters());
            st.setString(2, reiziger.getTussenvoegsel());
            st.setString(3, reiziger.getAchternaam());
            st.setDate(4, (Date) reiziger.getGeboortedatum());
            st.setInt(5, reiziger.getId());

            st.executeUpdate(sqlQuery);
            st.close();

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(Reiziger reiziger){
        try{
            // Create a SQL Query
            String sqlQuery = "DELETE FROM reiziger WHERE reiziger_id = ?";

            // Create a Statement
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setInt(1, reiziger.getId());

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
                return new Reiziger(rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum"));
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
            ResultSet rs = st.executeQuery();
            ArrayList<Reiziger> reizigers = new ArrayList<>();
            while(rs.next()){

                int id = rs.getInt("reiziger_id");
                String voorletters = rs.getString("voorletters");
                String tussenvoegsel = rs.getString("tussenvoegsel");
                String achternaam = rs.getString("achternaam");
                Date geboortedatum = rs.getDate("geboortedatum");

                reizigers.add(new Reiziger(id, voorletters, tussenvoegsel, achternaam, geboortedatum));
            }
            return reizigers;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Reiziger> findAll(){
        try{
            ArrayList<Reiziger> reizigers = new ArrayList<>();
            String sqlQuery = "SELECT * FROM reiziger";
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            ResultSet rs = st.executeQuery();
            while(rs.next()){

                int id = rs.getInt("reiziger_id");
                String voorletters = rs.getString("voorletters");
                String tussenvoegsel = rs.getString("tussenvoegsel");
                String achternaam = rs.getString("achternaam");
                Date geboortedatum = rs.getDate("geboortedatum");

                reizigers.add(new Reiziger(id, voorletters, tussenvoegsel, achternaam, geboortedatum));
            }
            return reizigers;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
        /*
         while (rs.next()) {
          int i = rs.getInt("userid");
          String str = rs.getString("username");

          //Assuming you have a user object
          User user = new User(i, str);

          ll.add(user);
        }
        */
    }
}