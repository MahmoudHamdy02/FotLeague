import { Request, Response } from "express";
import * as predictionService from "../services/predictionService";

export const getUserPredictionsBySeason = async (req: Request, res: Response) => {
    const { season } = req.params;
    const userId = req.authUser.id;

    try {
        const predictions = await predictionService.getUserPredictionsBySeason(userId, season);
        res.status(200).json(predictions);
    } catch (error) {
        console.log(error);
        res.status(400).json({error: "Error getting predictions"});
    }
};

export const addPrediction = async (req: Request, res: Response) => {
    const { user_id, match_id, home, away } = req.body;
    try {
        const prediction = await predictionService.addPrediction({user_id, match_id, home, away});
        res.status(201).json(prediction);
    } catch (error) {
        res.status(400).json({error: "Error adding prediction"});
    }
};

export const updatePrediction = async (req: Request, res: Response) => {
    const { match_id, home, away } = req.body;
    const userId = req.authUser.id;
    try {
        const prediction = await predictionService.editPredictionScores(userId, match_id, home, away);
        res.status(201).json(prediction);
    } catch (error) {
        res.status(400).json({error: "Error updating prediction"});
    }
};