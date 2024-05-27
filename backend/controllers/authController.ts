import { Request, Response } from "express";
import * as userService from "../services/userService";
import passport from "passport";

export const authStatus = (req: Request, res: Response) => {
    res.json(req.authUser);
};

export const login = (req: Request, res: Response) => {
    res.json({message: "Signed in"});
};

export const signup = async (req: Request, res: Response) => {
    const { email, password, name, role } = req.body;
    // TODO: Send back specific errors
    try {
        const user = await userService.createUser(email, password, name, role);

        passport.authenticate("local")(req, res, () => {
            res.status(201).json(user);
        });

    } catch (error) {
        return res.status(400).json({error: "Error creating user"});
    }
};

export const logout = (req: Request, res: Response) => {
    req.logout(err => {
        res.status(200).json({message: "Logged out", error: err});
    });
};
