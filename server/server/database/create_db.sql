###  SQL FILE ###



DROP DATABASE IF EXISTS tlf;
CREATE DATABASE tlf;
USE tlf;


CREATE TABLE IF NOT EXISTS factions (
    faction_id INT,
    faction_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (faction_id)
); 


CREATE TABLE IF NOT EXISTS users (
    account_id INT AUTO_INCREMENT,
    user_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    status TINYINT NOT NULL,
    PRIMARY KEY (account_id)
); 

CREATE TABLE IF NOT EXISTS sectors (
    sector_id INT AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    galactic_x INT,
    galactic_y INT,
    faction_id INT,
    PRIMARY KEY (sector_id),
    FOREIGN KEY(faction_id) REFERENCES factions(faction_id)
); 

CREATE TABLE IF NOT EXISTS npcs(
	npc_id INT AUTO_INCREMENT,
	npc_name VARCHAR(255),
	x_pos FLOAT,
	y_pos FLOAT,
	faction_id INT,
	sector_id INT,
	structure_id INT,
	PRIMARY KEY(npc_id),
	FOREIGN KEY(faction_id) REFERENCES factions(faction_id),
	FOREIGN KEY(sector_id) REFERENCES sectors(sector_id)
);

CREATE TABLE IF NOT EXISTS characters(
	character_id INT AUTO_INCREMENT,
	account_id INT,
	character_name VARCHAR(255),
	character_model INT,
	x_pos FLOAT,
	y_pos FLOAT,
	sector_id INT,
	PRIMARY KEY(character_id),
	FOREIGN KEY(account_id) REFERENCES users(account_id),
        FOREIGN KEY(sector_id) REFERENCES sectors(sector_id)
);


CREATE TABLE IF NOT EXISTS ships(
	ship_id INT AUTO_INCREMENT,
	ship_name VARCHAR(255),
	ship_class VARCHAR(255),
	sector_id INT,
 	x_pos FLOAT,
    	y_pos FLOAT,
    	local_x_pos INT,
    	local_y_pos INT,
    	PRIMARY KEY(ship_id),
    	FOREIGN KEY(sector_id) REFERENCES sectors(sector_id)
);

CREATE TABLE IF NOT EXISTS stations(
	station_id INT AUTO_INCREMENT,
	station_name VARCHAR(255),
	station_class VARCHAR(255),
	sector_id INT,
 	x_pos FLOAT,
    	y_pos FLOAT,
    	local_x_pos INT,
    	local_y_pos INT,
    PRIMARY KEY(station_id),
    FOREIGN KEY(sector_id) REFERENCES sectors(sector_id)
);

CREATE TABLE IF NOT EXISTS ship_rooms(
	room_id INT,
	ship_id INT,
	room_type VARCHAR(255),
	room_name VARCHAR(255),
	FOREIGN KEY(ship_id) REFERENCES ships(ship_id)
);

CREATE TABLE IF NOT EXISTS station_rooms(
	room_id INT,
	station_id INT,
	room_type VARCHAR(255),
	room_name INT,
	FOREIGN KEY(station_id) REFERENCES stations(station_id)
);



INSERT INTO factions (faction_id, faction_name) VALUES (0, 'None');
INSERT INTO factions (faction_id, faction_name) VALUES (1, 'The Kingdom of Vri');
INSERT INTO factions (faction_id, faction_name) VALUES (2, 'The Ex-i');
INSERT INTO factions (faction_id, faction_name) VALUES (3, 'The Lawless');
INSERT INTO factions (faction_id, faction_name) VALUES (4, 'The Bastion of Old Mengiron');
INSERT INTO factions (faction_id, faction_name) VALUES (5, 'The Unknown');

INSERT INTO sectors VALUES(9001, '0-0', 0, 0, 3);
INSERT INTO sectors VALUES(9002, '0-1', 0, 1, 3);
INSERT INTO sectors VALUES(9003, '0-2', 0, 2, 3);




INSERT INTO users VALUES(1, 'sirlachlan', 'pass', '$2a$04$R3jpsZg0OpudMBy.8Cbno.XX1DmvTFyUpLkcVZmt5qjWm.UKZodLO', 0);
INSERT INTO users VALUES(2, 'lachlanmac', 'pass', '$2a$04$4wbYlcHJ71Zfb42PyUhaP.GEUZboWaOZq739FQYfd94LMGuGkPFnG', 0);
INSERT INTO users VALUES(1001, 'testclient1', 'pass', '$2a$04$4wbYlcHJ71Zfb42PyUhaP.GEUZboWaOZq739FQYfd94LMGuGkPFnG', 0);
INSERT INTO users VALUES(1002, 'testclient2', 'pass', '$2a$04$4wbYlcHJ71Zfb42PyUhaP.GEUZboWaOZq739FQYfd94LMGuGkPFnG', 0);
INSERT INTO users VALUES(1003, 'testclient3', 'pass', '$2a$04$4wbYlcHJ71Zfb42PyUhaP.GEUZboWaOZq739FQYfd94LMGuGkPFnG', 0);



INSERT INTO ships VALUES(100, 'Ragnarok', 'Cruiser_A', 9001, 0, 0, 0, 0);
INSERT INTO ships VALUES(101, 'Shenigan', 'Cruiser_A', 9001, 0, 0, 1, 1);
INSERT INTO stations VALUES(1, 'DarNuraOutpost', 'Outpost_A', 9001, 0, 0, 3, 3);


INSERT INTO ship_rooms VALUES(0, 100, 'bridge', 'The Bridge');
INSERT INTO ship_rooms VALUES(1, 100, 'quarters', 'The Captains Quarters');
INSERT INTO ship_rooms VALUES(2, 100, 'quarters', 'The First Officers Quarters');
INSERT INTO ship_rooms VALUES(3, 100, 'shop', 'Outfitting');
INSERT INTO ship_rooms VALUES(4, 100, 'cafe', 'Cafe Solei');
INSERT INTO ship_rooms VALUES(5, 100, 'engineering', 'Engineering Dept. 1');		


INSERT INTO characters VALUES(3588, 1, 'Lachlan', 3954, 10, 10, 9001);
INSERT INTO characters VALUES(5488, 2, 'Drahkon', 3954, 84, 30, 9001);
INSERT INTO characters VALUES(997, 1003, 'Olivia', 3954, 84, 30, 9001);
INSERT INTO characters VALUES(998, 1001, 'testclientone', 3954, 2300, 3850, 9001);
INSERT INTO characters VALUES(999, 1002, 'testclienttwo', 3954, 2320, 3890, 9001);


INSERT INTO npcs VALUES(424, 'TheFirstGuy', 4100, 700, 1, 9001, 100);
INSERT INTO npcs VALUES(425, 'TheSecondGuy', 4150, 710, 1, 9001, 100);

COMMIT;