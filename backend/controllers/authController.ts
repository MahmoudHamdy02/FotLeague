import { Request, Response } from "express";
import userService from "../services/userService";

namespace authController {
    export const authStatus = (req: Request, res: Response) => {
        res.json(req.authUser);
    };

    export const login = (req: Request, res: Response) => {
        res.json({message: "Signed in"});
    };

    export const signup = async (req: Request, res: Response) => {
        const { email, password, name, role } = req.body;
        try {
            await userService.createUser(email, password, name, role);
            res.status(201).json({message: "User created"});
        } catch (e) {
            return res.status(400).json({error: "Error creating user"});
        }
    };

    export const logout = (req: Request, res: Response) => {
        req.logout(err => {
            res.json({message: "Logged out", error: err});
        });
    };
}

export default authController;