import { Router } from "express";
import passport from "passport";
import "../strategies/local";
import { requireAuth } from "../middleware/requireAuth";
import userService from "../services/userService";

const router = Router();

router.get("/status", requireAuth, (req, res) => {
    res.json({authenticated: req.isAuthenticated()});
});

router.post("/login/", passport.authenticate("local"), (req, res) => {
    res.json({message: "Signed in"});
});

router.post("/signup/", async (req, res) => {
    const { email, password, name, role } = req.body;
    try {
        await userService.createUser(email, password, name, role);
        res.status(201).json({message: "User created"});
    } catch (e) {
        return res.status(400).json({error: "Error creating user"});
    }
});

router.post("/logout", function(req, res) {
    req.logout(function(err) {
        res.json({message: "Logged out", error: err});
    });
});

export default router;