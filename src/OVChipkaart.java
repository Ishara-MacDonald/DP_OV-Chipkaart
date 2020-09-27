import java.util.ArrayList;
import java.util.Date;

public class OVChipkaart {
    private int id;
    private int klasse;
    private float saldo;

    private Date geldig_tot;
    private Reiziger reiziger;

    private ArrayList<Product> producten = new ArrayList<>();

    public OVChipkaart(int kaartnr, Date geldig_tot, int klasse, float saldo, Reiziger reiziger){
        this.id = kaartnr;
        this.geldig_tot = geldig_tot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
    }

    public int getId() { return id; }
    public Date getGeldig_tot() { return geldig_tot; }
    public int getKlasse() { return klasse; }
    public float getSaldo() { return saldo; }
    public Reiziger getReiziger() { return reiziger; }
    public ArrayList<Product> getProducten() { return producten; }

    public void setId(int id) { this.id = id; }
    public void setGeldig_tot(Date geldig_tot) { this.geldig_tot = geldig_tot; }
    public void setKlasse(int klasse) { this.klasse = klasse; }
    public void setSaldo(float saldo) { this.saldo = saldo; }
    public void setReiziger(Reiziger reiziger){ this.reiziger = reiziger; }
    public void setProducten(ArrayList<Product> producten) { this.producten = producten; }

    public boolean addProduct(Product product){ return producten.add(product); }
    public void deleteProduct(Product newProduct){
        producten.removeIf(product -> product.getId() == newProduct.getId());
    }

    public String toString() {
        return String.format("OVChipkaart { #%s geldig tot: %s, klasse: %s, saldo: %s, %s } ", id, geldig_tot, klasse, saldo, reiziger);
    }
}