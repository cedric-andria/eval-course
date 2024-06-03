create database course;
create user course with password 'course';
grant all privileges on database course to course;
-- GRANT pg_read_all_settings TO course;
-- CREATE ROLE copy_role;
-- GRANT COPY ON ALL TABLES IN SCHEMA public to copy_role;
-- GRANT copy_role to course;
\c course postgres;

create table Utilisateur(
    login varchar(50),
    mdp varchar(50),
    profil varchar(10),
    pk serial primary key
);
insert into Utilisateur (login, mdp, profil) values ('admin', 'admin', 'admin');
insert into Utilisateur (login, mdp, profil) values ('zoto', 'zoto', 'equipe');
insert into Utilisateur (login, mdp, profil) values ('hery', 'hery', 'equipe');
insert into Utilisateur (login, mdp, profil) values ('tanjaka', 'tanjaka', 'equipe');
insert into Utilisateur (login, mdp, profil) values ('finoana', 'finoana', 'equipe');

create table Param_points_rang(
    rang int NOT NULL UNIQUE,
    points double precision NOT NULL,
    pk serial primary key,
    created_at timestamp DEFAULT now()
);
insert into Param_points_rang (rang, points) values (1, 10);
insert into Param_points_rang (rang, points) values (2, 6);
insert into Param_points_rang (rang, points) values (3, 4);
insert into Param_points_rang (rang, points) values (4, 2);
insert into Param_points_rang (rang, points) values (5, 1);

create sequence scetape increment by 1 start 1;
create table Etape(
    id varchar(50) default nextval('scetape'),
    nom varchar(100),
    longueur double precision,
    nbcoureur_equipe int,
    rang int,
    date_depart timestamp,
    pk serial primary key
);
insert into Etape(nom, longueur, nbcoureur_equipe, rang, date_depart) values ('Fiakarana', 5, 2, 2, '2024-01-01 11:00:00');
insert into Etape(nom, longueur, nbcoureur_equipe, rang, date_depart) values ('Fidinana', 10, 1, 4, '2024-01-02 16:00:00');
insert into Etape(nom, longueur, nbcoureur_equipe, rang, date_depart) values ('Plat', 10, 3, 1, '2024-01-01 08:00:00');
insert into Etape(nom, longueur, nbcoureur_equipe, rang, date_depart) values ('Fauxplat', 5, 2, 3, '2024-01-01 15:00:00');
insert into Etape(nom, longueur, nbcoureur_equipe, rang, date_depart) values ('Fiakarana', 5, 2, 5, '2024-01-02 12:00:00');

create sequence scequipe increment by 1 start 1;
create table Equipe(
    id varchar(50) default nextval('scequipe'),
    nom VARCHAR(50),
    idutilisateur int references Utilisateur(pk),
    pk serial primary key
);
insert into Equipe(nom, idutilisateur) values ('Zoto', 2);
insert into Equipe(nom, idutilisateur) values ('Hery', 3);
insert into Equipe(nom, idutilisateur) values ('Tanjaka', 4);
insert into Equipe(nom, idutilisateur) values ('Finoana', 5);

create sequence scgenre increment by 1 start 1;
create table Genre(
    id varchar(50) DEFAULT nextval('scgenre'),
    nom varchar(20),
    pk serial primary key
);
insert into genre(nom) values ('Homme');
insert into genre(nom) values ('Femme');

create sequence sccoureur increment by 1 start 1;
create table Coureur(
    dossard varchar(100) NOT NULL UNIQUE default nextval('sccoureur'),
    nom varchar(100),
    idgenre int references Genre(pk),
    datenaissance date,
    pointtotal double precision DEFAULT 0,
    idequipe int references equipe(pk),
    pk serial primary key
);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('Jean', 1, '2003-01-01', 1);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('Jacques', 1, '2002-01-01', 1);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('Jules', 1, '2001-01-01', 1);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('Jim', 1, '2000-01-01', 1);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('John', 1, '1999-01-01', 1);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('Jeanne', 2, '1998-01-01', 2);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('Julie', 2, '1997-01-01', 2);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('Joelle', 2, '1996-01-01', 2);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('Marthe', 2, '1995-01-01', 2);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('Marie', 2, '1994-01-01', 2);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('Louise', 2, '1993-01-01', 3);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('Luc', 1, '1992-01-01', 3);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('Lillie', 2, '1991-01-01', 3);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('Lampard', 1, '1990-01-01', 3);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('Lenny', 1, '1989-01-01', 3);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('Paul', 1, '1988-01-01', 4);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('Pierre', 1, '1987-01-01', 4);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('Perline', 2, '1986-01-01', 4);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('Pavard', 1, '1985-01-01', 4);
insert into Coureur(nom, idgenre, datenaissance, idequipe) values ('Pupuce', 2, '1984-01-01', 4);

create sequence sccategorie increment by 1 start 1;
create table Categorie(
    id varchar(50) DEFAULT nextval('sccategorie'),
    nom varchar(50),
    pk serial primary key
);
insert into Categorie(nom) values ('Homme');
insert into Categorie(nom) values ('Femme');
insert into Categorie(nom) values ('Junior');
insert into Categorie(nom) values ('Senior');

create table Categorie_coureur(
    idcoureur int references Coureur(pk),
    idcategorie int references Categorie(pk),
    pk serial primary key, 
    created_at timestamp DEFAULT now()
);
insert into Categorie_coureur(idcoureur, idcategorie) values (1, 1);
insert into Categorie_coureur(idcoureur, idcategorie) values (1, 3);
insert into Categorie_coureur(idcoureur, idcategorie) values (2, 1);
insert into Categorie_coureur(idcoureur, idcategorie) values (2, 3);
insert into Categorie_coureur(idcoureur, idcategorie) values (3, 1);
insert into Categorie_coureur(idcoureur, idcategorie) values (3, 3);
insert into Categorie_coureur(idcoureur, idcategorie) values (4, 1);
insert into Categorie_coureur(idcoureur, idcategorie) values (4, 3);
insert into Categorie_coureur(idcoureur, idcategorie) values (5, 1);
insert into Categorie_coureur(idcoureur, idcategorie) values (5, 3);
insert into Categorie_coureur(idcoureur, idcategorie) values (6, 2);
insert into Categorie_coureur(idcoureur, idcategorie) values (6, 3);
insert into Categorie_coureur(idcoureur, idcategorie) values (7, 2);
insert into Categorie_coureur(idcoureur, idcategorie) values (7, 3);
insert into Categorie_coureur(idcoureur, idcategorie) values (8, 2);
insert into Categorie_coureur(idcoureur, idcategorie) values (8, 3);
insert into Categorie_coureur(idcoureur, idcategorie) values (9, 2);
insert into Categorie_coureur(idcoureur, idcategorie) values (9, 3);
insert into Categorie_coureur(idcoureur, idcategorie) values (10, 2);
insert into Categorie_coureur(idcoureur, idcategorie) values (11, 2);
insert into Categorie_coureur(idcoureur, idcategorie) values (12, 2);
insert into Categorie_coureur(idcoureur, idcategorie) values (13, 2);
insert into Categorie_coureur(idcoureur, idcategorie) values (14, 2);
insert into Categorie_coureur(idcoureur, idcategorie) values (15, 2);
insert into Categorie_coureur(idcoureur, idcategorie) values (16, 2);
insert into Categorie_coureur(idcoureur, idcategorie) values (17, 2);
insert into Categorie_coureur(idcoureur, idcategorie) values (18, 2);
insert into Categorie_coureur(idcoureur, idcategorie) values (19, 2);
insert into Categorie_coureur(idcoureur, idcategorie) values (20, 2);

create sequence scaffectation increment by 1 start 1;
create table Affectation_coureur(
    id varchar(100) DEFAULT nextval('scaffectation'),
    idcoureur int references Coureur(pk),
    idetape int references Etape(pk),
    pk serial primary key
);
-- drop table Affectation_coureur;
-- drop sequence scaffectation;
--mety
-- insert into Affectation_coureur(idcoureur, idetape) values (3, 3);
-- insert into Affectation_coureur(idcoureur, idetape) values (2, 3);
-- insert into Affectation_coureur(idcoureur, idetape) values (5, 3);

--mety
-- insert into Affectation_coureur(idcoureur, idetape) values (6, 3);
-- insert into Affectation_coureur(idcoureur, idetape) values (7, 3);
-- insert into Affectation_coureur(idcoureur, idetape) values (8, 3);

--mety
-- insert into Affectation_coureur(idcoureur, idetape) values (6, 1);
-- insert into Affectation_coureur(idcoureur, idetape) values (10, 1);

--tsy ampy
-- insert into Affectation_coureur(idcoureur, idetape) values (19, 3);
-- insert into Affectation_coureur(idcoureur, idetape) values (20, 3);

--miotra
-- insert into Affectation_coureur(idcoureur, idetape) values (2, 1);
-- insert into Affectation_coureur(idcoureur, idetape) values (3, 1);
-- insert into Affectation_coureur(idcoureur, idetape) values (5, 1);

create sequence schistoetapecoureur increment by 1 start 1;
create table Histo_etape_coureur(
    id varchar(50) default nextval('schistoetapecoureur'),
    idaffectation int references Affectation_coureur(pk),
    heuredepart timestamp,
    heurearrivee timestamp,
    points double precision DEFAULT 0,
    rang int DEFAULT 0,
    pk serial primary key
);
-- drop table Histo_etape_coureur;
-- drop sequence schistoetapecoureur;
-- insert into Histo_etape_coureur (idaffectation, heuredepart, heurearrivee) values (1, '2024-01-01 08:00:00', '2024-01-01 09:00:00');
-- insert into Histo_etape_coureur (idaffectation, heuredepart, heurearrivee) values (2, '2024-01-01 08:00:00', '2024-01-01 10:00:00');
-- insert into Histo_etape_coureur (idaffectation, heuredepart, heurearrivee) values (3, '2024-01-01 08:00:00', '2024-01-01 09:30:00');
-- insert into Histo_etape_coureur (idaffectation, heuredepart, heurearrivee) values (4, '2024-01-01 08:00:00', '2024-01-01 08:30:00',);
-- insert into Histo_etape_coureur (idaffectation, heuredepart, heurearrivee) values (5, '2024-01-01 08:00:00', '2024-01-01 08:45:00',);
-- insert into Histo_etape_coureur (idaffectation, heuredepart, heurearrivee) values (6, '2024-01-01 08:00:00', '2024-01-01 08:50:00',);
-- insert into Histo_etape_coureur (idaffectation, heuredepart, heurearrivee) values (7, '2024-01-01 11:00:00', '2024-01-01 13:00:00');
-- insert into Histo_etape_coureur (idaffectation, heuredepart, heurearrivee) values (8, '2024-01-01 11:00:00', '2024-01-01 14:00:00');

create sequence scparametre_penalite increment by 1 start 1;
create table Parametre_penalite (
    id varchar(50) default nextval('scparametre_penalite'),
    idetape int references Etape(pk),
    points double precision,
    pk serial primary key
);

create sequence scpenalite increment by 1 start 1;
create table Penalite(
    id varchar(50) DEFAULT nextval('scpenalite'),
    idparametre int references Parametre_penalite(pk),
    idcoureur int references Coureur(pk),
    pk serial primary key,
    created_at timestamp default now()
);

