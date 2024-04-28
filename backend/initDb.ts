import { readFileSync } from "fs";
import { pool, initPool } from "./db";

// Initialise local postgres database
const init = async () => {
    // Create fotleague database if it doesn't exist
    const db = await initPool.query("SELECT datname FROM pg_catalog.pg_database WHERE datname = 'fotleague'");
    if (db.rowCount === 0) {
        await initPool.query("CREATE DATABASE fotleague;");
    }

    // Run initial database schema query
    const initQuery = readFileSync("./sql/init.sql").toString();
    await pool.query(initQuery);
    console.log("Database initialized!");
};

init();