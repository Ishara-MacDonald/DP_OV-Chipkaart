//pgAdmin

import java.sql.*;
import java.util.List;

public class Driver {
    private static Connection connection;

    public static void main(String[] args) {

        try{
            // 1. Get a connection to database
            connection = DriverManager.getConnection("jdbc:postgresql://localhost/ovchip", "postgres", "PostGres");
            testReizigerDAO(new ReizigerDAOPsql(connection));
//            // 2. Create a statement
//            Statement st = connection.createStatement();
//
//            // 3. Execute SQL query
//            ResultSet rs = st.executeQuery("SELECT * FROM reiziger");
//
//            // 4. Process the result set
//            System.out.println("Alle reizigers:");
//            while (rs.next()){
//                System.out.println(rs.getString("reiziger_id") + " " + rs.getString("voorletters") + ". " + rs.getString("achternaam"));
//            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static Connection getConnection(){ return connection; }

    private static void closeConnection() throws SQLException { connection.close(); }

    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     * @throws SQLException
     */
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        System.out.println(rdao.findAll().size() + " reizigers");
        System.out.println();

        // Vind de nieuwe reiziger in de database en geef deze terug
        System.out.println(String.format("[Test] Systeem vind reiziger de volgende reiziger bij ReizigerDAO.findById(77):\n %s", rdao.findById(77).toString()));
        System.out.println();

        List<Reiziger> reizigersByBirth = rdao.findByGbdatum("2002-12-03");
        System.out.println("[Test] ReizigerDAO.findByGbdatum('2002-12-03') geeft de volgende reizigers:");
        for (Reiziger r : reizigersByBirth) {
            System.out.println(r);
        }
        System.out.println();


        // Verwijder de nieuwe reiziger en haal deze uit de database
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        System.out.println(rdao.findAll().size() + " reizigers");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
    }
}
