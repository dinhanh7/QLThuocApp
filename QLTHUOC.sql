USE [master]
GO
/****** Object:  Database [QLTHUOC]    Script Date: 6/5/2025 11:03:31 PM ******/
CREATE DATABASE [QLTHUOC]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'QLTHUOC', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\QLTHUOC.mdf' , SIZE = 73728KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'QLTHUOC_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\QLTHUOC_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [QLTHUOC] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [QLTHUOC].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [QLTHUOC] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [QLTHUOC] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [QLTHUOC] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [QLTHUOC] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [QLTHUOC] SET ARITHABORT OFF 
GO
ALTER DATABASE [QLTHUOC] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [QLTHUOC] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [QLTHUOC] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [QLTHUOC] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [QLTHUOC] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [QLTHUOC] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [QLTHUOC] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [QLTHUOC] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [QLTHUOC] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [QLTHUOC] SET  ENABLE_BROKER 
GO
ALTER DATABASE [QLTHUOC] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [QLTHUOC] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [QLTHUOC] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [QLTHUOC] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [QLTHUOC] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [QLTHUOC] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [QLTHUOC] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [QLTHUOC] SET RECOVERY FULL 
GO
ALTER DATABASE [QLTHUOC] SET  MULTI_USER 
GO
ALTER DATABASE [QLTHUOC] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [QLTHUOC] SET DB_CHAINING OFF 
GO
ALTER DATABASE [QLTHUOC] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [QLTHUOC] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [QLTHUOC] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [QLTHUOC] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'QLTHUOC', N'ON'
GO
ALTER DATABASE [QLTHUOC] SET QUERY_STORE = ON
GO
ALTER DATABASE [QLTHUOC] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [QLTHUOC]
GO
/****** Object:  Table [dbo].[ChiTietHoaDon]    Script Date: 6/5/2025 11:03:32 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ChiTietHoaDon](
	[idHD] [nvarchar](10) NOT NULL,
	[idThuoc] [nvarchar](10) NOT NULL,
	[soLuong] [int] NOT NULL,
	[donGia] [float] NOT NULL,
 CONSTRAINT [idCTHD] PRIMARY KEY CLUSTERED 
(
	[idHD] ASC,
	[idThuoc] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ChiTietPhieuNhap]    Script Date: 6/5/2025 11:03:32 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ChiTietPhieuNhap](
	[idPN] [nvarchar](10) NOT NULL,
	[idThuoc] [nvarchar](10) NOT NULL,
	[soLuong] [int] NOT NULL,
	[donGia] [float] NOT NULL,
 CONSTRAINT [idCTPN] PRIMARY KEY CLUSTERED 
(
	[idPN] ASC,
	[idThuoc] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[HoaDon]    Script Date: 6/5/2025 11:03:32 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HoaDon](
	[idHD] [nvarchar](10) NOT NULL,
	[thoiGian] [datetime] NOT NULL,
	[idNV] [nvarchar](10) NOT NULL,
	[idKH] [nvarchar](10) NOT NULL,
	[tongTien] [float] NOT NULL,
	[phuongThucThanhToan] [nvarchar](50) NULL,
	[trangThaiDonHang] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK__HoaDon__9DB889C641FA0265] PRIMARY KEY CLUSTERED 
(
	[idHD] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[HopDong]    Script Date: 6/5/2025 11:03:32 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HopDong](
	[idHDong] [nvarchar](10) NOT NULL,
	[ngayBatDau] [date] NOT NULL,
	[ngayKetThuc] [date] NOT NULL,
	[noiDung] [nvarchar](max) NULL,
	[idNV] [nvarchar](10) NULL,
	[idNCC] [nvarchar](10) NULL,
	[trangThai] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK__HopDong__CFC9C71099C2876E] PRIMARY KEY CLUSTERED 
(
	[idHDong] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[KhachHang]    Script Date: 6/5/2025 11:03:32 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[KhachHang](
	[idKH] [nvarchar](10) NOT NULL,
	[hoTen] [nvarchar](255) NOT NULL,
	[sdt] [nvarchar](10) NOT NULL,
	[gioiTinh] [nvarchar](10) NOT NULL,
	[ngayThamGia] [date] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[idKH] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[NhaCungCap]    Script Date: 6/5/2025 11:03:32 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NhaCungCap](
	[idNCC] [nvarchar](10) NOT NULL,
	[tenNCC] [nvarchar](255) NOT NULL,
	[sdt] [nvarchar](10) NOT NULL,
	[diaChi] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[idNCC] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[NhanVien]    Script Date: 6/5/2025 11:03:32 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NhanVien](
	[idNV] [nvarchar](10) NOT NULL,
	[hoTen] [nvarchar](255) NOT NULL,
	[sdt] [nvarchar](10) NOT NULL,
	[gioiTinh] [nvarchar](10) NOT NULL,
	[namSinh] [int] NOT NULL,
	[ngayVaoLam] [date] NOT NULL,
	[luong] [nvarchar](50) NOT NULL,
	[trangThai] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK__NhanVien__9DB8791C1EC231B9] PRIMARY KEY CLUSTERED 
(
	[idNV] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PhanHoi]    Script Date: 6/5/2025 11:03:32 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PhanHoi](
	[idPH] [nvarchar](10) NOT NULL,
	[idKH] [nvarchar](10) NOT NULL,
	[idHD] [nvarchar](10) NOT NULL,
	[idThuoc] [nvarchar](10) NOT NULL,
	[noiDung] [nvarchar](max) NOT NULL,
	[thoiGian] [datetime] NOT NULL,
	[danhGia] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[idPH] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PhieuNhap]    Script Date: 6/5/2025 11:03:32 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PhieuNhap](
	[idPN] [nvarchar](10) NOT NULL,
	[thoiGian] [datetime] NOT NULL,
	[idNV] [nvarchar](10) NOT NULL,
	[idNCC] [nvarchar](10) NOT NULL,
	[tongTien] [float] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[idPN] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[TaiKhoan]    Script Date: 6/5/2025 11:03:32 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TaiKhoan](
	[idTK] [nvarchar](10) NOT NULL,
	[username] [nvarchar](255) NOT NULL,
	[password] [nvarchar](255) NOT NULL,
	[idNV] [nvarchar](10) NOT NULL,
	[idVT] [nvarchar](10) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[idTK] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Thuoc]    Script Date: 6/5/2025 11:03:32 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Thuoc](
	[idThuoc] [nvarchar](10) NOT NULL,
	[tenThuoc] [nvarchar](255) NOT NULL,
	[hinhAnh] [varbinary](max) NULL,
	[thanhPhan] [nvarchar](255) NULL,
	[donViTinh] [nvarchar](255) NOT NULL,
	[danhMuc] [nvarchar](255) NOT NULL,
	[xuatXu] [nvarchar](255) NOT NULL,
	[soLuongTon] [int] NOT NULL,
	[giaNhap] [float] NOT NULL,
	[donGia] [float] NOT NULL,
	[hanSuDung] [date] NOT NULL,
 CONSTRAINT [PK__Thuoc__301D31E6BEB17AAB] PRIMARY KEY CLUSTERED 
(
	[idThuoc] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[VaiTro]    Script Date: 6/5/2025 11:03:32 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[VaiTro](
	[idVT] [nvarchar](10) NOT NULL,
	[ten] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[idVT] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD001', N'T001', 10, 2000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD001', N'T006', 5, 3000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD001', N'T013', 8, 4200)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD002', N'T003', 3, 20000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD002', N'T005', 6, 5500)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD002', N'T007', 4, 12000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD003', N'T001', 15, 2000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD003', N'T002', 8, 4000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD003', N'T004', 5, 3500)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD004', N'T009', 2, 35000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD004', N'T010', 3, 25000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD004', N'T011', 12, 16000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD005', N'T008', 6, 5000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD005', N'T012', 1, 42000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD005', N'T015', 2, 65000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD006', N'T001', 20, 2000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD006', N'T006', 12, 3000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD006', N'T013', 15, 4200)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD007', N'T001', 8, 2000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD007', N'T002', 5, 4000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD007', N'T004', 10, 3500)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD008', N'T003', 5, 20000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD008', N'T014', 3, 50000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD008', N'T015', 2, 65000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD009', N'T005', 4, 5500)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD009', N'T007', 2, 12000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD009', N'T011', 6, 16000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD010', N'T008', 8, 5000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD010', N'T009', 1, 35000)
INSERT [dbo].[ChiTietHoaDon] ([idHD], [idThuoc], [soLuong], [donGia]) VALUES (N'HD010', N'T012', 2, 42000)
GO
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN001', N'T001', 200, 1500)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN001', N'T002', 150, 3000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN001', N'T008', 100, 3500)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN002', N'T003', 100, 15000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN002', N'T004', 150, 2500)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN002', N'T005', 120, 4000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN003', N'T006', 80, 2000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN003', N'T007', 60, 8000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN003', N'T009', 50, 25000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN004', N'T001', 100, 1500)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN004', N'T010', 70, 18000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN004', N'T011', 90, 12000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN005', N'T012', 40, 30000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN005', N'T013', 80, 2800)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN005', N'T014', 60, 35000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN006', N'T002', 80, 3000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN006', N'T005', 70, 4000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN006', N'T015', 50, 45000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN007', N'T003', 80, 15000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN007', N'T007', 40, 8000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN007', N'T011', 60, 12000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN008', N'T004', 100, 2500)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN008', N'T006', 90, 2000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN008', N'T010', 50, 18000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN009', N'T001', 120, 1500)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN009', N'T008', 80, 3500)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN009', N'T013', 70, 2800)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN010', N'T009', 60, 25000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN010', N'T012', 30, 30000)
INSERT [dbo].[ChiTietPhieuNhap] ([idPN], [idThuoc], [soLuong], [donGia]) VALUES (N'PN010', N'T014', 40, 35000)
GO
INSERT [dbo].[HoaDon] ([idHD], [thoiGian], [idNV], [idKH], [tongTien], [phuongThucThanhToan], [trangThaiDonHang]) VALUES (N'HD001', CAST(N'2024-06-01T10:30:00.000' AS DateTime), N'NV002', N'KH001', 125000, N'Tiền mặt', N'Đã thanh toán')
INSERT [dbo].[HoaDon] ([idHD], [thoiGian], [idNV], [idKH], [tongTien], [phuongThucThanhToan], [trangThaiDonHang]) VALUES (N'HD002', CAST(N'2024-06-02T14:15:00.000' AS DateTime), N'NV004', N'KH002', 280000, N'Chuyển khoản', N'Đã thanh toán')
INSERT [dbo].[HoaDon] ([idHD], [thoiGian], [idNV], [idKH], [tongTien], [phuongThucThanhToan], [trangThaiDonHang]) VALUES (N'HD003', CAST(N'2024-06-03T16:45:00.000' AS DateTime), N'NV006', N'KH003', 95000, N'Tiền mặt', N'Chưa thanh toán')
INSERT [dbo].[HoaDon] ([idHD], [thoiGian], [idNV], [idKH], [tongTien], [phuongThucThanhToan], [trangThaiDonHang]) VALUES (N'HD004', CAST(N'2024-06-05T09:20:00.000' AS DateTime), N'NV002', N'KH004', 340000, N'Thẻ tín dụng', N'Đã thanh toán')
INSERT [dbo].[HoaDon] ([idHD], [thoiGian], [idNV], [idKH], [tongTien], [phuongThucThanhToan], [trangThaiDonHang]) VALUES (N'HD005', CAST(N'2024-06-07T11:50:00.000' AS DateTime), N'NV007', N'KH005', 180000, N'Tiền mặt', N'Đã thanh toán')
INSERT [dbo].[HoaDon] ([idHD], [thoiGian], [idNV], [idKH], [tongTien], [phuongThucThanhToan], [trangThaiDonHang]) VALUES (N'HD006', CAST(N'2024-06-10T13:25:00.000' AS DateTime), N'NV008', N'KH006', 220000, N'Chuyển khoản', N'Chưa thanh toán')
INSERT [dbo].[HoaDon] ([idHD], [thoiGian], [idNV], [idKH], [tongTien], [phuongThucThanhToan], [trangThaiDonHang]) VALUES (N'HD007', CAST(N'2024-06-12T15:10:00.000' AS DateTime), N'NV004', N'KH007', 75000, N'Tiền mặt', N'Đã thanh toán')
INSERT [dbo].[HoaDon] ([idHD], [thoiGian], [idNV], [idKH], [tongTien], [phuongThucThanhToan], [trangThaiDonHang]) VALUES (N'HD008', CAST(N'2024-06-15T17:35:00.000' AS DateTime), N'NV002', N'KH008', 460000, N'Thẻ tín dụng', N'Đã thanh toán')
INSERT [dbo].[HoaDon] ([idHD], [thoiGian], [idNV], [idKH], [tongTien], [phuongThucThanhToan], [trangThaiDonHang]) VALUES (N'HD009', CAST(N'2024-06-18T12:05:00.000' AS DateTime), N'NV006', N'KH009', 135000, N'Tiền mặt', N'Chưa thanh toán')
INSERT [dbo].[HoaDon] ([idHD], [thoiGian], [idNV], [idKH], [tongTien], [phuongThucThanhToan], [trangThaiDonHang]) VALUES (N'HD010', CAST(N'2024-06-20T14:55:00.000' AS DateTime), N'NV007', N'KH010', 295000, N'Chuyển khoản', N'Đã thanh toán')
GO
INSERT [dbo].[HopDong] ([idHDong], [ngayBatDau], [ngayKetThuc], [noiDung], [idNV], [idNCC], [trangThai]) VALUES (N'HDong001', CAST(N'2024-01-01' AS Date), CAST(N'2024-12-31' AS Date), N'Hợp đồng cung cấp thuốc cơ bản năm 2024', NULL, N'NCC001', N'Đang diễn ra')
INSERT [dbo].[HopDong] ([idHDong], [ngayBatDau], [ngayKetThuc], [noiDung], [idNV], [idNCC], [trangThai]) VALUES (N'HDong002', CAST(N'2024-02-15' AS Date), CAST(N'2025-02-14' AS Date), N'Hợp đồng cung cấp kháng sinh chuyên dụng', NULL, N'NCC002', N'Đang diễn ra')
INSERT [dbo].[HopDong] ([idHDong], [ngayBatDau], [ngayKetThuc], [noiDung], [idNV], [idNCC], [trangThai]) VALUES (N'HDong003', CAST(N'2023-06-01' AS Date), CAST(N'2024-05-31' AS Date), N'Hợp đồng cung cấp vitamin và thực phẩm chức năng', NULL, N'NCC003', N'Đã kết thúc')
INSERT [dbo].[HopDong] ([idHDong], [ngayBatDau], [ngayKetThuc], [noiDung], [idNV], [idNCC], [trangThai]) VALUES (N'HDong004', CAST(N'2024-07-01' AS Date), CAST(N'2025-06-30' AS Date), N'Hợp đồng cung cấp thuốc tim mạch', NULL, N'NCC004', N'Chưa diễn ra')
INSERT [dbo].[HopDong] ([idHDong], [ngayBatDau], [ngayKetThuc], [noiDung], [idNV], [idNCC], [trangThai]) VALUES (N'HDong005', CAST(N'2024-03-10' AS Date), CAST(N'2024-09-09' AS Date), N'Hợp đồng cung cấp thuốc chống viêm', NULL, N'NCC005', N'Đã kết thúc')
INSERT [dbo].[HopDong] ([idHDong], [ngayBatDau], [ngayKetThuc], [noiDung], [idNV], [idNCC], [trangThai]) VALUES (N'HDong006', CAST(N'2024-04-20' AS Date), CAST(N'2025-04-19' AS Date), N'Hợp đồng cung cấp thuốc dị ứng', NULL, N'NCC006', N'Đang diễn ra')
INSERT [dbo].[HopDong] ([idHDong], [ngayBatDau], [ngayKetThuc], [noiDung], [idNV], [idNCC], [trangThai]) VALUES (N'HDong007', CAST(N'2025-01-01' AS Date), CAST(N'2025-12-31' AS Date), N'Hợp đồng cung cấp thuốc dạ dày năm 2025', NULL, N'NCC007', N'Chưa diễn ra')
INSERT [dbo].[HopDong] ([idHDong], [ngayBatDau], [ngayKetThuc], [noiDung], [idNV], [idNCC], [trangThai]) VALUES (N'HDong008', CAST(N'2024-05-15' AS Date), CAST(N'2025-05-14' AS Date), N'Hợp đồng cung cấp thuốc ngoài da', NULL, N'NCC008', N'Đang diễn ra')
INSERT [dbo].[HopDong] ([idHDong], [ngayBatDau], [ngayKetThuc], [noiDung], [idNV], [idNCC], [trangThai]) VALUES (N'HDong009', CAST(N'2023-12-01' AS Date), CAST(N'2024-11-30' AS Date), N'Hợp đồng làm việc nhân viên Bình', N'NV002', NULL, N'Đã kết thúc')
INSERT [dbo].[HopDong] ([idHDong], [ngayBatDau], [ngayKetThuc], [noiDung], [idNV], [idNCC], [trangThai]) VALUES (N'HDong010', CAST(N'2024-08-01' AS Date), CAST(N'2025-07-31' AS Date), N'Hợp đồng làm việc nhân viên Phương', N'NV006', NULL, N'Đang diễn ra')
GO
INSERT [dbo].[KhachHang] ([idKH], [hoTen], [sdt], [gioiTinh], [ngayThamGia]) VALUES (N'KH001', N'Nguyễn Thị Mai', N'0987654321', N'Nữ', CAST(N'2023-01-15' AS Date))
INSERT [dbo].[KhachHang] ([idKH], [hoTen], [sdt], [gioiTinh], [ngayThamGia]) VALUES (N'KH002', N'Trần Văn Nam', N'0976543210', N'Nam', CAST(N'2023-02-20' AS Date))
INSERT [dbo].[KhachHang] ([idKH], [hoTen], [sdt], [gioiTinh], [ngayThamGia]) VALUES (N'KH003', N'Lê Thị Hương', N'0965432109', N'Nữ', CAST(N'2023-03-10' AS Date))
INSERT [dbo].[KhachHang] ([idKH], [hoTen], [sdt], [gioiTinh], [ngayThamGia]) VALUES (N'KH004', N'Phạm Minh Tuấn', N'0954321098', N'Nam', CAST(N'2023-04-05' AS Date))
INSERT [dbo].[KhachHang] ([idKH], [hoTen], [sdt], [gioiTinh], [ngayThamGia]) VALUES (N'KH005', N'Hoàng Thị Lan', N'0943210987', N'Nữ', CAST(N'2023-05-12' AS Date))
INSERT [dbo].[KhachHang] ([idKH], [hoTen], [sdt], [gioiTinh], [ngayThamGia]) VALUES (N'KH006', N'Vũ Đình Hải', N'0932109876', N'Nam', CAST(N'2023-06-18' AS Date))
INSERT [dbo].[KhachHang] ([idKH], [hoTen], [sdt], [gioiTinh], [ngayThamGia]) VALUES (N'KH007', N'Đỗ Thị Nga', N'0921098765', N'Nữ', CAST(N'2023-07-22' AS Date))
INSERT [dbo].[KhachHang] ([idKH], [hoTen], [sdt], [gioiTinh], [ngayThamGia]) VALUES (N'KH008', N'Ngô Văn Đức', N'0910987654', N'Nam', CAST(N'2023-08-08' AS Date))
INSERT [dbo].[KhachHang] ([idKH], [hoTen], [sdt], [gioiTinh], [ngayThamGia]) VALUES (N'KH009', N'Bùi Thị Trang', N'0909876543', N'Nữ', CAST(N'2023-09-14' AS Date))
INSERT [dbo].[KhachHang] ([idKH], [hoTen], [sdt], [gioiTinh], [ngayThamGia]) VALUES (N'KH010', N'Lý Minh Khang', N'0998765432', N'Nam', CAST(N'2023-10-30' AS Date))
INSERT [dbo].[KhachHang] ([idKH], [hoTen], [sdt], [gioiTinh], [ngayThamGia]) VALUES (N'KH011', N'Phan Thị Linh', N'0988654321', N'Nữ', CAST(N'2023-11-25' AS Date))
INSERT [dbo].[KhachHang] ([idKH], [hoTen], [sdt], [gioiTinh], [ngayThamGia]) VALUES (N'KH012', N'Tô Văn Sơn', N'0977543210', N'Nam', CAST(N'2023-12-10' AS Date))
GO
INSERT [dbo].[NhaCungCap] ([idNCC], [tenNCC], [sdt], [diaChi]) VALUES (N'NCC001', N'Công ty Dược phẩm Hà Nội', N'0241234567', N'123 Đường Láng, Ba Đình, Hà Nội')
INSERT [dbo].[NhaCungCap] ([idNCC], [tenNCC], [sdt], [diaChi]) VALUES (N'NCC002', N'Dược phẩm Sài Gòn', N'0281234568', N'456 Nguyễn Trãi, Quận 5, TP.HCM')
INSERT [dbo].[NhaCungCap] ([idNCC], [tenNCC], [sdt], [diaChi]) VALUES (N'NCC003', N'Công ty Traphaco', N'0241234569', N'789 Giải Phóng, Hai Bà Trưng, Hà Nội')
INSERT [dbo].[NhaCungCap] ([idNCC], [tenNCC], [sdt], [diaChi]) VALUES (N'NCC004', N'Dược Hậu Giang', N'0291234570', N'321 Trần Hưng Đạo, Cần Thơ')
INSERT [dbo].[NhaCungCap] ([idNCC], [tenNCC], [sdt], [diaChi]) VALUES (N'NCC005', N'Imexpharm', N'0281234571', N'654 Điện Biên Phủ, Quận 3, TP.HCM')
INSERT [dbo].[NhaCungCap] ([idNCC], [tenNCC], [sdt], [diaChi]) VALUES (N'NCC006', N'Công ty Domesco', N'0251234572', N'987 Lê Lợi, Đồng Nai')
INSERT [dbo].[NhaCungCap] ([idNCC], [tenNCC], [sdt], [diaChi]) VALUES (N'NCC007', N'Dược phẩm Trung ương 1', N'0241234573', N'147 Thái Hà, Đống Đa, Hà Nội')
INSERT [dbo].[NhaCungCap] ([idNCC], [tenNCC], [sdt], [diaChi]) VALUES (N'NCC008', N'Mediplantex', N'0241234574', N'258 Cầu Giấy, Cầu Giấy, Hà Nội')
INSERT [dbo].[NhaCungCap] ([idNCC], [tenNCC], [sdt], [diaChi]) VALUES (N'NCC009', N'Dược phẩm An Thiên', N'0261234575', N'369 Lê Duẩn, Huế')
INSERT [dbo].[NhaCungCap] ([idNCC], [tenNCC], [sdt], [diaChi]) VALUES (N'NCC010', N'Công ty Pymepharco', N'0281234576', N'741 Cộng Hòa, Tân Bình, TP.HCM')
INSERT [dbo].[NhaCungCap] ([idNCC], [tenNCC], [sdt], [diaChi]) VALUES (N'NCC011', N'Dược phẩm Hà Tây', N'0241234577', N'852 Quang Trung, Hà Đông, Hà Nội')
INSERT [dbo].[NhaCungCap] ([idNCC], [tenNCC], [sdt], [diaChi]) VALUES (N'NCC012', N'Boston Pharma', N'0281234578', N'963 Võ Văn Tần, Quận 3, TP.HCM')
GO
INSERT [dbo].[NhanVien] ([idNV], [hoTen], [sdt], [gioiTinh], [namSinh], [ngayVaoLam], [luong], [trangThai]) VALUES (N'NV001', N'Nguyễn Văn An', N'0901234567', N'Nam', 1985, CAST(N'2020-01-15' AS Date), N'15000000', N'Đang làm việc')
INSERT [dbo].[NhanVien] ([idNV], [hoTen], [sdt], [gioiTinh], [namSinh], [ngayVaoLam], [luong], [trangThai]) VALUES (N'NV002', N'Trần Thị Bình', N'0912345678', N'Nữ', 1990, CAST(N'2021-03-20' AS Date), N'12000000', N'Đang làm việc')
INSERT [dbo].[NhanVien] ([idNV], [hoTen], [sdt], [gioiTinh], [namSinh], [ngayVaoLam], [luong], [trangThai]) VALUES (N'NV004', N'Phạm Thị Dung', N'0934567890', N'Nữ', 1992, CAST(N'2022-02-01' AS Date), N'11000000', N'Đang làm việc')
INSERT [dbo].[NhanVien] ([idNV], [hoTen], [sdt], [gioiTinh], [namSinh], [ngayVaoLam], [luong], [trangThai]) VALUES (N'NV005', N'Hoàng Văn Em', N'0945678901', N'Nam', 1987, CAST(N'2018-09-15' AS Date), N'14000000', N'Đã nghỉ việc')
INSERT [dbo].[NhanVien] ([idNV], [hoTen], [sdt], [gioiTinh], [namSinh], [ngayVaoLam], [luong], [trangThai]) VALUES (N'NV006', N'Vũ Thị Phương', N'0956789012', N'Nữ', 1993, CAST(N'2023-01-10' AS Date), N'10500000', N'Đang làm việc')
INSERT [dbo].[NhanVien] ([idNV], [hoTen], [sdt], [gioiTinh], [namSinh], [ngayVaoLam], [luong], [trangThai]) VALUES (N'NV007', N'Đỗ Minh Giang', N'0967890123', N'Nam', 1989, CAST(N'2020-07-20' AS Date), N'13000000', N'Đang làm việc')
INSERT [dbo].[NhanVien] ([idNV], [hoTen], [sdt], [gioiTinh], [namSinh], [ngayVaoLam], [luong], [trangThai]) VALUES (N'NV008', N'Ngô Thị Hoa', N'0978901234', N'Nữ', 1991, CAST(N'2021-11-05' AS Date), N'11500000', N'Đang làm việc')
GO
INSERT [dbo].[PhanHoi] ([idPH], [idKH], [idHD], [idThuoc], [noiDung], [thoiGian], [danhGia]) VALUES (N'PH001', N'KH001', N'HD001', N'T001', N'Thuốc rất hiệu quả, giảm đau nhanh chóng. Tôi rất hài lòng với chất lượng sản phẩm.', CAST(N'2024-06-02T15:30:00.000' AS DateTime), 5)
INSERT [dbo].[PhanHoi] ([idPH], [idKH], [idHD], [idThuoc], [noiDung], [thoiGian], [danhGia]) VALUES (N'PH002', N'KH002', N'HD002', N'T003', N'Vitamin C chất lượng tốt, đóng gói cẩn thận. Sẽ tiếp tục mua ở nhà thuốc.', CAST(N'2024-06-03T10:45:00.000' AS DateTime), 4)
INSERT [dbo].[PhanHoi] ([idPH], [idKH], [idHD], [idThuoc], [noiDung], [thoiGian], [danhGia]) VALUES (N'PH003', N'KH004', N'HD004', N'T009', N'Thuốc ho hiệu quả nhưng vị hơi đắng. Tuy nhiên vẫn dễ uống và có tác dụng nhanh.', CAST(N'2024-06-06T14:20:00.000' AS DateTime), 4)
INSERT [dbo].[PhanHoi] ([idPH], [idKH], [idHD], [idThuoc], [noiDung], [thoiGian], [danhGia]) VALUES (N'PH004', N'KH005', N'HD005', N'T012', N'Thuốc nhỏ mắt rất tốt, làm sạch và làm dịu mắt hiệu quả. Giá cả hợp lý.', CAST(N'2024-06-08T16:15:00.000' AS DateTime), 5)
INSERT [dbo].[PhanHoi] ([idPH], [idKH], [idHD], [idThuoc], [noiDung], [thoiGian], [danhGia]) VALUES (N'PH005', N'KH008', N'HD008', N'T014', N'Gel bôi ngoài da có tác dụng chống viêm tốt, thoa lên không gây kích ứng da.', CAST(N'2024-06-16T11:30:00.000' AS DateTime), 4)
GO
INSERT [dbo].[PhieuNhap] ([idPN], [thoiGian], [idNV], [idNCC], [tongTien]) VALUES (N'PN001', CAST(N'2024-01-15T09:30:00.000' AS DateTime), N'NV002', N'NCC001', 15000000)
INSERT [dbo].[PhieuNhap] ([idPN], [thoiGian], [idNV], [idNCC], [tongTien]) VALUES (N'PN002', CAST(N'2024-01-20T14:15:00.000' AS DateTime), N'NV004', N'NCC002', 22500000)
INSERT [dbo].[PhieuNhap] ([idPN], [thoiGian], [idNV], [idNCC], [tongTien]) VALUES (N'PN003', CAST(N'2024-02-05T10:45:00.000' AS DateTime), N'NV002', N'NCC003', 18750000)
INSERT [dbo].[PhieuNhap] ([idPN], [thoiGian], [idNV], [idNCC], [tongTien]) VALUES (N'PN004', CAST(N'2024-02-18T16:20:00.000' AS DateTime), N'NV006', N'NCC004', 12300000)
INSERT [dbo].[PhieuNhap] ([idPN], [thoiGian], [idNV], [idNCC], [tongTien]) VALUES (N'PN005', CAST(N'2024-03-08T11:10:00.000' AS DateTime), N'NV004', N'NCC005', 28900000)
INSERT [dbo].[PhieuNhap] ([idPN], [thoiGian], [idNV], [idNCC], [tongTien]) VALUES (N'PN006', CAST(N'2024-03-22T13:35:00.000' AS DateTime), N'NV007', N'NCC006', 16800000)
INSERT [dbo].[PhieuNhap] ([idPN], [thoiGian], [idNV], [idNCC], [tongTien]) VALUES (N'PN007', CAST(N'2024-04-10T15:50:00.000' AS DateTime), N'NV002', N'NCC007', 21400000)
INSERT [dbo].[PhieuNhap] ([idPN], [thoiGian], [idNV], [idNCC], [tongTien]) VALUES (N'PN008', CAST(N'2024-04-25T09:25:00.000' AS DateTime), N'NV008', N'NCC008', 19200000)
INSERT [dbo].[PhieuNhap] ([idPN], [thoiGian], [idNV], [idNCC], [tongTien]) VALUES (N'PN009', CAST(N'2024-05-12T12:15:00.000' AS DateTime), N'NV006', N'NCC009', 14600000)
INSERT [dbo].[PhieuNhap] ([idPN], [thoiGian], [idNV], [idNCC], [tongTien]) VALUES (N'PN010', CAST(N'2024-05-28T14:40:00.000' AS DateTime), N'NV004', N'NCC010', 25750000)
GO
INSERT [dbo].[TaiKhoan] ([idTK], [username], [password], [idNV], [idVT]) VALUES (N'TK001', N'admin', N'admin123', N'NV001', N'VT01')
INSERT [dbo].[TaiKhoan] ([idTK], [username], [password], [idNV], [idVT]) VALUES (N'TK002', N'nvbinh', N'binh123', N'NV002', N'VT02')
INSERT [dbo].[TaiKhoan] ([idTK], [username], [password], [idNV], [idVT]) VALUES (N'TK004', N'nvdung', N'dung123', N'NV004', N'VT002')
INSERT [dbo].[TaiKhoan] ([idTK], [username], [password], [idNV], [idVT]) VALUES (N'TK005', N'nvem', N'em123', N'NV005', N'VT002')
INSERT [dbo].[TaiKhoan] ([idTK], [username], [password], [idNV], [idVT]) VALUES (N'TK006', N'nvphuong', N'phuong123', N'NV006', N'VT002')
INSERT [dbo].[TaiKhoan] ([idTK], [username], [password], [idNV], [idVT]) VALUES (N'TK007', N'nvgiang', N'giang123', N'NV007', N'VT002')
INSERT [dbo].[TaiKhoan] ([idTK], [username], [password], [idNV], [idVT]) VALUES (N'TK008', N'nvhoa', N'hoa123', N'NV008', N'VT002')
GO
INSERT [dbo].[Thuoc] ([idThuoc], [tenThuoc], [hinhAnh], [thanhPhan], [donViTinh], [danhMuc], [xuatXu], [soLuongTon], [giaNhap], [donGia], [hanSuDung]) VALUES (N'T001', N'Paracetamol 500mg', NULL, N'Paracetamol 500mg', N'Viên', N'Thuốc giảm đau', N'Việt Nam', 500, 1500, 2000, CAST(N'2026-12-31' AS Date))
INSERT [dbo].[Thuoc] ([idThuoc], [tenThuoc], [hinhAnh], [thanhPhan], [donViTinh], [danhMuc], [xuatXu], [soLuongTon], [giaNhap], [donGia], [hanSuDung]) VALUES (N'T002', N'Amoxicillin 250mg', NULL, N'Amoxicillin 250mg', N'Viên', N'Kháng sinh', N'Ấn Độ', 300, 3000, 4000, CAST(N'2025-08-15' AS Date))
INSERT [dbo].[Thuoc] ([idThuoc], [tenThuoc], [hinhAnh], [thanhPhan], [donViTinh], [danhMuc], [xuatXu], [soLuongTon], [giaNhap], [donGia], [hanSuDung]) VALUES (N'T003', N'Vitamin C 1000mg', NULL, N'Acid Ascorbic 1000mg', N'Viên', N'Vitamin', N'Đức', 200, 15000, 20000, CAST(N'2027-03-20' AS Date))
INSERT [dbo].[Thuoc] ([idThuoc], [tenThuoc], [hinhAnh], [thanhPhan], [donViTinh], [danhMuc], [xuatXu], [soLuongTon], [giaNhap], [donGia], [hanSuDung]) VALUES (N'T004', N'Aspirin 81mg', NULL, N'Acetylsalicylic acid 81mg', N'Viên', N'Thuốc tim mạch', N'Mỹ', 400, 2500, 3500, CAST(N'2026-06-30' AS Date))
INSERT [dbo].[Thuoc] ([idThuoc], [tenThuoc], [hinhAnh], [thanhPhan], [donViTinh], [danhMuc], [xuatXu], [soLuongTon], [giaNhap], [donGia], [hanSuDung]) VALUES (N'T005', N'Ibuprofen 400mg', NULL, N'Ibuprofen 400mg', N'Viên', N'Thuốc chống viêm', N'Anh', 350, 4000, 5500, CAST(N'2025-11-10' AS Date))
INSERT [dbo].[Thuoc] ([idThuoc], [tenThuoc], [hinhAnh], [thanhPhan], [donViTinh], [danhMuc], [xuatXu], [soLuongTon], [giaNhap], [donGia], [hanSuDung]) VALUES (N'T006', N'Cetirizine 10mg', NULL, N'Cetirizine HCl 10mg', N'Viên', N'Thuốc dị ứng', N'Thái Lan', 250, 2000, 3000, CAST(N'2026-09-25' AS Date))
INSERT [dbo].[Thuoc] ([idThuoc], [tenThuoc], [hinhAnh], [thanhPhan], [donViTinh], [danhMuc], [xuatXu], [soLuongTon], [giaNhap], [donGia], [hanSuDung]) VALUES (N'T007', N'Omeprazole 20mg', NULL, N'Omeprazole 20mg', N'Viên', N'Thuốc dạ dày', N'Hàn Quốc', 180, 8000, 12000, CAST(N'2025-12-15' AS Date))
INSERT [dbo].[Thuoc] ([idThuoc], [tenThuoc], [hinhAnh], [thanhPhan], [donViTinh], [danhMuc], [xuatXu], [soLuongTon], [giaNhap], [donGia], [hanSuDung]) VALUES (N'T008', N'Metformin 500mg', NULL, N'Metformin HCl 500mg', N'Viên', N'Thuốc tiểu đường', N'Việt Nam', 400, 3500, 5000, CAST(N'2026-04-18' AS Date))
INSERT [dbo].[Thuoc] ([idThuoc], [tenThuoc], [hinhAnh], [thanhPhan], [donViTinh], [danhMuc], [xuatXu], [soLuongTon], [giaNhap], [donGia], [hanSuDung]) VALUES (N'T009', N'Cough Syrup', NULL, N'Dextromethorphan 15mg/5ml', N'Chai', N'Thuốc ho', N'Singapore', 100, 25000, 35000, CAST(N'2025-07-22' AS Date))
INSERT [dbo].[Thuoc] ([idThuoc], [tenThuoc], [hinhAnh], [thanhPhan], [donViTinh], [danhMuc], [xuatXu], [soLuongTon], [giaNhap], [donGia], [hanSuDung]) VALUES (N'T010', N'Betadine Solution', NULL, N'Povidone Iodine 10%', N'Chai', N'Thuốc sát trùng', N'Thái Lan', 150, 18000, 25000, CAST(N'2026-01-30' AS Date))
INSERT [dbo].[Thuoc] ([idThuoc], [tenThuoc], [hinhAnh], [thanhPhan], [donViTinh], [danhMuc], [xuatXu], [soLuongTon], [giaNhap], [donGia], [hanSuDung]) VALUES (N'T011', N'Calcium 600mg', NULL, N'Calcium Carbonate 600mg', N'Viên', N'Thuốc bổ sung canxi', N'Úc', 300, 12000, 16000, CAST(N'2027-05-12' AS Date))
INSERT [dbo].[Thuoc] ([idThuoc], [tenThuoc], [hinhAnh], [thanhPhan], [donViTinh], [danhMuc], [xuatXu], [soLuongTon], [giaNhap], [donGia], [hanSuDung]) VALUES (N'T012', N'Eye Drops', NULL, N'Sodium Chloride 0.9%', N'Chai', N'Thuốc nhỏ mắt', N'Nhật Bản', 80, 30000, 42000, CAST(N'2025-10-08' AS Date))
INSERT [dbo].[Thuoc] ([idThuoc], [tenThuoc], [hinhAnh], [thanhPhan], [donViTinh], [danhMuc], [xuatXu], [soLuongTon], [giaNhap], [donGia], [hanSuDung]) VALUES (N'T013', N'Loratadine 10mg', NULL, N'Loratadine 10mg', N'Viên', N'Thuốc dị ứng', N'Ấn Độ', 220, 2800, 4200, CAST(N'2026-08-14' AS Date))
INSERT [dbo].[Thuoc] ([idThuoc], [tenThuoc], [hinhAnh], [thanhPhan], [donViTinh], [danhMuc], [xuatXu], [soLuongTon], [giaNhap], [donGia], [hanSuDung]) VALUES (N'T014', N'Diclofenac Gel', NULL, N'Diclofenac Sodium 1%', N'Tuýp', N'Thuốc bôi ngoài da', N'Đức', 120, 35000, 50000, CAST(N'2025-09-30' AS Date))
INSERT [dbo].[Thuoc] ([idThuoc], [tenThuoc], [hinhAnh], [thanhPhan], [donViTinh], [danhMuc], [xuatXu], [soLuongTon], [giaNhap], [donGia], [hanSuDung]) VALUES (N'T015', N'Multivitamin', NULL, N'Vitamin tổng hợp', N'Viên', N'Vitamin', N'Mỹ', 180, 45000, 65000, CAST(N'2027-02-28' AS Date))
GO
INSERT [dbo].[VaiTro] ([idVT], [ten]) VALUES (N'VT001', N'Admin')
INSERT [dbo].[VaiTro] ([idVT], [ten]) VALUES (N'VT002', N'Nhân viên')
INSERT [dbo].[VaiTro] ([idVT], [ten]) VALUES (N'VT01', N'qtv')
INSERT [dbo].[VaiTro] ([idVT], [ten]) VALUES (N'VT02', N'nv')
GO
ALTER TABLE [dbo].[PhanHoi] ADD  DEFAULT (getdate()) FOR [thoiGian]
GO
ALTER TABLE [dbo].[ChiTietHoaDon]  WITH CHECK ADD  CONSTRAINT [FK__ChiTietHo__idThu__571DF1D5] FOREIGN KEY([idThuoc])
REFERENCES [dbo].[Thuoc] ([idThuoc])
GO
ALTER TABLE [dbo].[ChiTietHoaDon] CHECK CONSTRAINT [FK__ChiTietHo__idThu__571DF1D5]
GO
ALTER TABLE [dbo].[ChiTietHoaDon]  WITH CHECK ADD  CONSTRAINT [FK__ChiTietHoa__idHD__5629CD9C] FOREIGN KEY([idHD])
REFERENCES [dbo].[HoaDon] ([idHD])
GO
ALTER TABLE [dbo].[ChiTietHoaDon] CHECK CONSTRAINT [FK__ChiTietHoa__idHD__5629CD9C]
GO
ALTER TABLE [dbo].[ChiTietPhieuNhap]  WITH CHECK ADD  CONSTRAINT [FK__ChiTietPh__idThu__60A75C0F] FOREIGN KEY([idThuoc])
REFERENCES [dbo].[Thuoc] ([idThuoc])
GO
ALTER TABLE [dbo].[ChiTietPhieuNhap] CHECK CONSTRAINT [FK__ChiTietPh__idThu__60A75C0F]
GO
ALTER TABLE [dbo].[ChiTietPhieuNhap]  WITH CHECK ADD  CONSTRAINT [FK__ChiTietPhi__idPN__5FB337D6] FOREIGN KEY([idPN])
REFERENCES [dbo].[PhieuNhap] ([idPN])
GO
ALTER TABLE [dbo].[ChiTietPhieuNhap] CHECK CONSTRAINT [FK__ChiTietPhi__idPN__5FB337D6]
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD  CONSTRAINT [FK__HoaDon__idKH__534D60F1] FOREIGN KEY([idKH])
REFERENCES [dbo].[KhachHang] ([idKH])
GO
ALTER TABLE [dbo].[HoaDon] CHECK CONSTRAINT [FK__HoaDon__idKH__534D60F1]
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD  CONSTRAINT [FK__HoaDon__idNV__52593CB8] FOREIGN KEY([idNV])
REFERENCES [dbo].[NhanVien] ([idNV])
GO
ALTER TABLE [dbo].[HoaDon] CHECK CONSTRAINT [FK__HoaDon__idNV__52593CB8]
GO
ALTER TABLE [dbo].[HopDong]  WITH CHECK ADD  CONSTRAINT [FK_HopDong_NhaCungCap] FOREIGN KEY([idNCC])
REFERENCES [dbo].[NhaCungCap] ([idNCC])
GO
ALTER TABLE [dbo].[HopDong] CHECK CONSTRAINT [FK_HopDong_NhaCungCap]
GO
ALTER TABLE [dbo].[HopDong]  WITH CHECK ADD  CONSTRAINT [FK_HopDong_NhanVien] FOREIGN KEY([idNV])
REFERENCES [dbo].[NhanVien] ([idNV])
GO
ALTER TABLE [dbo].[HopDong] CHECK CONSTRAINT [FK_HopDong_NhanVien]
GO
ALTER TABLE [dbo].[PhanHoi]  WITH CHECK ADD  CONSTRAINT [FK__PhanHoi__17F790F9] FOREIGN KEY([idHD], [idThuoc])
REFERENCES [dbo].[ChiTietHoaDon] ([idHD], [idThuoc])
GO
ALTER TABLE [dbo].[PhanHoi] CHECK CONSTRAINT [FK__PhanHoi__17F790F9]
GO
ALTER TABLE [dbo].[PhanHoi]  WITH CHECK ADD  CONSTRAINT [FK__PhanHoi__idKH__17036CC0] FOREIGN KEY([idKH])
REFERENCES [dbo].[KhachHang] ([idKH])
GO
ALTER TABLE [dbo].[PhanHoi] CHECK CONSTRAINT [FK__PhanHoi__idKH__17036CC0]
GO
ALTER TABLE [dbo].[PhieuNhap]  WITH CHECK ADD  CONSTRAINT [FK__PhieuNhap__idNCC__5CD6CB2B] FOREIGN KEY([idNCC])
REFERENCES [dbo].[NhaCungCap] ([idNCC])
GO
ALTER TABLE [dbo].[PhieuNhap] CHECK CONSTRAINT [FK__PhieuNhap__idNCC__5CD6CB2B]
GO
ALTER TABLE [dbo].[PhieuNhap]  WITH CHECK ADD  CONSTRAINT [FK__PhieuNhap__idNV__5BE2A6F2] FOREIGN KEY([idNV])
REFERENCES [dbo].[NhanVien] ([idNV])
GO
ALTER TABLE [dbo].[PhieuNhap] CHECK CONSTRAINT [FK__PhieuNhap__idNV__5BE2A6F2]
GO
ALTER TABLE [dbo].[TaiKhoan]  WITH CHECK ADD  CONSTRAINT [FK__TaiKhoan__idNV__3B75D760] FOREIGN KEY([idNV])
REFERENCES [dbo].[NhanVien] ([idNV])
GO
ALTER TABLE [dbo].[TaiKhoan] CHECK CONSTRAINT [FK__TaiKhoan__idNV__3B75D760]
GO
ALTER TABLE [dbo].[TaiKhoan]  WITH CHECK ADD  CONSTRAINT [FK__TaiKhoan__idVT__3C69FB99] FOREIGN KEY([idVT])
REFERENCES [dbo].[VaiTro] ([idVT])
GO
ALTER TABLE [dbo].[TaiKhoan] CHECK CONSTRAINT [FK__TaiKhoan__idVT__3C69FB99]
GO
ALTER TABLE [dbo].[HopDong]  WITH CHECK ADD  CONSTRAINT [CK__HopDong__29221CFB] CHECK  (([idNV] IS NOT NULL AND [idNCC] IS NULL OR [idNV] IS NULL AND [idNCC] IS NOT NULL))
GO
ALTER TABLE [dbo].[HopDong] CHECK CONSTRAINT [CK__HopDong__29221CFB]
GO
ALTER TABLE [dbo].[PhanHoi]  WITH CHECK ADD  CONSTRAINT [CK__PhanHoi__danhGia__160F4887] CHECK  (([danhGia]>=(1) AND [danhGia]<=(5)))
GO
ALTER TABLE [dbo].[PhanHoi] CHECK CONSTRAINT [CK__PhanHoi__danhGia__160F4887]
GO
USE [master]
GO
ALTER DATABASE [QLTHUOC] SET  READ_WRITE 
GO
