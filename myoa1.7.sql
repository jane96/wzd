USE [master]
GO
/****** Object:  Database [myoa]    Script Date: 2017/2/19 21:26:02 ******/
CREATE DATABASE [myoa]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'myoa', FILENAME = N'F:\sqlspace\MSSQL11.MSSQLSERVER\MSSQL\DATA\myoa.mdf' , SIZE = 5120KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'myoa_log', FILENAME = N'F:\sqlspace\MSSQL11.MSSQLSERVER\MSSQL\DATA\myoa_log.ldf' , SIZE = 5696KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
ALTER DATABASE [myoa] SET COMPATIBILITY_LEVEL = 110
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [myoa].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [myoa] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [myoa] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [myoa] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [myoa] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [myoa] SET ARITHABORT OFF 
GO
ALTER DATABASE [myoa] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [myoa] SET AUTO_CREATE_STATISTICS ON 
GO
ALTER DATABASE [myoa] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [myoa] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [myoa] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [myoa] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [myoa] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [myoa] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [myoa] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [myoa] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [myoa] SET  DISABLE_BROKER 
GO
ALTER DATABASE [myoa] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [myoa] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [myoa] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [myoa] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [myoa] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [myoa] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [myoa] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [myoa] SET RECOVERY FULL 
GO
ALTER DATABASE [myoa] SET  MULTI_USER 
GO
ALTER DATABASE [myoa] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [myoa] SET DB_CHAINING OFF 
GO
ALTER DATABASE [myoa] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [myoa] SET TARGET_RECOVERY_TIME = 0 SECONDS 
GO
EXEC sys.sp_db_vardecimal_storage_format N'myoa', N'ON'
GO
USE [myoa]
GO
/****** Object:  Table [dbo].[permission]    Script Date: 2017/2/19 21:26:02 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[permission](
	[id] [varchar](255) NOT NULL,
	[permission_name] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[player]    Script Date: 2017/2/19 21:26:02 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[player](
	[p_id] [varchar](255) NOT NULL,
	[date] [datetime] NULL,
	[p_number] [varchar](255) NULL,
	[p_wechat] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[p_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[proxy]    Script Date: 2017/2/19 21:26:02 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[proxy](
	[id] [varchar](255) NOT NULL,
	[date] [datetime] NULL,
	[proxy_phone] [varchar](255) NULL,
	[proxy_we_chat] [varchar](255) NULL,
	[user_id] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[role]    Script Date: 2017/2/19 21:26:02 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[role](
	[id] [varchar](255) NOT NULL,
	[role_name] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[role_permission]    Script Date: 2017/2/19 21:26:02 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[role_permission](
	[role_id] [varchar](255) NOT NULL,
	[permission_id] [varchar](255) NOT NULL
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[sale_record]    Script Date: 2017/2/19 21:26:02 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[sale_record](
	[id] [varchar](255) NOT NULL,
	[player_id] [varchar](255) NULL,
	[player_name] [varchar](255) NULL,
	[player_number] [varchar](255) NULL,
	[player_recharge_date] [datetime] NULL,
	[player_remark] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[users]    Script Date: 2017/2/19 21:26:02 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[users](
	[login_id] [varchar](255) NOT NULL,
	[password] [varchar](255) NULL,
	[role_id] [varchar](255) NULL,
	[name] [varchar](255) NULL,
	[record_number] [varchar](255) NULL,
	[current_number] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[login_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
INSERT [dbo].[permission] ([id], [permission_name]) VALUES (N'0b57f90833d646baa7c9db38dea3848d', N'uE8BYJeOSTs=')
INSERT [dbo].[permission] ([id], [permission_name]) VALUES (N'a6a75aef4cc74ef789c46c64efb61d11', N'1bmyiVifR48=')
INSERT [dbo].[permission] ([id], [permission_name]) VALUES (N'bd2b9a03958c4cb0b0e6b8bbddb99f23', N'wKiv1vjUV4A=')
INSERT [dbo].[permission] ([id], [permission_name]) VALUES (N'd8b78c457222498caed3432df0d1195b', N'uC+ZqmNtLhE=')
INSERT [dbo].[role] ([id], [role_name]) VALUES (N'47986e4309b54d23ad9985cceff391f2', N'4Kk1jeQVPYU=')
INSERT [dbo].[role] ([id], [role_name]) VALUES (N'542828d7cc5542bf9d4b6f0e84e4b056', N'fuHIrGNEws4=')
INSERT [dbo].[role] ([id], [role_name]) VALUES (N'72103fb9e3344b50b55e675dee32c238', N'I/8xlIRjjdk=')
INSERT [dbo].[role_permission] ([role_id], [permission_id]) VALUES (N'47986e4309b54d23ad9985cceff391f2', N'0b57f90833d646baa7c9db38dea3848d')
INSERT [dbo].[role_permission] ([role_id], [permission_id]) VALUES (N'542828d7cc5542bf9d4b6f0e84e4b056', N'0b57f90833d646baa7c9db38dea3848d')
INSERT [dbo].[role_permission] ([role_id], [permission_id]) VALUES (N'47986e4309b54d23ad9985cceff391f2', N'a6a75aef4cc74ef789c46c64efb61d11')
INSERT [dbo].[role_permission] ([role_id], [permission_id]) VALUES (N'542828d7cc5542bf9d4b6f0e84e4b056', N'a6a75aef4cc74ef789c46c64efb61d11')
INSERT [dbo].[role_permission] ([role_id], [permission_id]) VALUES (N'47986e4309b54d23ad9985cceff391f2', N'bd2b9a03958c4cb0b0e6b8bbddb99f23')
INSERT [dbo].[role_permission] ([role_id], [permission_id]) VALUES (N'542828d7cc5542bf9d4b6f0e84e4b056', N'bd2b9a03958c4cb0b0e6b8bbddb99f23')
INSERT [dbo].[role_permission] ([role_id], [permission_id]) VALUES (N'72103fb9e3344b50b55e675dee32c238', N'bd2b9a03958c4cb0b0e6b8bbddb99f23')
INSERT [dbo].[role_permission] ([role_id], [permission_id]) VALUES (N'47986e4309b54d23ad9985cceff391f2', N'd8b78c457222498caed3432df0d1195b')
INSERT [dbo].[role_permission] ([role_id], [permission_id]) VALUES (N'542828d7cc5542bf9d4b6f0e84e4b056', N'd8b78c457222498caed3432df0d1195b')

INSERT [dbo].[users] ([login_id], [password], [role_id], [name], [record_number], [current_number]) VALUES (N'001', N'GudemajkPqlpPNp8ZnpRJg==', N'542828d7cc5542bf9d4b6f0e84e4b056', N'HB8k8a6pnByM/9GudP4oxQ==', N'YQUUUUczOaI=', N'YQUUUUczOaI=')
INSERT [dbo].[users] ([login_id], [password], [role_id], [name], [record_number], [current_number]) VALUES (N'002', N'yUzBsf1giMNpPNp8ZnpRJg==', N'542828d7cc5542bf9d4b6f0e84e4b056', N'HB8k8a6pnByzd6ywx1srJg==', N'YQUUUUczOaI=', N'YQUUUUczOaI=')
INSERT [dbo].[users] ([login_id], [password], [role_id], [name], [record_number], [current_number]) VALUES (N'003', N'T74xJ5Ku2VppPNp8ZnpRJg==', N'542828d7cc5542bf9d4b6f0e84e4b056', N'HB8k8a6pnBzknqu5pdqN1Q==', N'YQUUUUczOaI=', N'YQUUUUczOaI=')
INSERT [dbo].[users] ([login_id], [password], [role_id], [name], [record_number], [current_number]) VALUES (N'004', N'8f/Yu/Mlt2FpPNp8ZnpRJg==', N'542828d7cc5542bf9d4b6f0e84e4b056', N'HB8k8a6pnBya0TBrMDPZPw==', N'YQUUUUczOaI=', N'YQUUUUczOaI=')
INSERT [dbo].[users] ([login_id], [password], [role_id], [name], [record_number], [current_number]) VALUES (N'005', N'k6Mk57XguehpPNp8ZnpRJg==', N'542828d7cc5542bf9d4b6f0e84e4b056', N'HB8k8a6pnBzYbjoQw38y8Q==', N'YQUUUUczOaI=', N'YQUUUUczOaI=')
ALTER TABLE [dbo].[proxy]  WITH CHECK ADD  CONSTRAINT [FK65FCA6E7720FB1E] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([login_id])
GO
ALTER TABLE [dbo].[proxy] CHECK CONSTRAINT [FK65FCA6E7720FB1E]
GO
ALTER TABLE [dbo].[role_permission]  WITH CHECK ADD  CONSTRAINT [FKBD40D538707899E] FOREIGN KEY([permission_id])
REFERENCES [dbo].[permission] ([id])
GO
ALTER TABLE [dbo].[role_permission] CHECK CONSTRAINT [FKBD40D538707899E]
GO
ALTER TABLE [dbo].[role_permission]  WITH CHECK ADD  CONSTRAINT [FKBD40D538D1F6373E] FOREIGN KEY([role_id])
REFERENCES [dbo].[role] ([id])
GO
ALTER TABLE [dbo].[role_permission] CHECK CONSTRAINT [FKBD40D538D1F6373E]
GO
ALTER TABLE [dbo].[users]  WITH CHECK ADD  CONSTRAINT [FK6A68E08D1F6373E] FOREIGN KEY([role_id])
REFERENCES [dbo].[role] ([id])
GO
ALTER TABLE [dbo].[users] CHECK CONSTRAINT [FK6A68E08D1F6373E]
GO
USE [master]
GO
ALTER DATABASE [myoa] SET  READ_WRITE 
GO
