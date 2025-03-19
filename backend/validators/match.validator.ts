/* eslint-disable array-bracket-newline */
import { body } from "express-validator";

export const initializeMatches = [
    body("matches").isArray()
];

export const updateMatch = [
    body("matchId").isInt(),
    body("status").isInt(),
    body("homeScore").isInt(),
    body("awayScore").isInt(),
    body("datetime").isString(),
    body("liveTime")
        .isString()
        .optional({values: "null"})
];

export * as matchValidator from "./match.validator";
