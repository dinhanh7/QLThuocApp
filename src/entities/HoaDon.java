package entities;

import java.util.Date;

/**
 * HoaDon.java
 *
 * Bảng dbo.HoaDon đã bổ sung thêm hai cột:
 *   - phuongThucThanhToan  NVARCHAR(50)   NULL
 *   - trangThaiDonHang     NVARCHAR(50)   NOT NULL
 *
 * Cấu trúc bảng hiện tại:
 *   idHD      NVARCHAR(10)   PK  NOT NULL
 *   thoiGian  DATETIME       NOT NULL
 *   idNV      NVARCHAR(10)   NOT NULL   (FK -> NhanVien)
 *   idKH      NVARCHAR(10)   NOT NULL   (FK -> KhachHang)
 *   tongTien  FLOAT          NOT NULL
 *   phuongThucThanhToan NVARCHAR(50)  NULL
 *   trangThaiDonHang    NVARCHAR(50)  NOT NULL
 */
public class HoaDon {
    private String idHD;
    private Date thoiGian;
    private String idNV;
    private String idKH;
    private double tongTien;
    private String phuongThucThanhToan; // mới, có thể NULL
    private String trangThaiDonHang;    // mới, NOT NULL

    public HoaDon() {
    }

    public String getIdHD() {
        return idHD;
    }

    public void setIdHD(String idHD) {
        this.idHD = idHD;
    }

    public Date getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(Date thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getIdNV() {
        return idNV;
    }

    public void setIdNV(String idNV) {
        this.idNV = idNV;
    }

    public String getIdKH() {
        return idKH;
    }

    public void setIdKH(String idKH) {
        this.idKH = idKH;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public String getTrangThaiDonHang() {
        return trangThaiDonHang;
    }

    public void setTrangThaiDonHang(String trangThaiDonHang) {
        this.trangThaiDonHang = trangThaiDonHang;
    }
}
