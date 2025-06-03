package entities;

/**
 * VaiTro.java
 * Lớp entity ánh xạ bảng VaiTro trong CSDL.
 */
public class VaiTro {
    private String idVT;  // khóa chính
    private String ten;   // tên vai trò (ví dụ: "Admin", "Nhân viên", ...)

    public VaiTro() {
    }

    public VaiTro(String idVT, String ten) {
        this.idVT = idVT;
        this.ten = ten;
    }

    public String getIdVT() {
        return idVT;
    }

    public void setIdVT(String idVT) {
        this.idVT = idVT;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}
