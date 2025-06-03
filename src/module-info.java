module QLThuocApp {
    requires java.sql;            // Cho phép module này dùng JDBC
    requires java.desktop;        // Cho phép dùng Swing, ImageIcon, JFrame, etc.
    
    // Nếu bạn viết thư viện muốn truyền tiếp java.sql cho các module khác
    // thì mới cần dòng này, còn nếu chỉ dùng nội bộ thì không cần
    // requires transitive java.sql;

    exports connectDB;
    exports entities;
    exports dao;
    exports controller;
    exports gui;
    exports utils;
}
