MYSQL:
CREATE TABLE clienti( 
id INT NOT NULL, 
nazov_pc varchar(255),
kluc varchar(255),
ma_povolenie boolean,
cas_aktualizacie varchar(20),
kredit int,
minutych int,
PRIMARY KEY ( id ) );

CREATE TABLE povolenia( 
id INT NOT NULL AUTO_INCREMENT, 
nazov_pc varchar(255),
kluc varchar(255),
datum timestamp,
PRIMARY KEY ( id ) );

CREATE TABLE kluce( 
id INT NOT NULL AUTO_INCREMENT, 
kluc varchar(255),
poznamka varchar(500),
PRIMARY KEY ( id ) );

CREATE TABLE db_data( 
connection_string varchar(255),
username varchar(255),
password varchar(255));

CREATE TABLE log( 
id INT NOT NULL AUTO_INCREMENT, 
nazov_pc varchar(255),
kluc varchar(255),
datum timestamp,
typ varchar(20),
message varchar(500),
PRIMARY KEY ( id ) );

CREATE TABLE pripomienky( 
id INT NOT NULL AUTO_INCREMENT, 
nazov varchar(255),
datum timestamp,
message varchar(1000),
PRIMARY KEY ( id ) );


CREATE TABLE Inzeraty( 
id INT NOT NULL, 
portal varchar(255),
nazov varchar(255),
text varchar(4000),
meno varchar(255),
telefon varchar(255),
lokalita varchar(255),
aktualny_link varchar(255),
cena varchar(25),
time_inserted timestamp,
pocet_zobrazeni integer,
sent boolean,
zaujimavy boolean,
surne boolean,
precitany boolean,
typ varchar(20),
kategoria varchar(25),
PRIMARY KEY ( id ) );

CREATE TABLE emaily( 
id INT NOT NULL AUTO_INCREMENT, 
sender varchar(255),
recipient varchar(255),
datetime timestamp,
subject varchar(255),
message varchar(25000),
PRIMARY KEY ( id ) );