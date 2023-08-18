CREATE TABLE IF NOT EXISTS Currencies
(
    ID       INTEGER PRIMARY KEY AUTOINCREMENT,
    Code     VARCHAR(30) NOT NULL,
    FullName VARCHAR(30) NOT NULL,
    Sign     VARCHAR(30) NOT NULL,
    UNIQUE (Code)
);


CREATE TABLE IF NOT EXISTS ExchangeRates
(
    ID               INTEGER PRIMARY KEY AUTOINCREMENT,
    BaseCurrencyId   INTEGER,
    TargetCurrencyId INTEGER,
    Rate             BIGINT(20, 6) NOT NULL,
    FOREIGN KEY (BaseCurrencyId) REFERENCES Currencies (ID),
    FOREIGN KEY (TargetCurrencyId) REFERENCES Currencies (ID),
    UNIQUE (BaseCurrencyId, TargetCurrencyId)
);