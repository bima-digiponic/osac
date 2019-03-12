package osac.digiponic.com.osac.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataItemMenu {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("kategori")
    @Expose
    private String kategori;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("jenis_kendaraan")
    @Expose
    private String jenis_kendaraan;
    @SerializedName("gambar")
    @Expose
    private String gambar;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;
    @SerializedName("kategori_keterangan")
    @Expose
    private String kategori_keterangan;
    @SerializedName("jenis_kendaraan_keterangan")
    @Expose
    private String jenis_kendaraan_keterangan;
    private boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public DataItemMenu() {
    }

    public DataItemMenu(String id, String kategori, String name, String price, String jenis_kendaraan, String gambar, String keterangan, String deskripsi, String kategori_keterangan, String jenis_kendaraan_keterangan) {
        this.id = id;
        this.kategori = kategori;
        this.name = name;
        this.price = price;
        this.jenis_kendaraan = jenis_kendaraan;
        this.gambar = gambar;
        this.keterangan = keterangan;
        this.deskripsi = deskripsi;
        this.kategori_keterangan = kategori_keterangan;
        this.jenis_kendaraan_keterangan = jenis_kendaraan_keterangan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getJenis_kendaraan() {
        return jenis_kendaraan;
    }

    public void setJenis_kendaraan(String jenis_kendaraan) {
        this.jenis_kendaraan = jenis_kendaraan;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getKategori_keterangan() {
        return kategori_keterangan;
    }

    public void setKategori_keterangan(String kategori_keterangan) {
        this.kategori_keterangan = kategori_keterangan;
    }

    public String getJenis_kendaraan_keterangan() {
        return jenis_kendaraan_keterangan;
    }

    public void setJenis_kendaraan_keterangan(String jenis_kendaraan_keterangan) {
        this.jenis_kendaraan_keterangan = jenis_kendaraan_keterangan;
    }
}
