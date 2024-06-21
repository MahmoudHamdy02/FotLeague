import { Router } from "express";
import passport from "passport";
import "../strategies/local";
import { requireAuth } from "../middleware/requireAuth";
import { authController } from "../controllers/auth.controller";

export const authRouter = Router();

// TODO: Admin routes/functionality: no. of registered/logged in users, no. of created leagues?
// TODO: Google OAuth (& twitter/facebook?)
// TODO: Forgot password

authRouter.get("/status", requireAuth, authController.authStatus);

// TODO: Setup error handling for passport login
authRouter.post("/login/", passport.authenticate("local"), authController.login);

authRouter.post("/signup/", authController.signup);

authRouter.post("/logout", authController.logout);

authRouter.post("/reset", requireAuth, authController.resetPassword);
