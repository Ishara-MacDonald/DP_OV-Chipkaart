import java.util.Date;

public class OVChipkaart {
    private int kaart_nummer;
    private Date geldig_tot;
    private int klasse;
    private float saldo;
    private int reiziger_id;

    public OVChipkaart(int kaartnr, Date geldig_tot, int klasse, float saldo, int reiziger_id){
        this.kaart_nummer = kaartnr;
        this.geldig_tot = geldig_tot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger_id = reiziger_id;
    }



}