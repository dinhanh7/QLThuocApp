package entities;

import java.util.Date;

public class KhachHang {
    private String idKH;
    private String hoTen;
    private String sdt;
    private String gioiTinh;
    private Date ngayThamGia;
    private int diemTichLuy; // điểm tích lũy

    public KhachHang() {
    }
    public KhachHang(String idKH, String hoTen, String sdt, String gioiTinh, Date ngayThamGia) {
        this.idKH = idKH;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.gioiTinh = gioiTinh;
        this.ngayThamGia = ngayThamGia;
    }
    public String getIdKH() {
        return idKH;
    }

    public void setIdKH(String idKH) {
        this.idKH = idKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public Date getNgayThamGia() {
        return ngayThamGia;
    }

    public void setNgayThamGia(Date ngayThamGia) {
        this.ngayThamGia = ngayThamGia;
    }
    // thêm phần này 62-64
    private Boolean isDeleted;
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
    
    public int getDiemTichLuy() { return diemTichLuy; }
    public void setDiemTichLuy(int diemTichLuy) { this.diemTichLuy = diemTichLuy; }
}
