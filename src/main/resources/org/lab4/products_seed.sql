CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    image_path VARCHAR(255) NOT NULL,
    CONSTRAINT products_name_brand_unique UNIQUE (name, brand)
);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'products_name_brand_unique'
    ) THEN
        ALTER TABLE products
        ADD CONSTRAINT products_name_brand_unique UNIQUE (name, brand);
    END IF;
END $$;

INSERT INTO products (name, price, brand, description, image_path) VALUES
('4DFWD PULSE SHOES', 160.00, 'Adidas', 'This product is excluded from all promotional discounts.', '/org/lab3/img1.png'),
('FORUM MID SHOES', 100.00, 'Adidas', 'This product is excluded.', '/org/lab3/img2.png'),
('SUPERNOVA SHOES', 150.00, 'Adidas', 'NMD City Stock 2 series.', '/org/lab3/img3.png'),
('Adidas Originals', 160.00, 'Adidas', 'NMD City Stock 2 classic look.', '/org/lab3/img4.png'),
('Adidas Dark Knight', 120.00, 'Adidas', 'NMD City Stock 2 running shoes.', '/org/lab3/img5.png'),
('4DFWD PULSE ORANGE', 160.00, 'Adidas', 'Special limited orange edition.', '/org/lab3/img6.png')
ON CONFLICT ON CONSTRAINT products_name_brand_unique DO NOTHING;
