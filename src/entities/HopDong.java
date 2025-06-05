package entities;

import java.util.Date;

/**
 * HopDong.java
 *
 * Bảng dbo.HopDong đã bổ sung thêm cột:
 *   - trangThai  NVARCHAR(50)   NOT NULL
 *
 * Cấu trúc bảng hiện tại:
 *   idHDong    NVARCHAR(10)   PK   NOT NULL
 *   ngayBatDau DATE           NOT NULL
 *   ngayKetThuc DATE           NOT NULL
 *   noiDung    NVARCHAR(MAX)  NULL
 *   idNV       NVARCHAR(10)   NULL   (FK to NhanVien)
 *   idNCC      NVARCHAR(10)   NULL   (FK to NhaCungCap)
 *   trangThai  NVARCHAR(50)   NOT NULL
 */
public class HopDong {
    private String idHDong;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String noiDung;
    private String idNV;    // có thể NULL nếu là HĐ nhà cung cấp
    private String idNCC;   // có thể NULL nếu là HĐ nhân viên
    private String trangThai;

    public HopDong() {
    }

    public String getIdHDong() {
        return idHDong;
    }

    public void setIdHDong(String idHDong) {
        this.idHDong = idHDong;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getIdNV() {
        return idNV;
    }

    public void setIdNV(String idNV) {
        this.idNV = idNV;
    }

    public String getIdNCC() {
        return idNCC;
    }

    public void setIdNCC(String idNCC) {
        this.idNCC = idNCC;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
