import { Request, Response } from "express";
import { predictionService } from "../services/prediction.service";
import { validate } from "./utils";
import { matchService } from "../services/match.service";

export const getUserPredictionsForCurrentSeason = async (req: Request, res: Response) => {
    const userId = req.authUser.id;

    try {
        const currenstSeason = await matchService.getCurrentSeason();
        const predictions = await predictionService.getUserPredictionsBySeason(userId, currenstSeason);
        res.status(200).json(predictions);
    } catch (error) {
        res.status(400).json({error: "Error getting predictions"});
    }
};

export const getUserPredictionsBySeason = async (req: Request, res: Response) => {
    const userId = req.authUser.id;
    const { season } = req.params;

    try {
        const predictions = await predictionService.getUserPredictionsBySeason(userId, parseInt(season));
        res.status(200).json(predictions);
    } catch (error) {
        res.status(400).json({error: "Error getting predictions"});
    }
};

export const addPrediction = async (req: Request, res: Response) => {
    const userId = req.authUser.id;
    const { matchId, home, away } = req.body;
    if (!validate([matchId, home, away], ["number", "number", "number"], res)) return;

    try {
        const prediction = await predictionService.addPrediction(userId, matchId, home, away);
        res.status(201).json(prediction);
    } catch (error) {
        res.status(400).json({error: "Error adding prediction"});
    }
};

export const updatePrediction = async (req: Request, res: Response) => {
    const userId = req.authUser.id;
    const { matchId, home, away } = req.body;
    if (!validate([matchId, home, away], ["number", "number", "number"], res)) return;

    try {
        const prediction = await predictionService.editPredictionScores(userId, matchId, home, away);
        res.status(201).json(prediction);
    } catch (error) {
        res.status(400).json({error: "Error updating prediction"});
    }
};

export * as predictionController from "./prediction.controller";