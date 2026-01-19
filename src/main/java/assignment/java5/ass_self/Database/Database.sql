use JAVA5_ASS

create table Users
(
    UserID       BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    FullName     VARCHAR(50)                       NOT NULL,
    Email        VARCHAR(50)                       NOT NULL UNIQUE,
    Phone        VARCHAR(15)                       NULL,
    Avatar       NVARCHAR(255),
    DateOfBirth  DATE,
    Gender       VARCHAR(10),
    PasswordHash VARCHAR(255)                      NOT NULL,
    IsActive     BIT      DEFAULT 1,
    IsDeleted    BIT      DEFAULT 0,
    CreatedAt    DATETIME DEFAULT SYSDATETIME(),
    CONSTRAINT CK_Users_Email CHECK (Email LIKE '%_@__%.__%'),
    CONSTRAINT CK_Users_Phone CHECK (Phone LIKE '[0-9]%' AND LEN(Phone) >= 10 AND LEN(Phone) <= 15)
);

CREATE TABLE EmailVerificationTokens (
     TokenID BIGINT IDENTITY PRIMARY KEY,
    UserID BIGINT NOT NULL,
    Token NVARCHAR(255) NOT NULL,
    ExpiredAt DATETIME NOT NULL,
    IsUsed BIT DEFAULT 0,
    CreatedAt DATETIME DEFAULT SYSDATETIME(),

CONSTRAINT FK_Verify_User
     FOREIGN KEY (UserID) REFERENCES Users(UserID)
);



create table Roles
(
    RoleID   INT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    RoleName VARCHAR(50)                    NOT NULL UNIQUE,
);
create table UserRoles
(
    UserID BIGINT NOT NULL,
    RoleID INT    NOT NULL,
    PRIMARY KEY (UserID, RoleID),
    FOREIGN KEY (UserID) REFERENCES Users (UserID),
    FOREIGN KEY (RoleID) REFERENCES Roles (RoleID)
);
create table UserAddresses
(
    AddressID    BIGINT IDENTITY (1,1) PRIMARY KEY,
    UserID       BIGINT NOT NULL,
    ReceiverName VARCHAR(50),
    AddressLine  NVARCHAR(255),
    Province     NVARCHAR(100),
    District     NVARCHAR(100),
    Ward         NVARCHAR(100),
    AddressType  NVARCHAR(50) DEFAULT 'Home',
    Phone        VARCHAR(15),
    IsDefault    BIT          DEFAULT 0,
    IsDeleted    BIT          DEFAULT 0,
    FOREIGN KEY (UserID) REFERENCES Users (UserID),
    CONSTRAINT CK_UserAddresses_AddressType CHECK (AddressType IN ('Home', 'Official', 'Other'))
);
create table Publishers
(
    PublisherID   INT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    PublisherName NVARCHAR(150)                  NOT NULL,
    ContactEmail  VARCHAR(100),
    Address       NVARCHAR(255),
    IsDeleted     BIT DEFAULT 0
);
create table Books
(
    BookID      BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    Title       NVARCHAR(255)                     NOT NULL,
    Slug        NVARCHAR(255)                     NOT NULL UNIQUE,
    ISBN        VARCHAR(20),
    Description NVARCHAR(MAX),
    Price       DECIMAL(18, 2)                    NOT NULL,
    PublisherID INT,
    PublishYear INT,
    Language    NVARCHAR(50),
    SoldCount   INT      DEFAULT 0,
    IsActive    BIT      DEFAULT 1,
    IsDeleted   BIT      DEFAULT 0,
    CreatedAt   DATETIME DEFAULT SYSDATETIME(),
    FOREIGN KEY (PublisherID) REFERENCES Publishers (PublisherID)
);
create table Categories
(
    CategoryID   INT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    CategoryName NVARCHAR(100)                  NOT NULL,
    ParentID     INT                            NULL,
    IsDeleted    BIT DEFAULT 0,
    FOREIGN KEY (ParentID) REFERENCES Categories (CategoryID)
);
create table BookCategories
(
    BookID     BIGINT NOT NULL,
    CategoryID INT    NOT NULL,
    PRIMARY KEY (BookID, CategoryID),
    FOREIGN KEY (BookID) REFERENCES Books (BookID),
    FOREIGN KEY (CategoryID) REFERENCES Categories (CategoryID)
);
create table Authors
(
    AuthorID   BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    AuthorName NVARCHAR(150)                     NOT NULL,
    Biography  NVARCHAR(MAX),
    IsDeleted  BIT DEFAULT 0
);

create table BookAuthors
(
    BookID   BIGINT NOT NULL,
    AuthorID BIGINT NOT NULL,
    PRIMARY KEY (BookID, AuthorID),
    FOREIGN KEY (BookID) REFERENCES Books (BookID),
    FOREIGN KEY (AuthorID) REFERENCES Authors (AuthorID)
);
create table BookImages
(
    ImageID  BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    BookID   BIGINT                            NOT NULL,
    ImageURL NVARCHAR(255)                     NOT NULL,
    IsMain   BIT DEFAULT 0,
    FOREIGN KEY (BookID) REFERENCES Books (BookID)
);

create table CategoriesImages
(
    ImageID    BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    CategoryID INT                               NOT NULL,
    ImageURL   NVARCHAR(255)                     NOT NULL,
    IsMain     BIT DEFAULT 0,
    IsDeleted  BIT DEFAULT 0,
    FOREIGN KEY (CategoryID) REFERENCES Categories (CategoryID)
);

create table Inventories
(
    BookID   BIGINT PRIMARY KEY NOT NULL,
    Quantity INT                NOT NULL CHECK (Quantity >= 0),
    Reserved INT DEFAULT 0,
    FOREIGN KEY (BookID) REFERENCES Books (BookID)
);
create table Carts
(
    CartID    BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    UserID    BIGINT                            NOT NULL,
    CreatedAt DATETIME DEFAULT SYSDATETIME(),
    FOREIGN KEY (UserID) REFERENCES Users (UserID)
);
create table CartItems
(
    CartID   BIGINT NOT NULL,
    BookID   BIGINT NOT NULL,
    Quantity INT    NOT NULL CHECK (Quantity > 0),
    PRIMARY KEY (CartID, BookID),
    FOREIGN KEY (CartID) REFERENCES Carts (CartID),
    FOREIGN KEY (BookID) REFERENCES Books (BookID)
);
CREATE TABLE Vouchers
(
    VoucherID         BIGINT IDENTITY (1,1) PRIMARY KEY,
    Code              VARCHAR(50)    NOT NULL UNIQUE,
    Description       NVARCHAR(255),
    DiscountType      NVARCHAR(20)   NOT NULL, -- Percent, Fixed
    DiscountValue     DECIMAL(18, 2) NOT NULL,
    MinOrderAmount    DECIMAL(18, 2) DEFAULT 0,
    MaxDiscountAmount DECIMAL(18, 2),
    UsageLimit        INT,                     -- Tổng số lần dùng
    UsedCount         INT            DEFAULT 0,
    StartDate         DATETIME       NOT NULL,
    EndDate           DATETIME       NOT NULL,
    IsActive          BIT            DEFAULT 1,
    CreatedAt         DATETIME       DEFAULT SYSDATETIME(),
    CONSTRAINT CK_Vouchers_DiscountType CHECK (DiscountType IN ('Percent', 'Fixed')),
    CONSTRAINT CK_Vouchers_Dates CHECK (EndDate > StartDate)
);


create table Orders
(
    OrderID        BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    UserID         BIGINT                            NOT NULL,
    AddressID      BIGINT                            NOT NULL,
    TotalAmount    DECIMAL(18, 2),
    CurrentStatus  NVARCHAR(50),
    CanceledReason NVARCHAR(255),
    CanceledAt     DATETIME,
    CreatedAt      DATETIME       DEFAULT SYSDATETIME(),
    CompletedAt    DATETIME,
    VoucherID      BIGINT,
    PaymentMethod  NVARCHAR(50),
    ShippingFee    DECIMAL(18, 2) DEFAULT 0,
    DiscountAmount DECIMAL(18, 2) DEFAULT 0,
    Note           NVARCHAR(255),
    FOREIGN KEY (UserID) REFERENCES Users (UserID),
    FOREIGN KEY (AddressID) REFERENCES UserAddresses (AddressID),
    FOREIGN KEY (VoucherID) REFERENCES Vouchers (VoucherID),
    CONSTRAINT CK_Orders_PaymentMethod CHECK (PaymentMethod IN ('VNPAY', 'VCB', 'COD', 'Banking', 'Other')),
    CONSTRAINT CK_Orders_CurrentStatus CHECK (CurrentStatus IN
                                              ('Pending', 'Processing', 'Shipped', 'Delivered', 'Canceled',
                                               'Returned') )
);
CREATE TABLE VoucherUsage
(
    VoucherUsageID BIGINT IDENTITY (1,1) PRIMARY KEY,
    VoucherID      BIGINT NOT NULL,
    UserID         BIGINT NOT NULL,
    OrderID        BIGINT,
    UsedAt         DATETIME DEFAULT SYSDATETIME(),
    FOREIGN KEY (VoucherID) REFERENCES Vouchers (VoucherID),
    FOREIGN KEY (UserID) REFERENCES Users (UserID),
    FOREIGN KEY (OrderID) REFERENCES Orders (OrderID)
);
create table OrderItems
(
    OrderID  BIGINT         NOT NULL,
    BookID   BIGINT         NOT NULL,
    Quantity INT            NOT NULL CHECK (Quantity > 0),
    Price    DECIMAL(18, 2) NOT NULL,
    PRIMARY KEY (OrderID, BookID),
    FOREIGN KEY (OrderID) REFERENCES Orders (OrderID),
    FOREIGN KEY (BookID) REFERENCES Books (BookID)
);
create table OrderStatusHistory
(
    HistoryID BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    OrderID   BIGINT                            NOT NULL,
    Status    NVARCHAR(50)                      NOT NULL,
    Note      NVARCHAR(255),
    ChangedAt DATETIME DEFAULT SYSDATETIME(),
    FOREIGN KEY (OrderID) REFERENCES Orders (OrderID),
    CONSTRAINT CK_OrderStatusHistory_Status CHECK (Status IN
                                                   ('Pending', 'Processing', 'Shipped', 'Delivered', 'Canceled',
                                                    'Returned'))
);
create table Payments
(
    PaymentID       BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    OrderID         BIGINT                            NOT NULL,
    Provider        NVARCHAR(100),
    TransactionCode NVARCHAR(150),
    Amount          DECIMAL(18, 2)                    NOT NULL,
    Status          NVARCHAR(50) DEFAULT 'Pending', -- Pending, Success, Failed, Refunded
    ResponseData    NVARCHAR(MAX),                  -- JSON response từ payment gateway
    CONSTRAINT CK_Payments_Status CHECK (Status IN ('Pending', 'Success', 'Failed', 'Refunded')),
    PaymentDate     DATETIME     DEFAULT SYSDATETIME(),
    FOREIGN KEY (OrderID) REFERENCES Orders (OrderID),
);
create table Shipping
(
    ShippingID            BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    OrderID               BIGINT                            NOT NULL,
    ShippingProvider      NVARCHAR(100),
    TrackingNumber        NVARCHAR(150),
    ShippingStatus        NVARCHAR(50),
    EstimatedDeliveryDate DATETIME,
    ActualDeliveryDate    DATETIME,
    ShippedAt             DATETIME,
    FOREIGN KEY (OrderID) REFERENCES Orders (OrderID)
);
create table Reviews
(
    ReviewID  BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    BookID    BIGINT                            NOT NULL,
    UserID    BIGINT                            NOT NULL,
    Rating    INT CHECK (Rating BETWEEN 1 AND 5),
    Comment   NVARCHAR(MAX),
    CreatedAt DATETIME DEFAULT SYSDATETIME(),
    FOREIGN KEY (BookID) REFERENCES Books (BookID),
    FOREIGN KEY (UserID) REFERENCES Users (UserID)
);
create table Promotions
(
    PromotionID     BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    PromotionName   NVARCHAR(50),
    DiscountPercent int CHECK (DiscountPercent BETWEEN 0 AND 100),
    StartDate       DATETIME                          NOT NULL,
    EndDate         DATETIME                          NOT NULL
);
create table PromotionBooks
(
    PromotionID BIGINT NOT NULL,
    BookID      BIGINT NOT NULL,
    PRIMARY KEY (PromotionID, BookID),
    FOREIGN KEY (PromotionID) REFERENCES Promotions (PromotionID),
    FOREIGN KEY (BookID) REFERENCES Books (BookID)
);

CREATE TABLE Notifications
(
    NotificationID BIGINT IDENTITY (1,1) PRIMARY KEY,
    UserID         BIGINT        NOT NULL,
    Title          NVARCHAR(255) NOT NULL,
    Content        NVARCHAR(MAX),
    Type           NVARCHAR(50), -- Order, Promotion, System
    IsRead         BIT      DEFAULT 0,
    CreatedAt      DATETIME DEFAULT SYSDATETIME(),
    FOREIGN KEY (UserID) REFERENCES Users (UserID)
);
CREATE TABLE Wishlists
(
    UserID    BIGINT NOT NULL,
    BookID    BIGINT NOT NULL,
    CreatedAt DATETIME DEFAULT SYSDATETIME(),
    PRIMARY KEY (UserID, BookID),
    FOREIGN KEY (UserID) REFERENCES Users (UserID),
    FOREIGN KEY (BookID) REFERENCES Books (BookID)
);

CREATE INDEX IDX_Notifications_UserID_IsRead ON Notifications (UserID, IsRead);
CREATE INDEX IDX_Vouchers_Code ON Vouchers (Code);
CREATE INDEX IDX_Books_Title ON Books (Title);
CREATE UNIQUE INDEX IDX_Books_Slug ON Books (Slug);
CREATE INDEX IDX_Orders_User_CreatedAt ON Orders (UserID, CreatedAt DESC);
CREATE INDEX IDX_OrderItems_OrderID ON OrderItems (OrderID);
CREATE INDEX IDX_CartItems_CartID ON CartItems (CartID);
CREATE INDEX IDX_Reviews_BookID ON Reviews (BookID);
CREATE INDEX IDX_BookCategories_CategoryID ON BookCategories (CategoryID);
CREATE INDEX IDX_OrderStatusHistory_OrderID ON OrderStatusHistory (OrderID);
CREATE INDEX IDX_Users_Email ON Users (Email);
CREATE INDEX IDX_Books_ISBN ON Books (ISBN);
CREATE INDEX IDX_Books_PublisherID ON Books (PublisherID);
CREATE INDEX IDX_Books_IsActive_IsDeleted ON Books (IsActive, IsDeleted);
CREATE INDEX IDX_Inventories_Quantity ON Inventories (Quantity);
CREATE INDEX IDX_Reviews_UserID ON Reviews (UserID);
CREATE INDEX IDX_Reviews_Rating ON Reviews (Rating);
CREATE INDEX IDX_Payments_TransactionCode ON Payments (TransactionCode);
CREATE INDEX IDX_Payments_Status ON Payments (Status);
CREATE INDEX IDX_Shipping_OrderID ON Shipping (OrderID);
CREATE INDEX IDX_VoucherUsage_VoucherID ON VoucherUsage (VoucherID);
CREATE INDEX IDX_VoucherUsage_UserID ON VoucherUsage (UserID);
CREATE INDEX IDX_BookAuthors_AuthorID ON BookAuthors (AuthorID);
CREATE INDEX IDX_PromotionBooks_BookID ON PromotionBooks (BookID);
CREATE INDEX IDX_Orders_Status_CreatedAt ON Orders (CurrentStatus, CreatedAt DESC);
CREATE INDEX IDX_Wishlists_UserID ON Wishlists (UserID);
CREATE INDEX IDX_CategoryImages_CategoryID ON CategoriesImages (CategoryID);

-- Insert Roles
INSERT INTO Roles (RoleName)
VALUES ('Admin'),
       ('Customer'),
       ('Manager');

-- Insert Publishers
INSERT INTO Publishers (PublisherName, ContactEmail, Address)
VALUES (N'Nhà Xuất Bản Trẻ', 'nxbtre@nxbtre.com.vn', N'161B Lý Chính Thắng, Quận 3, TP.HCM'),
       (N'Nhà Xuất Bản Kim Đồng', 'kimdong@nxbkimdong.com.vn', N'55 Quang Trung, Hai Bà Trưng, Hà Nội'),
       (N'Nhà Xuất Bản Văn Học', 'info@nxbvanhoc.com.vn', N'18 Nguyễn Trường Tộ, Ba Đình, Hà Nội'),
       (N'Nhà Xuất Bản Tổng Hợp TPHCM', 'info@nxbhcm.com.vn', N'62 Nguyễn Thị Minh Khai, Quận 1, TP.HCM'),
       (N'Nhà Xuất Bản Lao Động', 'nxblaodong@laodong.vn', N'175 Giảng Võ, Đống Đa, Hà Nội'),
       (N'Nhà Xuất Bản Hội Nhà Văn', 'info@nxbhnv.com.vn', N'65 Nguyễn Du, Hai Bà Trưng, Hà Nội'),
       (N'Nhà Xuất Bản Phụ Nữ', 'nxbphunu@phunu.vn', N'39 Hàng Chuối, Hai Bà Trưng, Hà Nội'),
       (N'Nhà Xuất Bản Thế Giới', 'info@thegioipublishers.vn', N'46 Trần Hưng Đạo, Hoàn Kiếm, Hà Nội'),
       (N'Nhà Xuất Bản Dân Trí', 'nxbdantri@dantri.vn', N'9 Phạm Ngọc Thạch, Đống Đa, Hà Nội'),
       (N'Nhà Xuất Bản Thanh Niên', 'info@nxbthanhnien.vn', N'64 Bà Triệu, Hoàn Kiếm, Hà Nội'),
       (N'Nhà Xuất Bản Đại Học Quốc Gia', 'info@vnupress.vn', N'16 Hàng Chuối, Hai Bà Trưng, Hà Nội'),
       (N'Alpha Books', 'contact@alphabooks.vn', N'84 Trần Quốc Toản, Quận 3, TP.HCM');

-- Insert Categories (với phân cấp cha-con)
INSERT INTO Categories (CategoryName, ParentID)
VALUES (N'Văn học', NULL),
       (N'Kinh tế', NULL),
       (N'Khoa học - Công nghệ', NULL),
       (N'Thiếu nhi', NULL),
       (N'Tâm lý - Kỹ năng sống', NULL),
       (N'Lịch sử', NULL),
       (N'Triết học', NULL),
       (N'Tiểu thuyết', 1),
       (N'Quản trị kinh doanh', 2),
       (N'Marketing', 2),
       (N'Tài chính', 2),
       (N'Công nghệ thông tin', 3);


-- Insert Authors
INSERT INTO Authors (AuthorName, Biography)
VALUES (N'Nguyễn Nhật Ánh', N'Nhà văn nổi tiếng Việt Nam, tác giả của nhiều tác phẩm văn học thiếu nhi'),
       (N'Nguyễn Ngọc Tư', N'Nhà văn nữ người Việt Nam, được biết đến với văn phong độc đáo'),
       (N'Nguyễn Du', N'Đại thi hào Việt Nam, tác giả Truyện Kiều'),
       (N'Tô Hoài', N'Nhà văn, nhà báo, tác giả Dế Mèn phiêu lưu ký'),
       (N'Nam Cao', N'Nhà văn hiện thực Việt Nam'),
       (N'Vũ Trọng Phụng', N'Nhà văn, nhà báo nổi tiếng thời kỳ cận đại'),
       (N'Dale Carnegie', N'Tác giả nổi tiếng người Mỹ về lĩnh vực phát triển bản thân'),
       (N'Napoleon Hill', N'Tác giả sách về thành công và triết lý làm giàu'),
       (N'Paulo Coelho', N'Nhà văn Brazil nổi tiếng thế giới'),
       (N'Haruki Murakami', N'Nhà văn Nhật Bản đương đại'),
       (N'J.K. Rowling', N'Tác giả người Anh, nổi tiếng với series Harry Potter'),
       (N'George Orwell', N'Nhà văn, nhà báo người Anh'),
       (N'Malcolm Gladwell', N'Nhà văn, nhà báo người Canada'),
       (N'Simon Sinek', N'Tác giả, diễn giả về lãnh đạo và quản lý'),
       (N'Robert Kiyosaki', N'Doanh nhân, nhà đầu tư và tác giả người Mỹ'),
        (N'Fujiko F. Fujio', N'Nhà sáng tạo truyện tranh Doraemon'),
       (N'Huỳnh Thái Ngọc', N'Một họa sĩ và nhà sáng tạo nội dung người Việt, người đã tạo ra loạt truyện tranh và phim hoạt hình độc đáo này từ năm 2014'),
       (N'Vô Danh', null),
       (N'Tạ Huy Long', N'Một nhà văn và nhà báo người Việt Nam, nổi tiếng với các tác phẩm về lịch sử và văn hóa Việt Nam'),
       (N'Keith Ferrazzi', N'Tác giả và diễn giả nổi tiếng về networking và xây dựng mối quan hệ'),
       (N'Rosie Nguyễn', N'Tác giả người Việt Nam, nổi tiếng với các tác phẩm về phát triển bản thân và kỹ năng sống'),
       (N'Dominique Loreau', N'Tác giả người Pháp, nổi tiếng với các tác phẩm về phong cách sống tối giản'),
       (N'Tony Buổi Sáng', N'Tác giả và doanh nhân người Việt Nam, nổi tiếng với các tác phẩm về kinh doanh và phát triển bản thân');


-- Insert Books - Văn học (10 cuốn)
INSERT INTO Books (Title, Slug, ISBN, Description, Price, PublisherID, PublishYear, Language, SoldCount, IsActive)
VALUES (N'Cho Tôi Xin Một Vé Đi Tuổi Thơ', 'cho-toi-xin-mot-ve-di-tuoi-tho', '9786041127272',
        N'Tác phẩm mang màu sắc hoài niệm về tuổi thơ miền quê', 95000, 1, 2018, N'Tiếng Việt', 1250, 1),
       (N'Mắt Biếc', 'mat-biec', '9786041127289', N'Câu chuyện tình yêu đẹp và buồn của Ngạn và Hà Lan', 105000, 1,
        2019, N'Tiếng Việt', 2100, 1),
       (N'Tôi Thấy Hoa Vàng Trên Cỏ Xanh', 'toi-thay-hoa-vang-tren-co-xanh', '9786041127296',
        N'Truyện về tuổi thơ của hai anh em Thiều và Tường', 115000, 1, 2020, N'Tiếng Việt', 1890, 1),
       (N'Truyện Kiều', 'truyen-kieu', '9786041001234', N'Tác phẩm kinh điển của văn học Việt Nam', 85000, 3, 2017,
        N'Tiếng Việt', 980, 1),
       (N'Dế Mèn Phiêu Lưu Ký', 'de-men-phieu-luu-ky', '9786041001241', N'Câu chuyện phiêu lưu của chú dế mèn', 75000,
        2, 2018, N'Tiếng Việt', 1560, 1),
       (N'Số Đỏ', 'so-do', '9786041001258', N'Tác phẩm hiện thực phê phán của Vũ Trọng Phụng', 95000, 3, 2019,
        N'Tiếng Việt', 720, 1),
       (N'Chí Phèo', 'chi-pheo', '9786041001265', N'Truyện ngắn nổi tiếng của Nam Cao', 65000, 3, 2018, N'Tiếng Việt',
        890, 1),
       (N'Lão Hạc', 'lao-hac', '9786041001272', N'Tác phẩm cảm động về tình người', 70000, 3, 2017, N'Tiếng Việt', 1050,
        1),
       (N'Tắt Đèn', 'tat-den', '9786041001289', N'Tiểu thuyết hiện thực của Ngô Tất Tố', 85000, 3, 2019, N'Tiếng Việt',
        670, 1),
       (N'Hai Đứa Trẻ', 'hai-dua-tre', '9786041001296', N'Truyện ngắn nổi tiếng của Thạch Lam', 60000, 3, 2020,
        N'Tiếng Việt', 540, 1);

-- Insert Books - Kinh tế (10 cuốn)
INSERT INTO Books (Title, Slug, ISBN, Description, Price, PublisherID, PublishYear, Language, SoldCount, IsActive)
VALUES (N'Đắc Nhân Tâm', 'dac-nhan-tam', '9786041002001', N'Nghệ thuật thu phục lòng người', 125000, 1, 2020,
        N'Tiếng Việt', 3500, 1),
       (N'Nghĩ Giàu Và Làm Giàu', 'nghi-giau-va-lam-giau', '9786041002018', N'13 nguyên tắc nghĩ giàu làm giàu', 135000,
        5, 2019, N'Tiếng Việt', 2890, 1),
       (N'Cha Giàu Cha Nghèo', 'cha-giau-cha-ngheo', '9786041002025', N'Bài học về đầu tư và tài chính', 145000, 5,
        2021, N'Tiếng Việt', 3120, 1),
       (N'Khởi Nghiệp Tinh Gọn', 'khoi-nghiep-tinh-gon', '9786041002032', N'Phương pháp khởi nghiệp hiệu quả', 155000,
        12, 2020, N'Tiếng Việt', 1670, 1),
       (N'Tư Duy Nhanh Và Chậm', 'tu-duy-nhanh-va-cham', '9786041002049', N'Nghiên cứu về cách con người ra quyết định',
        165000, 4, 2021, N'Tiếng Việt', 1890, 1);

-- Insert Books - Khoa học - Công nghệ (10 cuốn)
INSERT INTO Books (Title, Slug, ISBN, Description, Price, PublisherID, PublishYear, Language, SoldCount, IsActive)
VALUES (N'Lập Trình Java Cơ Bản', 'lap-trinh-java-co-ban', '9786041003001', N'Giáo trình học lập trình Java từ đầu',
        195000, 11, 2021, N'Tiếng Việt', 1120, 1),
       (N'Python Cho Người Mới Bắt Đầu', 'python-cho-nguoi-moi-bat-dau', '9786041003018',
        N'Học Python dễ dàng và hiệu quả', 165000, 11, 2020, N'Tiếng Việt', 1450, 1),
       (N'Trí Tuệ Nhân Tạo Căn Bản', 'tri-tue-nhan-tao-can-ban', '9786041003025',
        N'Giới thiệu về AI và Machine Learning', 225000, 11, 2021, N'Tiếng Việt', 890, 1),
       (N'Big Data Và Phân Tích Dữ Liệu', 'big-data-va-phan-tich-du-lieu', '9786041003032',
        N'Kỹ thuật xử lý dữ liệu lớn', 245000, 11, 2020, N'Tiếng Việt', 670, 1),
       (N'Blockchain Và Ứng Dụng', 'blockchain-va-ung-dung', '9786041003049', N'Công nghệ blockchain và cryptocurrency',
        215000, 11, 2021, N'Tiếng Việt', 560, 1);

-- Insert Books - Thiếu nhi (10 cuốn)
INSERT INTO Books (Title, Slug, ISBN, Description, Price, PublisherID, PublishYear, Language, SoldCount, IsActive)
VALUES (N'Dế Mèn Phiêu Lưu Ký', 'de-men-phieu-luu-ky-thieu-nhi', '9786041004001', N'Cuộc phiêu lưu của chú dế mèn',
        85000, 2, 2020, N'Tiếng Việt', 2340, 1),
       (N'Doraemon - Chú Mèo Máy Đến Từ Tương Lai', 'doraemon-chu-meo-may', '9786041004018',
        N'Truyện tranh Doraemon tập 1', 25000, 2, 2021, N'Tiếng Việt', 4560, 1),
       (N'Thỏ Bảy Màu', 'tho-bay-mau', '9786041004025', N'Truyện tranh cho trẻ em', 30000, 2, 2020, N'Tiếng Việt', 3450,
        1),
       (N'Cổ Tích Việt Nam', 'co-tich-viet-nam', '9786041004032', N'Tuyển tập cổ tích dân gian', 95000, 2, 2019,
        N'Tiếng Việt', 1890, 1),
       (N'Truyện Tranh Lịch Sử Việt Nam', 'truyen-tranh-lich-su-viet-nam', '9786041004049', N'Lịch sử qua tranh',
        125000, 2, 2021, N'Tiếng Việt', 1560, 1);

-- Insert Books - Tâm lý - Kỹ năng sống (10 cuốn)
INSERT INTO Books (Title, Slug, ISBN, Description, Price, PublisherID, PublishYear, Language, SoldCount, IsActive)
VALUES (N'Đừng Bao Giờ Đi Ăn Một Mình', 'dung-bao-gio-di-an-mot-minh', '9786041005001', N'Nghệ thuật networking',
        135000, 1, 2020, N'Tiếng Việt', 1890, 1),
       (N'Tuổi Trẻ Đáng Giá Bao Nhiêu', 'tuoi-tre-dang-gia-bao-nhieu', '9786041005018',
        N'Tự truyện và bài học cuộc sống', 95000, 1, 2019, N'Tiếng Việt', 2560, 1),
       (N'Nghệ Thuật Sống Tối Giản', 'nghe-thuat-song-toi-gian', '9786041005025', N'Sống đơn giản để hạnh phúc hơn',
        115000, 1, 2021, N'Tiếng Việt', 1450, 1),
       (N'Quẳng Gánh Lo Đi Và Vui Sống', 'quang-ganh-lo-di-va-vui-song', '9786041005032',
        N'Vượt qua lo âu trong cuộc sống', 105000, 1, 2020, N'Tiếng Việt', 2120, 1),
       (N'Cà Phê Cùng Tony', 'ca-phe-cung-tony', '9786041005049', N'Bài học kinh doanh và cuộc sống', 85000, 1, 2019,
        N'Tiếng Việt', 1670, 1);

-- Insert BookAuthors (liên kết sách với tác giả)
INSERT INTO BookAuthors (BookID, AuthorID)
VALUES (1, 1),
       (2, 1),
       (3, 1),   -- Nguyễn Nhật Ánh
       (4, 3),   -- Nguyễn Du
       (5, 4),   -- Tô Hoài
       (6, 6),
       (7, 5),
       (8, 5),
       (9, 5),
       (10, 5),  -- Vũ Trọng Phụng, Nam Cao
       (11, 7), -- Dale Carnegie
       (12, 8),  -- Napoleon Hill
       (13, 15), -- Robert Kiyosaki
       (14, 13),
       (15, 13), -- Malcolm Gladwell
       (16, 7),  -- Dale Carnegie
       (17, 14),
       (20, 14), -- Simon Sinek
       (18, 15),
       (19, 15),
       (22, 16),
       (23, 17),
       (24, 18),
       (25, 19), -- Vô Danh, Huỳnh Thái Ngọc
       (26, 20),
       (27, 21), -- Keith Ferrazzi, Rosie Nguyễn
       (28, 22),
       (29, 7), -- Dominique Loreau
       (30, 23);  -- Tony Buổi Sáng
-- Tâm lý

-- Insert BookCategories (liên kết sách với danh mục)
INSERT INTO BookCategories (BookID, CategoryID)
VALUES
-- Văn học
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(5, 4),
(6, 1),
(7, 1),
(8, 1),
(9, 1),
(10, 1),
(11, 5),
(12, 11),
(13, 11),
(14, 11),
(15, 5),
(16, 12),
(17, 12),
(18, 12),
(19, 12),
(20, 12),
(22, 4),
(23, 4),
(24, 4),
(25, 4),
(26, 5),
(27, 1),
(28, 5),
(29, 5),
(30, 5);


-- Insert BookImages
INSERT INTO BookImages (BookID, ImageURL, IsMain)
VALUES (1, 'cho_toi_mot_ve_ve_tuoi_tho.jpg', 1),
       (2, 'mat_biec.jpg', 1),
       (3, 'toi_thay_hoa_vang_tren_co xanh.jpg', 1),
       (4, 'truyen_kieu.jpg', 1),
       (5, 'de_men_phieu_luu_ky.jpg', 1),
       (6, 'so_do.jpg', 1),
       (7, 'chi_pheo.jpg', 1),
       (8, 'lao_hac.jpg', 1),
       (9, 'tat_den.jpg', 1),
       (10, 'hai_dua_tre.jpg', 1),
       (11, 'dac_nhan_tam.jpg', 1),
       (12, 'nghi_giau_lam_giau.jpg', 1),
       (13, 'cha_giau_cha_ngheo.jpg', 1),
       (14, 'khoi_nghiep_tinh_gon.jpg', 1),
       (15, 'tu_duy_nhanh_va_cham.jpg', 1),
       (16, 'lap_trinh_java_co_ban.jpg', 1),
       (17, 'python_cho_nguoi_moi_bat_dau.jpg', 1),
       (18, 'tri_tue_nhan_tao_can_ban.jpg', 1),
       (19, 'big_data_va_phan_tich_du_lieu.jpg', 1),
       (20, 'blockchain_va_ung_dung.jpg', 1),
       (22, 'doraemon_chu_meo_may.jpg', 1),
       (23, 'tho_bay_mau.jpg', 1),
       (24, 'co_tich_viet_nam.jpg', 1),
       (25, 'truyen_tranh_lich_su_viet_nam.jpg', 1),
       (26, 'dung_bao_gio_di_an_mot_minh.jpg', 1),
       (27, 'tuoi_tre_dang_gia_bao_nhieu.jpg', 1),
       (28, 'nghe_thuat_song_toi_gian.jpg', 1),
       (29, 'quang_ganh_lo_di_va_vui_song.jpg', 1),
       (30, 'ca_phe_cung_tony.jpg', 1);


-- Insert Inventories
INSERT INTO Inventories (BookID, Quantity, Reserved)
VALUES (1, 150, 10),
       (2, 200, 15),
       (3, 180, 12),
       (4, 120, 8),
       (5, 160, 10),
       (6, 90, 5),
       (7, 100, 7),
       (8, 110, 6),
       (9, 95, 5),
       (10, 85, 4),
       (11, 300, 25),
       (12, 250, 20),
       (13, 280, 22),
       (14, 180, 15),
       (15, 200, 18),
       (16, 220, 17),
       (17, 190, 14),
       (18, 170, 12),
       (19, 210, 16),
       (20, 240, 19),
       (21, 140, 10),
       (22, 160, 12),
       (23, 120, 8),
       (24, 100, 7),
       (25, 90, 6),
       (26, 150, 11),
       (27, 130, 9),
       (28, 110, 8),
       (29, 80, 5),
       (30, 95, 6),
       (31, 300, 30),
       (32, 500, 50),
       (33, 400, 40),
       (34, 200, 20),
       (35, 180, 18),
       (36, 160, 15),
       (37, 220, 22),
       (38, 190, 19),
       (39, 600, 60),
       (40, 250, 25),
       (41, 200, 18),
       (42, 280, 25),
       (43, 170, 14),
       (44, 230, 20),
       (45, 190, 16),
       (46, 150, 12),
       (47, 140, 11),
       (48, 260, 23),
       (49, 210, 18),
       (50, 180, 15);

-- Insert Carts
INSERT INTO Carts (UserID)
VALUES (2),
       (4),
       (6),
       (7),
       (8),
       (9),
       (10),
       (11),
       (12),
       (5);

-- Insert CartItems
INSERT INTO CartItems (CartID, BookID, Quantity)
VALUES (1, 1, 2),
       (1, 11, 1),
       (1, 31, 3),
       (2, 2, 1),
       (2, 12, 2),
       (3, 3, 1),
       (3, 21, 1),
       (3, 41, 1),
       (4, 4, 2),
       (4, 13, 1),
       (5, 5, 1),
       (5, 32, 2),
       (5, 42, 1),
       (6, 11, 1),
       (6, 21, 1),
       (7, 12, 2),
       (7, 22, 1),
       (8, 31, 3),
       (8, 32, 2),
       (9, 41, 1),
       (9, 42, 1),
       (9, 43, 1),
       (10, 1, 1),
       (10, 2, 1),
       (10, 3, 1);

-- Insert Vouchers
INSERT INTO Vouchers (Code, Description, DiscountType, DiscountValue, MinOrderAmount, MaxDiscountAmount, UsageLimit,
                      UsedCount, StartDate, EndDate, IsActive)
VALUES ('NEWYEAR2024', N'Giảm 10% đơn hàng đầu năm', 'Percent', 10, 200000, 50000, 1000, 245, '2024-01-01',
        '2024-12-31', 1),
       ('WELCOME50K', N'Giảm 50K cho khách hàng mới', 'Fixed', 50000, 300000, 50000, 500, 123, '2024-01-01',
        '2024-12-31', 1),
       ('SUMMER2024', N'Giảm 15% mùa hè', 'Percent', 15, 500000, 100000, 2000, 567, '2024-06-01', '2024-08-31', 1),
       ('BOOK100K', N'Giảm 100K đơn từ 1 triệu', 'Fixed', 100000, 1000000, 100000, 300, 89, '2024-01-01', '2024-12-31',
        1),
       ('FREESHIP', N'Miễn phí ship cho đơn từ 500K', 'Fixed', 30000, 500000, 30000, 5000, 1234, '2024-01-01',
        '2024-12-31', 1),
       ('MEMBER20', N'Giảm 20% cho thành viên', 'Percent', 20, 300000, 150000, 1500, 456, '2024-01-01', '2024-12-31',
        1),
       ('FLASH30', N'Flash sale giảm 30%', 'Percent', 30, 200000, 200000, 100, 78, '2024-11-11', '2024-11-11', 0),
       ('STUDENT15', N'Giảm 15% cho sinh viên', 'Percent', 15, 150000, 75000, 2000, 890, '2024-01-01', '2024-12-31', 1),
       ('VIP200K', N'Giảm 200K cho VIP', 'Fixed', 200000, 2000000, 200000, 50, 23, '2024-01-01', '2024-12-31', 1),
       ('BIRTHDAY25', N'Giảm 25% sinh nhật', 'Percent', 25, 300000, 125000, 1000, 345, '2024-01-01', '2024-12-31', 1);

-- Insert Orders
INSERT INTO Orders (UserID, AddressID, TotalAmount, CurrentStatus, CreatedAt, VoucherID, PaymentMethod, ShippingFee,
                    DiscountAmount, Note)
VALUES (2, 1, 425000, 'Delivered', '2024-11-01', 1, 'COD', 30000, 42500, N'Giao giờ hành chính'),
       (4, 3, 680000, 'Delivered', '2024-11-05', 2, 'VNPAY', 30000, 50000, N'Gọi trước khi giao'),
       (6, 4, 950000, 'Shipped', '2024-11-10', 3, 'Banking', 30000, 142500, N'Để tại bảo vệ nếu không có người'),
       (8, 5, 320000, 'Processing', '2024-11-15', NULL, 'COD', 30000, 0, NULL),
       (9, 6, 1250000, 'Delivered', '2024-11-20', 4, 'VNPAY', 0, 100000, N'Hàng dễ vỡ xin cẩn thận'),
       (10, 7, 540000, 'Delivered', '2024-11-22', 5, 'COD', 0, 30000, NULL),
       (2, 2, 780000, 'Delivered', '2024-11-25', NULL, 'Banking', 30000, 0, N'Giao buổi tối'),
       (4, 3, 890000, 'Canceled', '2024-11-27', NULL, 'COD', 30000, 0, N'Đặt nhầm'),
       (6, 4, 1450000, 'Pending', '2024-11-28', 6, 'VNPAY', 30000, 290000, N'Thanh toán khi nhận hàng'),
       (8, 5, 620000, 'Delivered', '2024-11-29', NULL, 'COD', 30000, 0, NULL),
       (10, 7, 1100000, 'Processing', '2024-11-30', 8, 'Banking', 30000, 165000, N'Giao nhanh'),
       (11, 8, 750000, 'Shipped', '2024-12-01', NULL, 'VNPAY', 30000, 0, N'Kiểm tra hàng trước khi thanh toán');

-- Insert OrderItems
INSERT INTO OrderItems (OrderID, BookID, Quantity, Price)
VALUES (1, 1, 2, 95000),
       (1, 11, 1, 125000),
       (1, 31, 2, 85000),
       (2, 2, 1, 105000),
       (2, 12, 2, 135000),
       (2, 21, 1, 195000),
       (3, 3, 2, 115000),
       (3, 13, 1, 145000),
       (3, 22, 2, 165000),
       (3, 41, 1, 135000),
       (4, 4, 1, 85000),
       (4, 14, 1, 155000),
       (4, 32, 1, 25000),
       (5, 5, 2, 75000),
       (5, 15, 1, 165000),
       (5, 23, 2, 225000),
       (5, 42, 2, 95000),
       (5, 33, 3, 30000),
       (6, 11, 2, 125000),
       (6, 21, 1, 195000),
       (6, 31, 1, 85000),
       (7, 12, 2, 135000),
       (7, 22, 1, 165000),
       (7, 32, 3, 25000),
       (7, 42, 1, 95000),
       (7, 6, 1, 95000),
       (8, 13, 2, 145000),
       (8, 23, 1, 225000),
       (8, 43, 2, 115000),
       (9, 14, 2, 155000),
       (9, 24, 1, 245000),
       (9, 34, 3, 95000),
       (9, 44, 1, 105000),
       (9, 7, 2, 65000),
       (9, 17, 1, 175000),
       (10, 15, 1, 165000),
       (10, 25, 1, 215000),
       (10, 35, 2, 125000),
       (11, 16, 2, 95000),
       (11, 26, 1, 185000),
       (11, 36, 3, 105000),
       (11, 45, 1, 85000),
       (11, 8, 2, 70000),
       (12, 17, 1, 155000),
       (12, 27, 1, 175000),
       (12, 37, 2, 195000);

-- Insert OrderStatusHistory
INSERT INTO OrderStatusHistory (OrderID, Status, Note, ChangedAt)
VALUES (1, 'Pending', N'Đơn hàng mới', '2024-11-01 10:00:00'),
       (1, 'Processing', N'Đang xử lý', '2024-11-01 14:00:00'),
       (1, 'Shipped', N'Đã giao cho đơn vị vận chuyển', '2024-11-02 08:00:00'),
       (1, 'Delivered', N'Đã giao hàng thành công', '2024-11-03 16:30:00'),
       (2, 'Pending', N'Đơn hàng mới', '2024-11-05 09:15:00'),
       (2, 'Processing', N'Đang chuẩn bị hàng', '2024-11-05 15:20:00'),
       (2, 'Shipped', N'Đã xuất kho', '2024-11-06 10:00:00'),
       (2, 'Delivered', N'Giao hàng thành công', '2024-11-07 14:45:00'),
       (3, 'Pending', N'Chờ xác nhận', '2024-11-10 11:30:00'),
       (3, 'Processing', N'Đang đóng gói', '2024-11-10 16:00:00'),
       (3, 'Shipped', N'Đang trên đường giao', '2024-11-11 09:00:00'),
       (4, 'Pending', N'Đơn hàng mới', '2024-11-15 13:20:00'),
       (4, 'Processing', N'Đang xử lý', '2024-11-15 17:00:00'),
       (5, 'Pending', N'Chờ xử lý', '2024-11-20 08:45:00'),
       (5, 'Processing', N'Đang chuẩn bị', '2024-11-20 14:30:00'),
       (5, 'Shipped', N'Đã vận chuyển', '2024-11-21 07:00:00'),
       (5, 'Delivered', N'Hoàn thành', '2024-11-22 15:20:00');

-- Insert Payments
INSERT INTO Payments (OrderID, Provider, TransactionCode, Amount, Status, PaymentDate)
VALUES (1, 'COD', NULL, 382500, 'Success', '2024-11-03 16:30:00'),
       (2, 'VNPAY', 'VNP20241105091500', 630000, 'Success', '2024-11-05 09:20:00'),
       (3, 'VCB', 'VCB20241110113000', 807500, 'Success', '2024-11-10 11:35:00'),
       (4, 'COD', NULL, 350000, 'Pending', NULL),
       (5, 'VNPAY', 'VNP20241120084500', 1150000, 'Success', '2024-11-20 08:50:00'),
       (6, 'COD', NULL, 510000, 'Success', '2024-11-23 10:15:00'),
       (7, 'VCB', 'VCB20241125142000', 810000, 'Success', '2024-11-25 14:25:00'),
       (8, 'COD', NULL, 920000, 'Refunded', '2024-11-27 16:00:00'),
       (9, 'VNPAY', 'VNP20241128153000', 1160000, 'Pending', NULL),
       (10, 'COD', NULL, 650000, 'Success', '2024-11-30 11:45:00'),
       (11, 'VCB', 'VCB20241130162000', 935000, 'Pending', NULL),
       (12, 'VNPAY', 'VNP20241201103000', 780000, 'Success', '2024-12-01 10:35:00');

-- Insert Shipping
INSERT INTO Shipping (OrderID, ShippingProvider, TrackingNumber, ShippingStatus, EstimatedDeliveryDate,
                      ActualDeliveryDate, ShippedAt)
VALUES (1, N'Giao Hàng Nhanh', 'GHN123456789', 'Delivered', '2024-11-03', '2024-11-03 16:30:00', '2024-11-02 08:00:00'),
       (2, N'Giao Hàng Tiết Kiệm', 'GHTK987654321', 'Delivered', '2024-11-07', '2024-11-07 14:45:00',
        '2024-11-06 10:00:00'),
       (3, N'ViettelPost', 'VTP456789123', 'Shipped', '2024-11-13', NULL, '2024-11-11 09:00:00'),
       (4, N'Ninja Van', 'NV789123456', 'Processing', '2024-11-17', NULL, NULL),
       (5, N'J&T Express', 'JT321654987', 'Delivered', '2024-11-22', '2024-11-22 15:20:00', '2024-11-21 07:00:00'),
       (6, N'Giao Hàng Nhanh', 'GHN147258369', 'Delivered', '2024-11-24', '2024-11-23 10:15:00', '2024-11-22 14:00:00'),
       (7, N'ViettelPost', 'VTP258369147', 'Delivered', '2024-11-27', '2024-11-26 16:30:00', '2024-11-25 18:00:00'),
       (8, N'Giao Hàng Tiết Kiệm', 'GHTK369147258', 'Canceled', NULL, NULL, '2024-11-27 09:00:00'),
       (9, N'Ninja Van', 'NV741852963', 'Pending', '2024-12-02', NULL, NULL),
       (10, N'J&T Express', 'JT852963741', 'Delivered', '2024-12-02', '2024-11-30 11:45:00', '2024-11-29 15:00:00'),
       (11, N'Giao Hàng Nhanh', 'GHN963852741', 'Processing', '2024-12-04', NULL, NULL),
       (12, N'ViettelPost', 'VTP147852963', 'Shipped', '2024-12-04', NULL, '2024-12-01 13:00:00');

-- Insert VoucherUsage
INSERT INTO VoucherUsage (VoucherID, UserID, OrderID, UsedAt)
VALUES (1, 2, 1, '2024-11-01 10:00:00'),
       (2, 4, 2, '2024-11-05 09:15:00'),
       (3, 6, 3, '2024-11-10 11:30:00'),
       (4, 9, 5, '2024-11-20 08:45:00'),
       (5, 10, 6, '2024-11-22 10:30:00'),
       (6, 6, 9, '2024-11-28 15:30:00'),
       (8, 10, 11, '2024-11-30 16:20:00'),
       (1, 7, NULL, '2024-11-15 14:20:00'),
       (2, 8, NULL, '2024-11-18 09:45:00'),
       (3, 11, NULL, '2024-11-25 11:15:00'),
       (5, 12, NULL, '2024-11-27 16:30:00'),
       (10, 2, NULL, '2024-11-29 13:40:00');

-- Insert Reviews
INSERT INTO Reviews (BookID, UserID, Rating, Comment, CreatedAt)
VALUES (1, 2, 5, N'Sách rất hay, nhắc lại nhiều kỷ niệm tuổi thơ', '2024-11-04'),
       (1, 4, 5, N'Tuyệt vời, đọc rất cảm động', '2024-11-10'),
       (2, 6, 4, N'Câu chuyện buồn nhưng rất đẹp', '2024-11-12'),
       (11, 2, 5, N'Cuốn sách kinh điển về kỹ năng giao tiếp', '2024-11-05'),
       (11, 10, 5, N'Rất bổ ích cho công việc', '2024-11-24'),
       (12, 4, 4, N'Nhiều bài học hay về tư duy làm giàu', '2024-11-08'),
       (13, 8, 5, N'Cuốn sách hay về tư duy dạy con', '2024-11-16'),
       (22, 9, 5, N'Truyện Doraemon không bao giờ lỗi mốt', '2024-11-18'),
       (16, 6, 4, N'Sách về lập trình viết rất dễ hiểu', '2024-11-13'),
       (29, 10, 5, N'Giúp tôi thay đổi cách nhìn về cuộc sống', '2024-11-25'),
       (3, 8, 5, N'Một trong những tác phẩm hay nhất của tác giả', '2024-11-19'),
       (12, 9, 4, N'Bài học quản lý tài chính cá nhân rất hay', '2024-11-21');

-- Insert Promotions
INSERT INTO Promotions (PromotionName, DiscountPercent, StartDate, EndDate)
VALUES (N'Khuyến mãi tháng 11', 15, '2024-11-01', '2024-11-30'),
       (N'Black Friday 2024', 30, '2024-11-24', '2024-11-26'),
       (N'Giảm giá sách kinh tế', 20, '2024-11-01', '2024-12-31'),
       (N'Sale sách thiếu nhi', 25, '2024-11-15', '2024-12-15'),
       (N'Khuyến mãi cuối năm', 35, '2024-12-20', '2024-12-31'),
       (N'Flash sale giờ vàng', 40, '2024-11-11', '2024-11-11'),
       (N'Ưu đãi sách mới', 10, '2024-11-01', '2024-12-31'),
       (N'Giảm giá sách ngoại văn', 18, '2024-11-01', '2024-11-30'),
       (N'Sale tâm lý - kỹ năng', 22, '2024-11-10', '2024-12-10'),
       (N'Khuyến mãi đặc biệt', 50, '2024-12-12', '2024-12-12');

-- Insert PromotionBooks
INSERT INTO PromotionBooks (PromotionID, BookID)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 11),
       (1, 12),
       (2, 11),
       (2, 12),
       (2, 13),
       (2, 14),
       (2, 15),
       (2, 41),
       (2, 42),
       (3, 11),
       (3, 12),
       (3, 13),
       (3, 14),
       (3, 15),
       (3, 16),
       (3, 17),
       (3, 18),
       (3, 19),
       (3, 20),
       (4, 31),
       (4, 32),
       (4, 33),
       (4, 34),
       (4, 35),
       (4, 36),
       (4, 37),
       (4, 38),
       (4, 39),
       (4, 40),
       (5, 1),
       (5, 2),
       (5, 11),
       (5, 21),
       (5, 31),
       (5, 41),
       (7, 21),
       (7, 22),
       (7, 23),
       (7, 24),
       (7, 25),
       (9, 41),
       (9, 42),
       (9, 43),
       (9, 44),
       (9, 45),
       (9, 46),
       (9, 47),
       (9, 48),
       (9, 49),
       (9, 50);

-- Insert Notifications
INSERT INTO Notifications (UserID, Title, Content, Type, IsRead, CreatedAt)
VALUES (2, N'Đơn hàng đã giao', N'Đơn hàng #1 của bạn đã được giao thành công', 'Order', 1, '2024-11-03 16:30:00'),
       (2, N'Khuyến mãi đặc biệt', N'Giảm giá 20% cho đơn hàng tiếp theo', 'Promotion', 1, '2024-11-04 10:00:00'),
       (4, N'Đơn hàng đang vận chuyển', N'Đơn hàng #2 đang trên đường giao đến bạn', 'Order', 1, '2024-11-06 10:00:00'),
       (6, N'Xác nhận đơn hàng', N'Đơn hàng #3 đã được xác nhận', 'Order', 1, '2024-11-10 11:30:00'),
       (8, N'Cập nhật hệ thống', N'Hệ thống sẽ bảo trì vào 2h sáng ngày mai', 'System', 0, '2024-11-15 18:00:00'),
       (9, N'Đơn hàng hoàn thành', N'Cảm ơn bạn đã mua hàng. Hãy đánh giá sản phẩm', 'Order', 1, '2024-11-22 15:20:00'),
       (10, N'Flash Sale', N'Flash sale 50% từ 10h-12h hôm nay', 'Promotion', 0, '2024-11-23 09:00:00'),
       (11, N'Chào mừng thành viên mới', N'Chào mừng bạn đến với cửa hàng sách', 'System', 1, '2024-11-28 08:00:00'),
       (12, N'Sách mới về kho', N'Nhiều đầu sách mới đã có tại cửa hàng', 'Promotion', 0, '2024-11-29 14:00:00'),
       (2, N'Voucher sinh nhật', N'Bạn nhận được voucher giảm 25% nhân dịp sinh nhật', 'Promotion', 0,
        '2024-11-30 00:00:00'),
       (4, N'Nhắc nhở thanh toán', N'Đơn hàng #8 đang chờ thanh toán', 'Order', 0, '2024-11-27 10:00:00'),
       (6, N'Yêu cầu đánh giá', N'Bạn đã nhận hàng, hãy đánh giá cho chúng tôi', 'Order', 0, '2024-11-12 10:00:00');

-- Insert Wishlists
INSERT INTO Wishlists (UserID, BookID, CreatedAt)
VALUES (2, 4, '2024-11-01'),
       (2, 5, '2024-11-02'),
       (2, 14, '2024-11-03'),
       (4, 1, '2024-11-05'),
       (4, 15, '2024-11-06'),
       (4, 24, '2024-11-07'),
       (6, 2, '2024-11-10'),
       (6, 16, '2024-11-11'),
       (6, 43, '2024-11-12'),
       (8, 3, '2024-11-15'),
       (8, 17, '2024-11-16'),
       (8, 44, '2024-11-17'),
       (9, 6, '2024-11-20'),
       (9, 18, '2024-11-21'),
       (9, 45, '2024-11-22'),
       (10, 7, '2024-11-22'),
       (10, 19, '2024-11-23'),
       (10, 46, '2024-11-24'),
       (11, 8, '2024-11-28'),
       (11, 20, '2024-11-29'),
       (11, 47, '2024-11-30'),
       (12, 9, '2024-12-01'),
       (12, 25, '2024-12-01'),
       (12, 48, '2024-12-01');

PRINT 'Data inserted successfully!';