//pgAdmin

import java.sql.*;
import java.util.List;

public class Driver {
    private static Connection connection;

    public static void main(String[] args) {

        try{
            // Get a connection to database
            connection = DriverManager.getConnection("jdbc:postgresql://localhost/ovchip", "postgres", "PostGres");
            testReizigerDAO(new ReizigerDAOPsql(connection));
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

        AdresDAO adao = new AdresDAOPsql(connection);
        rdao.setAdao(adao);

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        printReizigerLoop(reizigers);

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske, null);
        System.out.println(rdao.findAll().size() + " reizigers");
        System.out.println();

        // Vind de nieuwe reiziger in de database en geef deze terug
        System.out.println(String.format("[Test] Systeem vind reiziger de volgende reiziger bij ReizigerDAO.findById(77):\n %s", rdao.findById(77).toString()));
        System.out.println();

        // Update de nieuwe reiziger in de database en geef deze terug
        sietske.setTussenvoegsel("de");
        sietske.setAchternaam("Boer");
        System.out.println("[Test] ReizigerDAO.update() geeft de volgende resultaten:\nVoor de update:");
        System.out.println(sietske);
        rdao.update(sietske);
        System.out.println("Na de update:");
        System.out.println(sietske);
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
    private static void testAdresDAO(AdresDAO adao) {
        System.out.println("\n---------- Test AdresDAO -------------");

        ReizigerDAO rdao = new ReizigerDAOPsql(connection);
        Reiziger newReiziger = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf("1981-03-14"));
        Adres newAdres = new Adres(7, "3455XD", "10", "de Landlaan","Utrecht", 77);
        rdao.save(newReiziger, newAdres);

        // Haal alle reizigers op uit de database
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende reizigers:");
        for(Adres a : adao.findAll()){
            System.out.println(a);
        }
        System.out.println();

        // Maak een nieuw adres aan en persisteer deze in de database
        System.out.print(String.format("[Test] Eerst %s adressen, na AdresDAO.save()", adao.findAll().size()));
        adao.save(newAdres);
        System.out.println(String.format(" %s adressen", adao.findAll().size()));
        System.out.println();

        System.out.println("[Test] Systeem vind het volgende adres bij AdresDAO.findById(7):");
        System.out.println(adao.findById(7));
        System.out.println();

        System.out.println(String.format("[Test] AdresDAO.update() geeft de volgende resultaten:\nVoor de update: %s", adao.findById(7)));
        newAdres.setPostcode("2342DX");
        adao.update(newAdres);
        System.out.println(String.format("Na de update: %s", adao.findById(7)));
        System.out.println();

        System.out.println("[Test] AdresDAO.findByReiziger() geeft het volgende adres:");
        System.out.println(adao.findByReiziger(rdao.findById(1)));
        System.out.println();

        System.out.print(String.format("[Test] Eerst %s adressen, na AdresDAO.save()", adao.findAll().size()));
        adao.delete(newAdres);
        System.out.println(String.format(" %s adressen", adao.findAll().size()));

        rdao.delete(rdao.findById(77));
    }
}
