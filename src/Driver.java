//pgAdmin

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Driver {
    private static Connection connection;

    public static void main(String[] args) {

        try{
            // Get a connection to database
            connection = DriverManager.getConnection("jdbc:postgresql://localhost/ovchip", "postgres", "PostGres");
//            testReizigerDAO(new ReizigerDAOPsql(connection));
//            testAdresDAO(new AdresDAOPsql(connection));
//            testOVChipkaartDAO(new OVChipkaartDAOPsql(connection));
            testProductDAO(new ProductDOAPsql(connection));
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
        rdao.save(newReiziger);

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

    /**
     * P4. OVChipkaart DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de OVChipkaartDAO
     *
     * @throws SQLException
     */
    private static void testOVChipkaartDAO(OVChipkaartDAO ovdao){
        System.out.println("\n---------- Test OVChipkaartDAO -------------");

        ReizigerDAO rdao = new ReizigerDAOPsql(connection);
        Reiziger newReiziger = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf("1981-03-14"));
        OVChipkaart newOVChipkaart1 = new OVChipkaart(23, java.sql.Date.valueOf("2024-09-13"), 1, (float) 50.0, newReiziger);
        OVChipkaart newOVChipkaart2 = new OVChipkaart(26, java.sql.Date.valueOf("2024-09-13"), 2, (float) 25.0, newReiziger);
//        Adres newAdres = new Adres(7, "3455XD", "10", "de Landlaan","Utrecht", 77);
        rdao.save(newReiziger);

        ovdao.setRdao(rdao);

        // Haal alle reizigers op uit de database
        System.out.println("[Test] OVChipkaartDAO.findAll() geeft de volgende ov-chipkaarten:");
        printOVChipkaartLoop(ovdao.findAll());
        System.out.println();

        System.out.printf("[Test] Eerst %s ov-chipkaarten, 2x na OVChipkaartDAO.save() ", ovdao.findAll().size());
        ovdao.save(newOVChipkaart1);
        ovdao.save(newOVChipkaart2);
        System.out.printf("%s ov-chipkaarten", ovdao.findAll().size());
        System.out.println("\n");

        System.out.println("[Test] Systeem vind het volgende ov-chipkaart bij OVChipkaartDAO.findById(23):");
        System.out.println(ovdao.findById(23));
        System.out.println();

        System.out.printf("[Test] OVChipkaartDAO.update() geeft de volgende resultaten:\nVoor de update: %s%n", ovdao.findById(23));
        newOVChipkaart1.setKlasse(2);
        ovdao.update(newOVChipkaart1);
        System.out.printf("Na de update: %s\n", ovdao.findById(23));
        System.out.println();

        System.out.println("[Test] OVChipkaartDAO.findByReiziger() geeft het volgende ov-chipkaart:");
        printOVChipkaartLoop(ovdao.findByReiziger(newReiziger));
        System.out.println();

        System.out.println("[Test] OVChipkaartDAO.findByReiziger() na OVChipkaartDAO.delete()");
        ovdao.delete(newOVChipkaart1);
        printOVChipkaartLoop(ovdao.findByReiziger(newReiziger));
        System.out.println();

        System.out.printf("[Test] Eerst %s ov-chipkaarten, na OVChipkaartDAO.delete()", ovdao.findAll().size());
        ovdao.delete(newOVChipkaart2);
        System.out.printf(" %s ov-chipkaarten", ovdao.findAll().size());
        System.out.println();

        rdao.delete(rdao.findById(77));
    }

    public static void printOVChipkaartLoop(List<OVChipkaart> kaarten){
        for(OVChipkaart ovChipkaart : kaarten){
            System.out.println(ovChipkaart);
        }
    }

    /**
     * P5. Product DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de ProductDAO
     *
     * @throws SQLException
     */
    private static void testProductDAO(ProductDAO pdao){
        //Setup Reiziger, OVChipkaart en Product.
        ReizigerDAO rdao = new ReizigerDAOPsql(connection);
        OVChipkaartDAO ovdao = new OVChipkaartDAOPsql(connection);
        AdresDAO adao = new AdresDAOPsql(connection);
        pdao.setOVdao(ovdao);
        pdao.setRdao(rdao);
        ovdao.setRdao(rdao);
        rdao.setOVdao(ovdao);
        rdao.setAdao(adao);

        Reiziger newReiziger = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf("1981-03-14"));

        OVChipkaart newOVChipkaart1 = new OVChipkaart(33, java.sql.Date.valueOf("2024-09-13"), 1, (float) 50.0, newReiziger);
        OVChipkaart newOVChipkaart2 = new OVChipkaart(30, java.sql.Date.valueOf("2024-09-13"), 2, (float) 25.0, newReiziger);
        rdao.save(newReiziger);
        newReiziger.addOVKaart(newOVChipkaart1);
        newReiziger.addOVKaart(newOVChipkaart2);

        Product productOne = new Product(25, "StudentenOV", "OV Product voor Studenten", 25.00f);

        productOne.addKaart(newOVChipkaart1);
        productOne.addKaart(newOVChipkaart2);

        OVChipkaart testKaart = ovdao.findById(35283);

        System.out.println("\n---------- Test ProductDAO -------------");
        System.out.println();

        System.out.println("[Test] ProductDAO.findAll() geeft de volgende ov-chipkaarten:");
        printProductLoop(pdao.findAll());
        System.out.println();

        System.out.println("[Test] ProductDAO.save() geeft het volgende resultaat:");
        System.out.println(pdao.save(productOne));
        System.out.println();

        System.out.printf("[Test] ProductDAO.update() geeft de volgende resultaten:\nVoor de update: %s%n", pdao.findById(25));
        productOne.setNaam("Studenten Product");
        pdao.update(productOne);
        System.out.printf("Na de update: %s\n", pdao.findById(25));
        System.out.println();

        System.out.println("[Test] Systeem vind de volgende ov-chipkaarten bij ProductDAO.findByOVChipkaart(33)");
        printProductLoop(pdao.findByOVChipkaart(testKaart));
        System.out.println();

        System.out.println("[Test] Systeem vind de volgende ov-chipkaarten bij ProductDAO.findByOVChipkaart(33)");
        System.out.println(testKaart);
        System.out.println();

        System.out.println("[Test] ProductDAO.delete() geeft het volgende resultaat:");
        System.out.println(pdao.delete(productOne));

        rdao.delete(newReiziger);


        /*System.out.printf("[Test] Eerst %s ov-chipkaarten, 2x na OVChipkaartDAO.save() ", pdao.findAll().size());
        System.out.printf("%s ov-chipkaarten", pdao.findAll().size());
        System.out.println("\n");

        System.out.printf("[Test] OVChipkaartDAO.update() geeft de volgende resultaten:\nVoor de update: %s%n", pdao.findById(23));
//        pdao.update(newOVChipkaart1);
        System.out.printf("Na de update: %s\n", pdao.findById(23));
        System.out.println();

        System.out.println("[Test] OVChipkaartDAO.findByReiziger() geeft het volgende ov-chipkaart:");
//        printOVChipkaartLoop(pdao.findByReiziger(newReiziger));
        System.out.println();

        System.out.println("[Test] OVChipkaartDAO.findByReiziger() na OVChipkaartDAO.delete()");
//        pdao.delete(newOVChipkaart1);
//        printOVChipkaartLoop(pdao.findByReiziger(newReiziger));
        System.out.println();

        System.out.printf("[Test] Eerst %s ov-chipkaarten, na OVChipkaartDAO.delete()", pdao.findAll().size());
//        pdao.delete(newOVChipkaart2);
        System.out.printf(" %s ov-chipkaarten", pdao.findAll().size());
        System.out.println();

//        rdao.delete(rdao.findById(77));*/
    }

    public static void printProductLoop(List<Product> producten){
        for(Product product : producten){
            System.out.println(product);
        }
    }
}
