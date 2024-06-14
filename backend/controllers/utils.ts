import { Response } from "express";

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const validate = (variables: any[], types: string[], res: Response): boolean => {
    for (let i=0; i<variables.length; i++) {
        const validType = types[i] === "array"
            ? variables[i] instanceof Array
            : typeof variables[i] === types[i];
        if (variables[i] === undefined || !validType) {
            res.status(400).json({error: "Invalid argument(s)"});
            return false;
        }
    }
    return true;
};
