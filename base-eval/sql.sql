create database btp;
create user btp with password 'btp';
grant all privileges on database btp to btp;

\c btp btp;

create table Utilisateur(
    login varchar(30),
    motdepasse varchar(50),
    telephone varchar(20) PRIMARY KEY DEFAULT '0',
    profil varchar(10) DEFAULT 'client'
);
insert into Utilisateur(login, motdepasse, profil) values ('btp', 'btp123', 'BTP');
insert into Utilisateur values (NULL, NULL, '0323022415', 'client');
insert into Utilisateur values (NULL, NULL, '0344524265', 'client');
insert into Utilisateur values (NULL, NULL, '0335122248', 'client');
insert into Utilisateur values (NULL, NULL, '0331011415', 'client');

create sequence sctypemaison increment by 1 start 1;
create table Typemaison(
    id varchar(20) DEFAULT nextval('sctypemaison'),
    nom varchar(50),
    description TEXT,
    surface double precision,
    dureetravaux double precision CHECK (dureetravaux > 0),
    lienillustration varchar(50) DEFAULT '',
    numero serial primary key
);

create table HistoDureeType
(
    id serial primary key,
    idtypemaison int references Typemaison(numero),
    dureetravaux double precision CHECK (dureetravaux > 0),
    created_at timestamp
);
CREATE OR REPLACE FUNCTION insert_HistoDureeType()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO HistoDureeType (idtypemaison, dureetravaux, created_at)
    VALUES (NEW.numero, NEW.dureetravaux, NOW());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER insert_after_typemaison_trigger
AFTER INSERT ON Typemaison
FOR EACH ROW
EXECUTE FUNCTION insert_HistoDureeType();

insert into Typemaison(nom, description, surface, dureetravaux, lienillustration) values('moderne', '4 chambres, 1 living, 1 salle de bain', 128, 50, 'img/maisons/maison-moderne.png');
insert into Typemaison(nom, description, surface, dureetravaux, lienillustration) values('contemporain', '2 chambres, 1 living, 1 salle de bain', 50, 70, 'img/maisons/maison-contemporain.png');
insert into Typemaison(nom, description, surface, dureetravaux, lienillustration) values('mitoyenne', '1 chambre, 1 salle de bain', 20, 20, 'img/maisons/maison-mitoyenne.png');
insert into Typemaison(nom, description, surface, dureetravaux, lienillustration) values('bioclimatique', '1 chambre', 30, 30, 'img/maisons/maison-bioclimatique.png');

create sequence scfinition increment by 1 start 1;
create table Finition(
    id varchar(50) DEFAULT nextval('scfinition'),
    nom varchar(20),
    pourcentage double precision,
    lienicone varchar(255) DEFAULT '',
    numero serial primary key
);

create table histoFinition(
    id serial primary key,
    idfinition int references Finition(numero),
    pourcentage double precision,
    created_at timestamp
);
CREATE OR REPLACE FUNCTION insert_Histofinition()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO histoFinition (idfinition, pourcentage, created_at)
    VALUES (NEW.numero, NEW.pourcentage, NOW());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER insert_after_finition_trigger
AFTER INSERT ON Finition
FOR EACH ROW
EXECUTE FUNCTION insert_Histofinition();

insert into finition(nom, pourcentage, lienicone) values ('Standard', 0, 'img/finition/standard.png');
insert into finition(nom, pourcentage, lienicone) values ('Gold', 2.5, 'img/finition/gold.png');
insert into finition(nom, pourcentage, lienicone) values ('Premium', 4, 'img/finition/premium.png');
insert into finition(nom, pourcentage, lienicone) values ('VIP', 7.5, 'img/finition/vip.png');

create sequence sclieu increment by 1 start 1;
create table Lieu(
    id varchar(50) DEFAULT nextval('sclieu'),
    nom varchar(50) NOT NULL,
    pourcentage_tarif double precision DEFAULT 0,
    numero serial primary key
);
insert into lieu(nom, pourcentage_tarif) values ('Behenjy', 0);
insert into lieu(nom, pourcentage_tarif) values ('Antsirabe', 0);
insert into lieu(nom, pourcentage_tarif) values ('Ihosy', 0);
insert into lieu(nom, pourcentage_tarif) values ('Tulear', 0);
insert into lieu(nom, pourcentage_tarif) values ('Mahajanga', 0);
insert into lieu(nom, pourcentage_tarif) values ('Antananarivo', 0);

create sequence scdevis increment by 1 start 1;
create table Devis(
    id varchar(50) DEFAULT nextval('scdevis'),
    telephone_client varchar(20) references Utilisateur(telephone),
    idtypemaison int references Typemaison(numero),
    idfinition int references Finition(numero),
    idlieu int references Lieu(numero),
    date_creation timestamp,
    date_debut_travaux timestamp,
    date_fin_travaux timestamp,
    paiement_termine int CHECK (paiement_termine between 0 and 1) DEFAULT 0,
    montant_total double precision default 0,
    numero serial primary key
);
-- insert into devis(telephone_client, idtypemaison, idfinition, idlieu, date_creation, date_debut_travaux, paiement_termine) values ('0323022415', '2', '1',  1, now(), '2024-06-11T12:00:00', 0);
-- insert into devis(telephone_client, idtypemaison, idfinition, idlieu, date_creation, date_debut_travaux, paiement_termine) values ('0323022415', '3', '3',  2, now(), '2024-06-17T12:00:00', 0);

-- insert into devis(telephone_client, idtypemaison, idfinition, idlieu, date_creation, date_debut_travaux, paiement_termine) values ('0344524265', '2', '2',  3, now(), '2024-05-21T00:00:00', 0);

-- insert into devis(telephone_client, idtypemaison, idfinition, idlieu, date_creation, date_debut_travaux, paiement_termine) values ('0335122248', '3', '1',  4, now(), '2024-06-15T12:00:00', 0);
-- insert into devis(telephone_client, idtypemaison, idfinition, idlieu, date_creation, date_debut_travaux, paiement_termine) values ('0335122248', '4', '1',  5, now(), '2025-06-10T12:00:00', 0);
-- insert into devis(telephone_client, idtypemaison, idfinition, idlieu, date_creation, date_debut_travaux, paiement_termine) values ('0335122248', '3', '2',  6, now(), '2026-06-09T12:00:00', 0);

create sequence scpaiement_devis increment by 1 start 1;
create table Paiement_devis(
    id varchar(50) DEFAULT nextval('scpaiement_devis'),
    iddevis int references Devis(numero),
    montant double precision,
    date_paiement timestamp,
    numero serial primary key
);
-- insert into paiement_devis(iddevis, montant, date_paiement) values (1, 1700000.50, '2024-06-01T00:00:00');
-- insert into paiement_devis(iddevis, montant, date_paiement) values (1, 2000000, '2024-06-08T00:00:00');

-- insert into paiement_devis(iddevis, montant, date_paiement) values (4, 20000000, '2024-07-08T00:00:00');

create sequence scunite increment by 1 start 1;
create table Unite(
    id varchar(50) DEFAULT nextval('scunite'),
    symbole varchar(10),
    numero serial primary key
);
insert into Unite(symbole) values ('m2');
insert into Unite(symbole) values ('m3');
insert into Unite(symbole) values ('fft');

create sequence sctravaux increment by 1 start 1;
create table Travaux(
    id varchar(50) DEFAULT nextval('sctravaux'),
    typetravaux varchar(50),
    prixunitaire double precision,
    idunite int references Unite(numero),
    quantite double precision,
    numero serial primary key
);

create table HistoPrixTravaux
(
    id serial primary key,
    idtravaux int references Travaux(numero),
    prixunitaire double precision,
    quantite double precision,
    created_at timestamp
);
CREATE OR REPLACE FUNCTION insert_HistoPrixTravaux()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (select 1 from HistoPrixTravaux WHERE idtravaux = NEW.numero) THEN
        INSERT INTO HistoPrixTravaux (idtravaux, prixunitaire, quantite, created_at)
        VALUES (NEW.numero, NEW.prixunitaire, new.quantite, NOW());
    ELSE
        INSERT INTO HistoPrixTravaux (idtravaux, prixunitaire, quantite, created_at)
        VALUES (NEW.numero, NEW.prixunitaire, new.quantite, '2000-01-01 00:00:00');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER insert_after_newtravaux_trigger
AFTER INSERT ON Travaux
FOR EACH ROW
EXECUTE FUNCTION insert_HistoPrixTravaux();

CREATE TRIGGER insert_after_updatetravaux_trigger
AFTER UPDATE ON Travaux
FOR EACH ROW
EXECUTE FUNCTION insert_HistoPrixTravaux();

insert into Travaux(typetravaux, prixunitaire, idunite, quantite) values ('mur de soutenement et demi cloture ht 1m', 190000.00, '2', 26.98);

insert into Travaux(typetravaux, prixunitaire, idunite, quantite) values ('decapage des terrains meubles', 3072.87, '1', 101.36);
insert into Travaux(typetravaux, prixunitaire, idunite, quantite) values ('dressage du plateforme', 3736.26, '1', 101.36);
insert into Travaux(typetravaux, prixunitaire, idunite, quantite) values ('fouille d`ouvrage terrain ferme', 9390.93, '2', 24.44);
-- insert into Travaux(typetravaux, prixunitaire, idunite, quantite) values ('remblai d`ouvrage');
-- insert into Travaux(typetravaux, prixunitaire, idunite, quantite) values ('travaux d`implantation');

-- insert into Travaux(typetravaux, prixunitaire, idunite, quantite) values ('maconnerie de moellons, ep = 35cm');
insert into Travaux(typetravaux, prixunitaire, idunite, quantite) values ('beton armee dosee a 350 kg/m3', 0, NULL, 0);
insert into Travaux(typetravaux, prixunitaire, idunite, quantite) values ('semelles isolee', 573215.80, '2', 0.53);
insert into Travaux(typetravaux, prixunitaire, idunite, quantite) values ('amorces poteaux', 573215.80, '2', 0.56);
insert into Travaux(typetravaux, prixunitaire, idunite, quantite) values ('chainage bas de 20x20', 573215.80, '2', 2.44);
-- insert into Travaux(typetravaux, prixunitaire, idunite, quantite) values ('remblai technique');
-- insert into Travaux(typetravaux, prixunitaire, idunite, quantite) values ('herrissonage ep=10');
-- insert into Travaux(typetravaux, prixunitaire, idunite, quantite) values ('beton ordinaire dosee a 300kg/m3');
-- insert into Travaux(typetravaux, prixunitaire, idunite, quantite) values ('chape de 2 cm');

create sequence sctravauxtypemaison increment by 1 start 1;
create table Travauxtypemaison(
    id varchar(50) DEFAULT nextval('sctravauxtypemaison'),
    idtravaux int references Travaux(numero),
    idtypemaison int references Typemaison(numero),
    numero serial primary key
);
insert into Travauxtypemaison(idtravaux, idtypemaison) values ('5','1');
insert into Travauxtypemaison(idtravaux, idtypemaison) values ('2','1');
insert into Travauxtypemaison(idtravaux, idtypemaison) values ('3','1');

insert into Travauxtypemaison(idtravaux, idtypemaison) values ('3','2');
insert into Travauxtypemaison(idtravaux, idtypemaison) values ('4','2');

insert into Travauxtypemaison(idtravaux, idtypemaison) values ('1','3');
insert into Travauxtypemaison(idtravaux, idtypemaison) values ('2','3');
insert into Travauxtypemaison(idtravaux, idtypemaison) values ('3','3');

insert into Travauxtypemaison(idtravaux, idtypemaison) values ('2','4');
insert into Travauxtypemaison(idtravaux, idtypemaison) values ('3','4');
insert into Travauxtypemaison(idtravaux, idtypemaison) values ('4','4');
insert into Travauxtypemaison(idtravaux, idtypemaison) values ('5','4');

create table Travauxfille
(
    idtravaux int references Travaux(numero),
    idtravauxmere int references Travaux(numero) CHECK (idtravauxmere != idtravaux),
    numero serial primary key
);
insert into Travauxfille (idtravaux, idtravauxmere) values ('6', '5');
insert into Travauxfille (idtravaux, idtravauxmere) values ('7', '5');
insert into Travauxfille (idtravaux, idtravauxmere) values ('8', '5');

create table tempmaisontravaux(
    typemaison varchar(50),
    description varchar(255),
    surface double precision,
    code_travaux varchar(50),
    typetravaux varchar(255),
    unite varchar(50),
    prix_unitaire double precision,
    quantite double precision,
    duree_travaux double precision
);
-- drop table tempmaisontravaux;
create table tempdevis(
    client varchar(50),
    ref_devis varchar(50),
    type_maison varchar(50),
    finition varchar(50),
    taux_finition double precision,
    date_devis timestamp,
    date_debut timestamp,
    lieu varchar(50)
);
-- drop table tempdevis;

create table temppaiement(
    ref_devis varchar(50),
    ref_paiement varchar(50),
    date_paiement timestamp,
    montant double precision
);
-- drop table temppaiement;

-- devis
-- lieu
-- histoprixtravaux
-- histofinition
-- finition
-- utilisateur
-- travauxtypemaison
-- travaux
-- histodureetype
-- typemaison
-- unite

-- \copy Profil from 'D:\ITU\S6\Training Eval\Spring Mandresy - Mixte\Cinepax\base-cinepax\profil.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');
-- alter table profil add COLUMN num_ligne serial;