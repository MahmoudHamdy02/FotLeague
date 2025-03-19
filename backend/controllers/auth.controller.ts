import { Request, Response } from "express";
import passport from "passport";
import { userService } from "../services/user.service";
import bcrypt from "bcryptjs";
import { UserRole } from "../enums/UserRole";
import { validate } from "./utils";

export const authStatus = (req: Request, res: Response) => {
    const {password: _, ...details} = req.authUser;
    res.status(200).json(details);
};

export const login = (req: Request, res: Response) => {
    const { rememberMe } = req.body;
    if (!validate([rememberMe], ["boolean"], res)) return;

    if (rememberMe) {
        req.session.cookie.maxAge = 1000 * 60 * 60 * 24 * 30; // 1 Month
        req.session.save();
    }
    const {password: _, ...details} = req.user!;
    res.json(details);
};

export const signup = async (req: Request, res: Response) => {
    const { email, password, name } = req.body;
    if (!validate([email, password, name], ["string", "string", "string"], res)) return;

    try {
        const user = await userService.createUser(email, password, name, UserRole.User);

        passport.authenticate("local")(req, res, () => {
            res.status(201).json(user);
        });

    } catch (error) {
        return res.status(400).json({error: "Error creating user"});
    }
};

export const resetPassword = async (req: Request, res: Response) => {
    const user = req.authUser;
    const { oldPassword, newPassword } = req.body;
    if (!validate([oldPassword, newPassword], ["string", "string"], res)) return;


    try {
        const passwordsMatch = await bcrypt.compare(oldPassword, user.password);

        if (!passwordsMatch)
            return res.status(403).json({error: "Old user password is incorrect"});

        const newUser = await userService.changeUserPassword(user.id, newPassword);

        req.logout(err => {
            return res.status(200).json({user: newUser, error: err});
        });

    } catch (error) {
        return res.status(400).json({error: "Error updating password"});
    }
};

export const logout = (req: Request, res: Response) => {
    req.logout(err => {
        res.status(200).json({message: "Logged out", error: err});
    });
};

export * as authController from "./auth.controller";