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
-- Testdaten
--  
INSERT INTO `Case` (crimeId, date, time, status, location)
  SELECT Crime.CrimeId, '2014-03-11', '12:31:00', "open", "Zurich"
  FROM Crime
  WHERE Crime.Crime = "Diebstahl";

INSERT INTO `Case` (crimeId, date, time, status, location)
  SELECT Crime.CrimeId, '2014-03-11', '12:31:00', "open", "Basel"
  FROM Crime
  WHERE Crime.Crime = "Mord";
  
INSERT INTO `Case` (crimeId, date, time, status, location)
  SELECT Crime.CrimeId, '2012-08-23', '23:00:00', "closed", "Aarau"
  FROM Crime
  WHERE Crime.Crime = "Koerperverletzung";  
  
INSERT INTO `Case` (crimeId, date, time, status, location)
  SELECT Crime.CrimeId, '2014-01-13', '09:15:00', "open", "Zurich"
  FROM Crime
  WHERE Crime.Crime = "Bankraub";
  
  
  
  