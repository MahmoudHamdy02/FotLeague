/* eslint-disable array-bracket-newline */
import { body } from "express-validator";

export const createLeague = [
    body("name")
        .isString()
        .isLength({min: 4})
];

export const joinLeague = [
    body("code")
        .isString()
        .isLength({min: 6, max: 6})
];

export const renameLeague = [
    body("leagueId")
        .isInt(),
    body("name")
        .isString()
        .isLength({min: 4})
];

export const generateNewLeagueCode = [
    body("leagueId")
        .isInt(),
];

export const leaveLeague = [
    body("leagueId")
        .isInt(),
];

export * as leagueValidator from "./league.validator";