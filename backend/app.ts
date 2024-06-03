import express from "express";
import session from "express-session";
import pgSession from "connect-pg-simple";
import { pool } from "./db";
import passport from "passport";
import dotenv from "dotenv";
import { authRouter } from "./routes/auth.router";
import { leagueRouter } from "./routes/league.router";
import { matchRouter } from "./routes/match.router";
import { predictionRouter } from "./routes/prediction.router";
import { scoreRouter } from "./routes/score.router";
dotenv.config({path: "../.env"});

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
        // Default: 15 Minutes
        // Remember me: 1 Month
        maxAge: 1000 * 60 * 15 // Milliseconds
    }
}));
app.use(passport.initialize());
app.use(passport.session());


// Routes
app.get("/", (req, res) => {
    res.status(200).json({message: "Server is running"});
});

app.use("/auth/", authRouter);
app.use("/leagues/", leagueRouter);
app.use("/matches/", matchRouter);
app.use("/predictions/", predictionRouter);
app.use("/scores/", scoreRouter);

export default app;
