import { Router } from "express";
import bcrypt from "bcryptjs";
import passport from "passport";
import "../strategies/local";
import { pool } from "../db";
import { requireAuth } from "../middleware/requireAuth";

const router = Router();

router.get("/status", requireAuth, (req, res) => {
    res.json({authenticated: req.isAuthenticated()});
});

router.post("/login/", passport.authenticate("local"), (req, res) => {
    res.json({message: "Signed in"});
});

router.post("/signup/", async (req, res) => {
    const { name, email, password } = req.body;

    const salt = await bcrypt.genSalt(10);
    const hashedPassword = await bcrypt.hash(password, salt);
    const data = await pool.query("INSERT INTO users(email, password, name) VALUES ($1, $2, $3);", [email, hashedPassword, name]);
    console.log(data);

    res.json({message: "User created"});
});

router.post("/logout", function(req, res) {
    req.logout(function(err) {
        res.json({message: "Logged out", error: err});
    });
});

export default router;