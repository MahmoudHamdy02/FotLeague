import { Router } from "express";
import passport from "passport";
import "../strategies/local";
import { requireAuth } from "../middleware/requireAuth";
import { authController } from "../controllers/auth.controller";
import { validate } from "../validators/validate";
import { authValidator } from "../validators/auth.validator";

export const authRouter = Router();

authRouter.get("/status", requireAuth, authController.authStatus);

authRouter.post("/login/", authValidator.login, validate, passport.authenticate("local"), authController.login);

authRouter.post("/signup/", authValidator.signup, validate, authController.signup);

authRouter.post("/logout", authController.logout);

authRouter.post("/reset", requireAuth, authValidator.resetPassword, validate, authController.resetPassword);
