CREATE SCHEMA mdc;

-- Create Book Table
CREATE TABLE mdc.book (
   id SERIAL PRIMARY KEY,
   title VARCHAR NOT NULL,
   author VARCHAR NOT NULL,
   imageUrl VARCHAR NULL,
   purchaseUrl VARCHAR NULL

)
WITH (
	OIDS=FALSE
) ;

-- Insert Books into table
INSERT INTO mdc.book (title, author) VALUES 
('Crystal Warrior', 'Mike Cooley');
INSERT INTO mdc.book (title, author) VALUES 
('Crystal Origin', 'Mike Cooley');
INSERT INTO mdc.book (title, author) VALUES 
('Crystal Legacy', 'Mike Cooley');
INSERT INTO mdc.book (title, author) VALUES 
('Crystal Fire', 'Mike Cooley');

