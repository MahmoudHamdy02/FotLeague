import { readFileSync } from "fs";
import { pool, initPool } from "./db";

const init = async () => {
    const db = await initPool.query("SELECT datname FROM pg_catalog.pg_database WHERE datname = 'fotleague'");
    if (db.rowCount === 0) {
        await initPool.query("CREATE DATABASE fotleague;");
    }
    const initQuery = readFileSync("./sql/init.sql").toString();
    await pool.query(initQuery);
    console.log("Database initialized!");
};

init();