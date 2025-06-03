package entities;

import java.util.Date;

public class Thuoc {
    private String idThuoc;
    private String tenThuoc;
    private byte[] hinhAnh;
    private String thanhPhan;
    private String idDVT;
    private String idDM;
    private String idXX;
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

    public String getIdDVT() {
        return idDVT;
    }

    public void setIdDVT(String idDVT) {
        this.idDVT = idDVT;
    }

    public String getIdDM() {
        return idDM;
    }

    public void setIdDM(String idDM) {
        this.idDM = idDM;
    }

    public String getIdXX() {
        return idXX;
    }

    public void setIdXX(String idXX) {
        this.idXX = idXX;
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
