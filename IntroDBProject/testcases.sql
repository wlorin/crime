use dmdb2014;
--
-- Crime
--
INSERT INTO `Crime` (Crime)
  values("Koerperverletzung");
INSERT INTO `Crime` (Crime)
  values("Mord");
INSERT INTO `Crime` (Crime)
  values("Diebstahl");
INSERT INTO `Crime` (Crime)
  values("Einbruch");
INSERT INTO `Crime` (Crime)
  values("Bankraub");
  
  
--
-- Testdaten Case
--  
INSERT INTO `Case` (crimeId, date, time, status, location, name)
  SELECT Crime.CrimeId, '2014-03-11', '12:31:00', "open", "Zurich", "Diebstahl 1"
  FROM Crime
  WHERE Crime.Crime = "Diebstahl";

INSERT INTO `Case` (crimeId, date, time, status, location, name)
  SELECT Crime.CrimeId, '2014-01-13', '09:15:00', "open", "Rapperswil", "Diebstahl 2"
  FROM Crime
  WHERE Crime.Crime = "Diebstahl"; 
  
INSERT INTO `Case` (crimeId, date, time, status, location, name)
  SELECT Crime.CrimeId, '2014-03-11', '12:31:00', "open", "Basel", "Mord in Basel"
  FROM Crime
  WHERE Crime.Crime = "Mord";
  
INSERT INTO `Case` (crimeId, date, time, status, location, name)
  SELECT Crime.CrimeId, '2014-01-13', '09:15:00', "open", "Zurich", "Mord in Zurich"
  FROM Crime
  WHERE Crime.Crime = "Mord";  
  
INSERT INTO `Case` (crimeId, date, time, status, location, name)
  SELECT Crime.CrimeId, '2013-05-12', '06:20:00', "closed", "Zurich", "Mord 3"
  FROM Crime
  WHERE Crime.Crime = "Mord";
  
INSERT INTO `Case` (crimeId, date, time, status, location, name)
  SELECT Crime.CrimeId, '2012-08-23', '23:00:00', "closed", "Aarau", "Schlägerei"
  FROM Crime
  WHERE Crime.Crime = "Koerperverletzung";  
  
INSERT INTO `Case` (crimeId, date, time, status, location, name)
  SELECT Crime.CrimeId, '2014-10-10', '16:00:00', "open", "Zurich", "Bankraub 1"
  FROM Crime
  WHERE Crime.Crime = "Bankraub";
  
INSERT INTO `Case` (crimeId, date, time, status, location, name)
  SELECT Crime.CrimeId, '2014-02-20', '09:45:00', "closed", "Bern", "Bankraub 2"
  FROM Crime
  WHERE Crime.Crime = "Bankraub";
  
INSERT INTO `Case` (crimeId, date, time, status, location, name)
  SELECT Crime.CrimeId, '2014-04-08', '01:00:00', "open", "Dübendorf", "Case 4"
  FROM Crime
  WHERE Crime.Crime = "Einbruch";  
  
INSERT INTO `Case` (crimeId, date, time, status, location, name)
  SELECT Crime.CrimeId, '2014-03-10', '06:00:00', "open", "Zurich", "Case 5"
  FROM Crime
  WHERE Crime.Crime = "Einbruch"; 
  
INSERT INTO `Case` (crimeId, date, time, status, location, name)
  SELECT Crime.CrimeId, '2014-04-01', '09:15:00', "open", "Zurich", "Case 6"
  FROM Crime
  WHERE Crime.Crime = "Einbruch"; 
  
INSERT INTO `Case` (crimeId, date, time, status, location, name)
  SELECT Crime.CrimeId, '2014-01-18', '14:30:00', "open", "Zurich", "Case 7"
  FROM Crime
  WHERE Crime.Crime = "Einbruch"; 

INSERT INTO `Case` (crimeId, date, time, status, location, name)
  SELECT Crime.CrimeId, '2014-01-13', '09:30:00', "open", "Basel", "Case 8"
  FROM Crime
  WHERE Crime.Crime = "Einbruch"; 

INSERT INTO `Case` (crimeId, date, time, status, location, name)
  SELECT Crime.CrimeId, '2014-02-27', '22:00:00', "open", "Bern", "Case 9"
  FROM Crime
  WHERE Crime.Crime = "Einbruch";  
  

--
--  User
--
INSERT INTO `User` (Name, Password)  
  VALUES ("Max", md5('password'));
  
INSERT INTO `User` (Name, Password)  
  VALUES ("Philipp", md5('password'));
 
INSERT INTO `User` (Name, Password)  
  VALUES ("Anna", md5('password'));

INSERT INTO `User` (Name, Password)  
  VALUES ("Christian", md5('password'));
  
INSERT INTO `User` (Name, Password)  
  VALUES ("Lorin", md5('password'));

INSERT INTO `User` (Name, Password)  
  VALUES ("Nina", md5('password'));  
  
--  
--  Persons of Interest
--
INSERT INTO `PoI` (Name, Birthdate)  
  VALUES ("Sarah Meier", '1980-08-25'); 
  
INSERT INTO `PoI` (Name, Birthdate)  
  VALUES ("Hans Müller", '1953-09-21'); 
  
  INSERT INTO `PoI` (Name, Birthdate)  
  VALUES ("Oliver Sieber", '1991-02-12'); 
  
  INSERT INTO `PoI` (Name, Birthdate)  
  VALUES ("Karl Hinrichsen", '1956-08-10'); 
  
  INSERT INTO `PoI` (Name, Birthdate)  
  VALUES ("Fabienne Neuwirth", '1985-04-18'); 
  
  INSERT INTO `PoI` (Name, Birthdate)  
  VALUES ("Peter Müller", '1968-03-09'); 
  
  INSERT INTO `PoI` (Name, Birthdate)  
  VALUES ("Urs Gerold", '1961-06-03') ;
  
  INSERT INTO `PoI` (Name, Birthdate)  
  VALUES ("Tamara Jäger", '1980-08-25'); 
  
  INSERT INTO `PoI` (Name, Birthdate)  
  VALUES ("Michael Peters", '1972-12-16'); 
  
  INSERT INTO `PoI` (Name, Birthdate)  
  VALUES ("Stefan Brunner", '1970-08-25'); 
  
  INSERT INTO `PoI` (Name, Birthdate)  
  VALUES ("Lukas Casanova", '1979-05-19'); 
  
--
-- CaseNote
--
INSERT INTO `CaseNote` (CaseId, Note, UserId)  
  SELECT Case.CaseId, "Mord durch Schusswaffe, Opfer: Mann, 28 Jahre", User.UserId
  FROM `Case`, User
  WHERE Case.Name = "Mord 3" and User.Name = "Max";   
  
INSERT INTO `CaseNote` (CaseId, Note, UserId)  
  SELECT Case.CaseId, "bisher keine Augenzeugen gefunden", User.UserId
  FROM `Case`, User
  WHERE Case.Name = "Mord 3" and User.Name = "Christian";    
  
INSERT INTO `CaseNote` (CaseId, Note, UserId)  
  SELECT Case.CaseId, "Rucksack gestohlen", User.UserId
  FROM `Case`, User
  WHERE Case.Name = "Diebstahl 1" and User.Name = "Anna"; 
  
INSERT INTO `CaseNote` (CaseId, Note, UserId)  
  SELECT Case.CaseId, "Handy gestohlen, im Zug", User.UserId
  FROM `Case`, User
  WHERE Case.Name = "Diebstahl 2" and User.Name = "Christian";  
  
INSERT INTO `CaseNote` (CaseId, Note, UserId)  
  SELECT Case.CaseId, "durch Balkontüre ins Haus eingebrochen, Bargeld und Schmuck gestohlen", User.UserId
  FROM `Case`, User
  WHERE Case.Name = "Case 5" and User.Name = "Anna";
  
INSERT INTO `CaseNote` (CaseId, Note, UserId)  
  SELECT Case.CaseId, "Täter gefasst", User.UserId
  FROM `Case`, User
  WHERE Case.Name = "Bankraub 2" and User.Name = "Anna";  
  
--
-- PoINote
--  
INSERT INTO `PoINote` (PoIId, Note, UserId)  
  SELECT PoI.PoIId, "kein Alibi für Tatzeit", User.UserId
  FROM PoI, User
  WHERE PoI.Name = "Michael Peters" and User.Name = "Lorin";  
  
--
-- Suspect
--
INSERT INTO `Suspect` (CaseId, PoIId)  
  SELECT Case.CaseId, PoI.PoIId
  FROM `Case`, PoI
  WHERE Case.Name = "Case 4" and PoI.Name = "Lukas Casanova";
  
--
-- Convicted
-- 
INSERT INTO `Convicted` (PoIId, CaseId, CrimeId, Date, Sentence)  
  SELECT PoI.PoIId, Case.CaseId, Crime.CrimeId, '2014-04-11', "Geldstrafe 500 Franken"
  FROM `Case`, PoI, Crime
  WHERE Case.Name = "Case 7" and PoI.Name = "Michael Peters" and Crime.Crime = "Einbruch";
  
INSERT INTO `Convicted` (CaseId, PoIId, CrimeId, Date, Sentence)  
  SELECT Case.CaseId, PoI.PoIId, Crime.CrimeId, '2014-04-11', "Gefängnis 3 Monate"
  FROM `Case`, PoI, Crime
  WHERE Case.Name = "Case 6" and PoI.Name = "Oliver Sieber" and Crime.Crime = "Koerperverletzung";
 
  