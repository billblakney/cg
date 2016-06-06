create text table investor (
   name varchar(50) PRIMARY KEY);
create text table broker (
   name varchar(20) PRIMARY KEY);
create text table acct_type (
   type varchar(50) PRIMARY KEY);
create text table acct (
   shortname varchar(10) PRIMARY KEY,
   longname varchar(100),
   type varchar(50),
   investor varchar(50),
   broker varchar(50),
   FOREIGN KEY (type) REFERENCES acct_type(type),
   FOREIGN KEY (investor) REFERENCES investor(name),
   FOREIGN KEY (broker) REFERENCES broker(name) );
create text table trade (
   acct varchar(10) NOT NULL,
   seqnum INT NOT NULL,
   date DATE NOT NULL,
   buysell varchar(4) NOT NULL,
   ticker varchar(6) NOT NULL,
   shares INT NOT NULL,
   price REAL NOT NULL,
   commission REAL NOT NULL,
   special_rule varchar(10),
   FOREIGN KEY (acct) REFERENCES acct(shortname),
   PRIMARY KEY (acct,seqnum) );
