//pgAdmin

import java.sql.*;
import java.util.List;

public class Driver {
    private static Connection connection;

    public static void main(String[] args) {

        try{
            // Get a connection to database
            connection = DriverManager.getConnection("jdbc:postgresql://localhost/ovchip", "postgres", "PostGres");
//            testReizigerDAO(new ReizigerDAOPsql(connection));
            testAdresDAO(new AdresDAOPsql(connection));
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
        printReizigerLoop(reizigers);

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

        // Update de nieuwe reiziger in de database en geef deze terug
        sietske.setTussenvoegsel("de");
        sietske.setAchternaam("Boer");
        System.out.println("[Test] ReizigerDAO.update() geeft de volgende resultaten:\nVoor de update:");
        System.out.println(rdao.findById(77));
        System.out.println("Na de update:");
        rdao.update(sietske);
        System.out.println(rdao.findById(77));
        System.out.println();

        // Vind reizigers met de geboortedatum '2002-12-03'
        System.out.println("[Test] ReizigerDAO.findByGbdatum('2002-12-03') geeft de volgende reizigers:");
        printReizigerLoop(rdao.findByGbdatum("2002-12-03"));

        // Verwijder de nieuwe reiziger en haal deze uit de database
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        System.out.println(rdao.findAll().size() + " reizigers");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
    }

    private static void printReizigerLoop(List<Reiziger> listReiziger){
        for(Reiziger r : listReiziger){
            System.out.println(r);
        }
        System.out.println();
    }

    /**
     * P3. Adres DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Adres DAO
     *
     * @throws SQLException
     */
    private static void testAdresDAO(AdresDAO adao) throws SQLException {
        System.out.println("\n---------- Test AdresDAO -------------");

        ReizigerDAO rdao = new ReizigerDAOPsql(connection);

        // Maak een nieuw adres aan en persisteer deze in de database
        Adres adres1 = new Adres(7, "3455XD", "10", "de Landlaan","Utrecht", 77);
        System.out.println(adao.save(adres1));
        adres1.setPostcode("2342DX");
        System.out.println(adao.update(adres1));
        System.out.println(adao.findById(7));
        System.out.println(adao.findByReiziger(rdao.findById(1)));
        System.out.println(adao.delete(adres1));
    }
}
