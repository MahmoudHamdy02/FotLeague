import { Request, Response } from "express";
import passport from "passport";
import { userService } from "../services/user.service";
import bcrypt from "bcryptjs";
import { UserRole } from "../enums/UserRole";

export const authStatus = (req: Request, res: Response) => {
    res.json(req.authUser);
};

export const login = (req: Request, res: Response) => {
    const { rememberMe } = req.body;
    if (rememberMe !== undefined && rememberMe) {
        req.session.cookie.maxAge = 1000 * 60 * 60 * 24 * 30; // 1 Month
        req.session.save();
    }
    res.json({message: "Signed in"});
};

export const signup = async (req: Request, res: Response) => {
    const { email, password, name } = req.body;
    // TODO: Send back specific errors
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
    const userId = req.authUser.id;
    const { oldPassword, newPassword } = req.body;

    try {
        const userDetails = await userService.getUserById(userId);

        const passwordsMatch = await bcrypt.compare(oldPassword, userDetails.password);

        if (!passwordsMatch)
            return res.status(403).json({error: "Old user password is incorrect"});

        const newUser = await userService.changeUserPassword(userId, newPassword);

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