import express from "express";
import session from "express-session";
import pgSession from "connect-pg-simple";
import { pool } from "./db";
import passport from "passport";
import authRouter from "./routes/auth";
import leaguesRouter from "./routes/leagues";

const app = express();

app.use(express.json());

app.use(session({
    store: new (pgSession(session))({
        pool: pool,
        createTableIfMissing: true,
        pruneSessionInterval: 900 // Seconds
    }),
    secret: process.env.SESSION_SECRET!,
    resave: false,
    saveUninitialized: false,
    cookie: {
        maxAge: 1000 * 20 // Milliseconds
    }
}));
app.use(passport.initialize());
app.use(passport.session());

app.use("/auth/", authRouter);

app.use("/leagues/", leaguesRouter);

app.get("/", (req, res) => {
    console.log("server pinged");
    res.status(200).send("Server is running");
});

app.get("/data", async (req, res) => {
    const data = await pool.query("SELECT * FROM match_status");
    res.status(200).json(data.rows);
});

app.post("/fbref", (req, res) => {
    const body = req.body;
    console.log(body);
    return res.status(200).json({ok: true});
});

app.listen(3000, () => {
    console.log(`Listening on port ${process.env.PORT}`);
});