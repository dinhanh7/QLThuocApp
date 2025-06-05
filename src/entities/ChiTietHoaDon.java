package entities;

/**
 * ChiTietHoaDon.java
 *
 * Entity cho bảng dbo.ChiTietHoaDon đã bổ sung thêm:
 *   - tenThuoc  NVARCHAR(255)  <-- Tên thuốc, lấy từ bảng Thuoc
 *
 * Cấu trúc bảng gốc:
 *   idHD    NVARCHAR(10)  NOT NULL   (khóa ngoại liên kết đến HoaDon)
 *   idThuoc NVARCHAR(10)  NOT NULL   (khóa ngoại liên kết đến Thuoc)
 *   soLuong INT          NOT NULL
 *   donGia  FLOAT        NOT NULL
 *
 * Chúng ta sẽ join với Thuoc để lấy thêm tenThuoc.
 */
public class ChiTietHoaDon {
    private String idHD;
    private String idThuoc;
    private String tenThuoc;  // Tên thuốc (mới)
    private int soLuong;
    private double donGia;

    public ChiTietHoaDon() {
    }

    public String getIdHD() {
        return idHD;
    }
    public void setIdHD(String idHD) {
        this.idHD = idHD;
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
