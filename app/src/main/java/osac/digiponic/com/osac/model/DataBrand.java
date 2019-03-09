package osac.digiponic.com.osac.model;

public class DataBrand {

    private String id, kode_tipe, name, image;

    public DataBrand(String id, String kode_tipe, String name, String image) {
        this.id = id;
        this.kode_tipe = kode_tipe;
        this.name = name;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKode_tipe() {
        return kode_tipe;
    }

    public void setKode_tipe(String kode_tipe) {
        this.kode_tipe = kode_tipe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
