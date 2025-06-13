package entities;

import java.util.Date;

/**
 * PhanHoi.java
 *
 * Mô tả cấu trúc bảng PhanHoi:
 *   idPH     (String)  – khóa chính
 *   idKH     (String)  – khóa ngoại tới KhachHang.idKH
 *   idHD     (String)  – khóa ngoại tới HoaDon.idHD
 *   noiDung  (String)
 *   thoiGian (Date)
 *   danhGia  (int)
 */
public class PhanHoi {
    private String idPH;
    private String idKH;
    private String idHD;
    private String noiDung;
    private Date thoiGian;
    private int danhGia;
    public PhanHoi() {
        // constructor rỗng để phục vụ tạo đối tượng tạm thời khi load dữ liệu
    }

    public PhanHoi(String idPH, String idKH, String idHD, String noiDung, Date thoiGian, int danhGia) {
        this.idPH = idPH;
        this.idKH = idKH;
        this.idHD = idHD;
        this.noiDung = noiDung;
        this.thoiGian = thoiGian;
        this.danhGia = danhGia;
    }

    public String getIdPH() {
        return idPH;
    }

    public void setIdPH(String idPH) {
        this.idPH = idPH;
    }

    public String getIdKH() {
        return idKH;
    }

    public void setIdKH(String idKH) {
        this.idKH = idKH;
    }

    public String getIdHD() {
        return idHD;
    }

    public void setIdHD(String idHD) {
        this.idHD = idHD;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Date getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(Date thoiGian) {
        this.thoiGian = thoiGian;
    }

    public int getDanhGia() {
        return danhGia;
    }

    public void setDanhGia(int danhGia) {
        this.danhGia = danhGia;
    }
    private Boolean isDeleted;
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
}
