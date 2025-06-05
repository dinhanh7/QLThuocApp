package entities;

import java.util.Date;

/**
 * Thuoc.java
 *
 *  Bảng dbo.Thuoc mới:
 *   - idThuoc      NVARCHAR(10)    NOT NULL
 *   - tenThuoc     NVARCHAR(255)   NOT NULL
 *   - hinhAnh      VARBINARY(MAX)  NULL
 *   - thanhPhan    NVARCHAR(255)   NULL
 *   - donViTinh    NVARCHAR(255)   NOT NULL
 *   - danhMuc      NVARCHAR(255)   NOT NULL
 *   - xuatXu       NCHAR(10)       NOT NULL
 *   - soLuongTon   INT             NOT NULL
 *   - giaNhap      FLOAT           NOT NULL
 *   - donGia       FLOAT           NOT NULL
 *   - hanSuDung    DATE            NOT NULL
 */
public class Thuoc {
    private String idThuoc;
    private String tenThuoc;
    private byte[] hinhAnh;
    private String thanhPhan;
    private String donViTinh;  // mới
    private String danhMuc;    // mới
    private String xuatXu;     // mới
    private int soLuongTon;
    private double giaNhap;
    private double donGia;
    private Date hanSuDung;

    public Thuoc() {
    }

    public String getIdThuoc() {
        return idThuoc;
    }

    public void setIdThuoc(String idThuoc) {
        this.idThuoc = idThuoc;
    }

    public String getTenThuoc() {
        return tenThuoc;
    }

    public void setTenThuoc(String tenThuoc) {
        this.tenThuoc = tenThuoc;
    }

    public byte[] getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getThanhPhan() {
        return thanhPhan;
    }

    public void setThanhPhan(String thanhPhan) {
        this.thanhPhan = thanhPhan;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public String getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(String danhMuc) {
        this.danhMuc = danhMuc;
    }

    public String getXuatXu() {
        return xuatXu;
    }

    public void setXuatXu(String xuatXu) {
        this.xuatXu = xuatXu;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public Date getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(Date hanSuDung) {
        this.hanSuDung = hanSuDung;
    }
}
