import java.util.Date;

public class OVChipkaart {
    private int id;
    private Date geldig_tot;
    private int klasse;
    private float saldo;
    private Reiziger reiziger;

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

    public void setReiziger(Reiziger reiziger){ this.reiziger = reiziger; }
    public void setKlasse(int klasse) { this.klasse = klasse; }

    public String toString() {
        return String.format("OVChipkaart { #%s geldig tot: %s, klasse: %s, saldo: %s, %s } ", id, geldig_tot, klasse, saldo, reiziger);
    }
}