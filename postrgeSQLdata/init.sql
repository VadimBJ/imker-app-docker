DO $$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_user WHERE usename = 'postgres') THEN
            CREATE USER postgres WITH PASSWORD 'root';
        END IF;
    END $$;

CREATE DATABASE Imker;
GRANT ALL PRIVILEGES ON DATABASE Imker TO postgres;

\echo '-------------Initialization script executed successfully-------------------';