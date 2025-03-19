/* eslint-disable array-bracket-newline */
import { body } from "express-validator";

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