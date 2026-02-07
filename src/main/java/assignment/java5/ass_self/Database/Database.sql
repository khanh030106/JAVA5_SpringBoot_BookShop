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
    ImageURL NVARCHAR(255)                     NULL,
    IsMain   BIT DEFAULT 0,
    FOREIGN KEY (BookID) REFERENCES Books (BookID)
);

alter table BookImages
alter column ImageURL NVARCHAR(255) null;

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

