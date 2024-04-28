import express from "express";
import { pool } from "./db";

const app = express();
app.use(express.json());

app.get("/", (req, res) => {
    res.status(200).send("Server is running");
});

app.get("/data", async (req, res) => {
    const data = await pool.query("SELECT * FROM test");
    res.status(200).json(data.rows);
});

app.listen(3000, () => {
    console.log(`Listening on port ${process.env.PORT}`);
});