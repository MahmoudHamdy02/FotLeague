/* eslint-disable array-bracket-newline */
import { body } from "express-validator";

export const signup = [
    body("email", "Please enter a valid email").isEmail(),
    body("password", "Password must have at least 8 characters and include one lowercase character, one uppercase character, a number, and a special character.")
        .isString()
        .matches(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$/, "i"),
    body("name", "Please enter a valid username")
        .isString()
        .trim()
        .not()
        .contains(" ")
        .isLength({min: 6})
];

export const login = [
    body("rememberMe", "Please select a valid value")
        .isBoolean()
];

export const resetPassword = [
    body("oldPassword", "Please enter a valid value")
        .isString()
        .not()
        .isEmpty(),
    body("newPassword", "Password must have at least 8 characters and include one lowercase character, one uppercase character, a number, and a special character.")
        .isString()
        .matches(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$/, "i"),
];

export * as authValidator from "./auth.validator";