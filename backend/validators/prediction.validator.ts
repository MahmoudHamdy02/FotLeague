/* eslint-disable array-bracket-newline */
import { body, param } from "express-validator";

export const getUserPredictionsBySeason = [
    param("season")
        .isString()
        .isLength({min: 4, max: 4})
];

export const addPrediction = [
    body("matchId")
        .isInt(),
    body("home").isInt(),
    body("away").isInt()
];

export const updatePrediction = [
    body("matchId")
        .isInt(),
    body("home").isInt(),
    body("away").isInt()
];

export * as predictionValidator from "./prediction.validator";