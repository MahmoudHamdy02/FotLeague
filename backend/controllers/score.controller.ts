import { Request, Response } from "express";
import { scoreService } from "../services/score.service";

export const getTotalUserScoreBySeason = async (req: Request, res: Response) => {
    const userId = req.authUser.id;
    const {season} = req.params;

    try {
        const score = await scoreService.getTotalUserScoreBySeason(userId, parseInt(season));

        return res.status(200).json({score});
    } catch (error) {
        return res.status(400).json({error: "Error getting user season score"});
    }
};

export const getUserScoresBySeason = async (req: Request, res: Response) => {
    const userId = req.authUser.id;
    const {season} = req.params;

    try {
        const scores = await scoreService.getUserScoresBySeason(userId, parseInt(season));

        return res.status(200).json(scores);
    } catch (error) {
        return res.status(400).json({error: "Error getting user scores"});
    }
};

// TODO: Validate request parameters and body
export const getTopGlobalUsersBySeason = async (req: Request, res: Response) => {
    const {season} = req.params;
    const num = parseInt((req.query.num ?? "10").toString());

    try {
        const results = await scoreService.getTopGlobalUsersBySeason(num, parseInt(season));

        return res.status(200).json(results);
    } catch (error) {
        return res.status(400).json({error: "Error getting top global users"});
    }
};

export * as scoreController from "./score.controller";