import { Router } from "express";
import passport from "passport";
import "../strategies/local";
import { requireAuth } from "../middleware/requireAuth";
import { authController } from "../controllers/authController";

const router = Router();

// TODO: Admin routes/functionality?

router.get("/status", requireAuth, authController.authStatus);

// TODO: Setup error handling for passport login
router.post("/login/", passport.authenticate("local"), authController.login);

router.post("/signup/", authController.signup);

router.post("/logout", authController.logout);

router.post("/reset", requireAuth, authController.resetPassword);

export default router;