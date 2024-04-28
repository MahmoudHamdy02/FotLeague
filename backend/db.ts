import { Pool } from "pg";

export const pool = new Pool({
    host: "db",
    port: 5432,
    user: process.env.POSTGRES_USER,
    password: process.env.POSTGRES_PASSWORD,
    database: "fotleague"
});

export const initPool = new Pool({
    host: "db",
    port: 5432,
    user: process.env.POSTGRES_USER,
    password: process.env.POSTGRES_PASSWORD,
});