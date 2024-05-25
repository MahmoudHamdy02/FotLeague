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