package entities;

import java.util.Date;

/**
 * NhanVien.java
 *
 * Bảng dbo.NhanVien đã bổ sung thêm hai cột:
 *   - luong      NVARCHAR(50)   NOT NULL
 *   - trangThai  NVARCHAR(50)   NOT NULL
 *
 * Đồng thời vẫn giữ các cột cũ:
 *   idNV, hoTen, sdt, gioiTinh, namSinh, ngayVaoLam
 *   (và phần thêm trước đó: username, password, roleId)
 */
public class NhanVien {
    private String idNV;
    private String hoTen;
    private String sdt;
    private String gioiTinh;
    private int namSinh;
    private Date ngayVaoLam;

    // Các trường mới:
    private String luong;
    private String trangThai;

    // Thông tin tài khoản (đã có từ trước)
    private String username;
    private String password;
    private String roleId; // idVT

    public NhanVien() {
    }

    public String getIdNV() {
        return idNV;
    }

    public void setIdNV(String idNV) {
        this.idNV = idNV;
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

    public int getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(int namSinh) {
        this.namSinh = namSinh;
    }

    public Date getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(Date ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public String getLuong() {
        return luong;
    }

    public void setLuong(String luong) {
        this.luong = luong;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    private Boolean isDeleted;
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
}
