import { NextFunction, Request, Response } from "express";

export const requireAuth = (req: Request, res: Response, next: NextFunction) => {
    if (req.isAuthenticated()) {
        req.authUser = req.user;
        next();
    }
    else
        return res.status(401).json({error: "User is not authenticated"});
};