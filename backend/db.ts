import { Pool } from "pg";

const host = process.env.NODE_ENV === "test"
    ? "db_test"
    : "db";

export const pool = new Pool({
    host: host,
    port: 5432,
    user: process.env.POSTGRES_USER,
    password: process.env.POSTGRES_PASSWORD,
    database: "fotleague"
});
