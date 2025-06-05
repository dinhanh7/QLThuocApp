package entities;

/**
 * ChiTietPhieuNhap.java
 *
 * Entity đã được bổ sung thêm trường tenThuoc để hiển thị tên thuốc:
 *   idPN      NVARCHAR(10)
 *   idThuoc   NVARCHAR(10)
 *   tenThuoc  NVARCHAR(255)   <-- mới
 *   soLuong   INT
 *   donGia    DOUBLE
 */
public class ChiTietPhieuNhap {
    private String idPN;
    private String idThuoc;
    private String tenThuoc; // Tên thuốc (gọi từ bảng Thuoc)
    private int soLuong;
    private double donGia;

    public ChiTietPhieuNhap() {
    }

    public String getIdPN() {
        return idPN;
    }
    public void setIdPN(String idPN) {
        this.idPN = idPN;
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

    public int getSoLuong() {
        return soLuong;
    }
    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }
    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }
}
