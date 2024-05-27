import { readFileSync } from "fs";
import { pool } from "../db";

const sql = readFileSync("./sql/init.sql");

export const resetDB = async () => {
    await pool.query("DROP SCHEMA public CASCADE;");
    await pool.query("CREATE SCHEMA public;");
    await pool.query(`GRANT ALL ON SCHEMA public TO ${process.env.POSTGRES_USER};`);
    await pool.query("GRANT ALL ON SCHEMA public TO public;");
    await pool.query(sql.toString());
};
