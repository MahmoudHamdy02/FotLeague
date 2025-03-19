import { param } from "express-validator";

export const season = [
    param("season")
        .isString()
        .isLength({min: 4, max: 4})
];


export * as validator from "./validator";
