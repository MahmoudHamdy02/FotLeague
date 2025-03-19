import { NextFunction, Request, Response } from "express";
import { validationResult } from "express-validator";

export const validate = (req: Request, res: Response, next: NextFunction) => {
    const errors = validationResult(req);
    if (errors.isEmpty()) {
        return next();
    }
    console.log(req.body);

    const errorMsgs = errors.array().map(error => {
        if (error.type == "field")
            return {[error.path]: error.msg};
    });

    return res.status(400).json(errorMsgs);
};