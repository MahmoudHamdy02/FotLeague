import express from "express";
import session from "express-session";
import pgSession from "connect-pg-simple";
import { pool } from "./db";
import passport from "passport";
import authRouter from "./routes/authRouter";
import leaguesRouter from "./routes/leagueRouter";
import matchRouter from "./routes/matchRouter";

const app = express();

app.use(express.json());

// Postgres auth sessions
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
        maxAge: 1000 * 60 * 5 // Milliseconds
    }
}));
app.use(passport.initialize());
app.use(passport.session());


// Routes
app.get("/", (req, res) => {
    res.status(200).send("Server is running");
});

app.use("/auth/", authRouter);
app.use("/leagues/", leaguesRouter);
app.use("/matches/", matchRouter);


// Start server
app.listen(3000, () => {
    console.log(`Listening on port ${process.env.PORT}`);
});